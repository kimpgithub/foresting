import firebase_admin
from firebase_admin import credentials, firestore
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from flask import Flask, request, jsonify, render_template

from flask_cors import CORS  # CORS 추가

# Flask 앱 초기화
app = Flask(__name__)
CORS(app)  # CORS 설정 추가

# Firebase 초기화
cred = credentials.Certificate("./sanlim-9fda3-firebase-adminsdk-xgffu-12566fe464.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

# 모든 사용자들의 방문 기록 불러오기
def get_all_user_visits():
    users_ref = db.collection('users')
    users_docs = users_ref.stream()
    user_visits = {doc.id: doc.to_dict().get('forests', []) for doc in users_docs}
    return user_visits

# 모든 산림 데이터 불러오기
def get_all_forests():
    forests_ref = db.collection('forests')
    forests_docs = forests_ref.stream()
    all_forests = {doc.id: doc.to_dict() for doc in forests_docs}
    return all_forests

# 사용자 입력 처리 및 필터링
def filter_forests(user_input, all_forests):
    filtered_forests = {}
    for forest_id, forest_data in all_forests.items():
        if user_input['region'] in forest_data['시도명'] and \
                any(activity in forest_data['활동'] for activity in user_input['activities']) and \
                any(facility in forest_data['시설'] for facility in user_input['facilities']):
            filtered_forests[forest_id] = forest_data
    return filtered_forests

# 사용자-산림 매트릭스 생성
def create_user_forest_matrix(user_visits, all_forests):
    all_forest_ids = list(all_forests.keys())
    data = {user_id: [1 if forest_id in forests else 0 for forest_id in all_forest_ids] for user_id, forests in user_visits.items()}
    user_forest_df = pd.DataFrame(data, index=all_forest_ids).T
    return user_forest_df

# 산림 추천 함수
def recommend_forests(user_id, user_forest_matrix, filtered_forests, all_forests, user_input, n_recommendations=5):
    user_similarities = cosine_similarity(user_forest_matrix)
    user_sim_matrix = pd.DataFrame(user_similarities, index=user_forest_matrix.index, columns=user_forest_matrix.index)
    user_sim_scores = user_sim_matrix[user_id]

    user_forests = set(user_forest_matrix.columns[user_forest_matrix.loc[user_id] == 1])
    non_visited_forests = set(filtered_forests.keys()) - user_forests

    forest_scores = {}
    for forest in non_visited_forests:
        score = sum(user_sim_scores[other_user_id] for other_user_id in user_forest_matrix.index if user_forest_matrix.loc[other_user_id, forest] == 1)
        forest_scores[forest] = score

    recommended_forests = sorted(forest_scores, key=forest_scores.get, reverse=True)[:n_recommendations]

    if not recommended_forests:
        region_filtered_forests = {forest_id: forest_data for forest_id, forest_data in all_forests.items() if user_input['region'] in forest_data['시도명']}
        recommended_forests = list(region_filtered_forests.keys())[:n_recommendations]

    return recommended_forests

# Flask 엔드포인트 정의
@app.route('/')
def home():
    return "테스트"

@app.route('/test')
def test():
    return render_template('index.html')

@app.route('/options')
def options():
    all_forests = get_all_forests()

    regions = set(forest['시도명'] for forest in all_forests.values())
    activities = set(activity for forest in all_forests.values() for activity in eval(forest['활동']))
    facilities = set(facility for forest in all_forests.values() for facility in eval(forest['시설']))

    return jsonify({
        'regions': sorted(regions),
        'activities': sorted(activities),
        'facilities': sorted(facilities)
    })

@app.route('/recommend', methods=['POST'])
def recommend():
    data = request.get_json()
    user_id = data.get('user_id')
    user_input = {
        'region': data.get('region'),
        'activities': data.get('activities', []),
        'facilities': data.get('facilities', [])
    }

    print(f"Received request for user_id: {user_id}, input: {user_input}")

    # 데이터 로드 및 처리
    all_user_visits = get_all_user_visits()
    all_forests = get_all_forests()
    filtered_forests = filter_forests(user_input, all_forests)
    user_forest_matrix = create_user_forest_matrix(all_user_visits, all_forests)
    recommendations = recommend_forests(user_id, user_forest_matrix, filtered_forests, all_forests, user_input)

    print(f"Recommendations: {recommendations}")

    return jsonify(recommendations)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')

package com.example.san_lim.screens.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.example.san_lim.ui.theme.ColorPalette

//InfoScreen.kt
@Composable
fun InfoScreen(recommendations: List<String>) {
    val context = LocalContext.current
    val lodges = loadForestLodgesFromJSON(context, "updated_forest_data.json")
    val filteredLodges = lodges.filter { it.휴양림명 in recommendations }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.earthyLightGreen)  // 박스의 배경색을 earthyLightGreen으로 설정
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()  // 화면 전체를 채우도록 설정
        ) {
            items(filteredLodges) { lodge ->
                LodgeCard(lodge)
            }
        }
    }
}

@Composable
fun LodgeCard(lodge: ForestLodge) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = ColorPalette.softWhite) // 배경색 변경
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 이미지
            val imageUrl = lodge.이미지URL ?: "https://via.placeholder.com/150"
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 기본 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = lodge.휴양림명,
                        style = MaterialTheme.typography.headlineMedium,
                        color = ColorPalette.darkCharcoal // 텍스트 색상 변경
                    )
                    Text(
                        text = lodge.시도명,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ColorPalette.lightCharcoal // 텍스트 색상 변경
                    )
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = ColorPalette.darkCharcoal // 아이콘 색상 변경
                    )
                }
            }

            // 펼쳐졌을 때 표시되는 상세 정보
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Type: ${lodge.휴양림구분}", color = ColorPalette.darkCharcoal)
                Text(text = "Area: ${lodge.휴양림면적} m²", color = ColorPalette.darkCharcoal)
                Text(text = "Capacity: ${lodge.수용인원수}", color = ColorPalette.darkCharcoal)
                Text(text = "Entrance Fee: ${lodge.입장료}", color = ColorPalette.darkCharcoal)
                Text(text = "Accommodation: ${lodge.숙박가능여부}", color = ColorPalette.darkCharcoal)
                Text(text = "Main Facilities: ${lodge.주요시설명}", color = ColorPalette.darkCharcoal)
                Text(text = "Address: ${lodge.소재지도로명주소}", color = ColorPalette.darkCharcoal)
                Text(text = "Phone Number: ${lodge.휴양림전화번호 ?: "N/A"}", color = ColorPalette.darkCharcoal)

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        lodge.홈페이지주소?.let { url ->
                            val fullUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
                                url
                            } else {
                                "http://$url"
                            }
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally), // 버튼을 가로 중앙에 정렬
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPalette.earthyDarkMoss) // 버튼 색상 변경
                ) {
                    Text(
                        text = "Visit Website",
                        color = ColorPalette.softWhite
                    )  // 버튼 텍스트 추가 (필요할 경우)
                }
            }
        }
    }
}// Utility functions

data class ForestLodge(
    val 휴양림명: String,
    val 시도명: String,
    val 휴양림구분: String,
    val 휴양림면적: Double,
    val 수용인원수: Int,
    val 입장료: String,
    val 숙박가능여부: String,
    val 주요시설명: String,
    val 소재지도로명주소: String,
    val 관리기관명: String,
    val 휴양림전화번호: String?,
    val 홈페이지주소: String?,
    val 위도: Double,
    val 경도: Double,
    val 이미지URL: String?
)

fun loadForestLodgesFromJSON(context: Context, fileName: String): List<ForestLodge> {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val gson = Gson()
    val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
    val recordsJsonArray = jsonObject.getAsJsonArray("records")
    val listType = object : TypeToken<List<Map<String, String>>>() {}.type
    val records = gson.fromJson<List<Map<String, String>>>(recordsJsonArray, listType)

    return records.map { record ->
        ForestLodge(
            휴양림명 = record["휴양림명"] ?: "",
            시도명 = record["시도명"] ?: "",
            휴양림구분 = record["휴양림구분"] ?: "",
            휴양림면적 = record["휴양림면적"]?.toDoubleOrNull() ?: 0.0,
            수용인원수 = record["수용인원수"]?.toIntOrNull() ?: 0,
            입장료 = record["입장료"] ?: "",
            숙박가능여부 = record["숙박가능여부"] ?: "",
            주요시설명 = record["주요시설명"] ?: "",
            소재지도로명주소 = record["소재지도로명주소"] ?: "",
            관리기관명 = record["관리기관명"] ?: "",
            휴양림전화번호 = record["휴양림전화번호"],
            홈페이지주소 = record["홈페이지주소"],
            위도 = record["위도"]?.toDoubleOrNull() ?: 0.0,
            경도 = record["경도"]?.toDoubleOrNull() ?: 0.0,
            이미지URL = record["이미지URL"]  // 이미지 URL 추가
        )
    }
}

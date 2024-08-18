package com.example.san_lim.screens.map

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.san_lim.ui.theme.ColorPalette
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.InputStreamReader

@Composable
fun HueyanglimCard(record: HueyanglimRecord, navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorPalette.earthyLightMoss)
                .padding(start = 16.dp, end = 8.dp)
        ) {
            // 휴양림명과 거리 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    modifier = Modifier.weight(1f)  // 텍스트가 가능한 많은 공간을 차지하도록 설정
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "간단히 보기" else "더보기",
                        tint = Color.Black // 아이콘 색상을 검은색으로 설정
                    )
                }
            }
            Text(text = "${"%.2f".format(record.distance / 1000)} km",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp) // km 텍스트 아래에 패딩 추가
            )

            // 추가 정보는 isExpanded가 true일 때만 표시
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "숙박 가능 여부: ${record.lodgingAvailable}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "주소: ${record.address}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "전화번호: ${record.phoneNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End // 버튼을 오른쪽 끝으로 정렬
                ) {
                    Button(onClick = { navController.navigate("register_visit_screen/${record.name}") },
                        colors = ButtonDefaults.buttonColors(Color(0xFF388E3C)) // 버튼 배경색을 진한 초록색으로 설정
                    ) {
                        Text(text = "방문기록 등록하기", color = Color.White) // 텍스트 색상을 흰색으로 설정
                    }
                }
            }
        }
    }
}

fun loadHueyanglimData(context: Context): List<HueyanglimRecord> {
    val inputStream = context.assets.open("hueyanglim_data.json")
    val reader = InputStreamReader(inputStream)
    val data = Gson().fromJson(reader, HueyanglimData::class.java)
    reader.close()
    return data.records
}

data class HueyanglimRecord(
    @SerializedName("휴양림명") val name: String,
    @SerializedName("숙박가능여부") val lodgingAvailable: String,
    @SerializedName("소재지도로명주소") val address: String,
    @SerializedName("휴양림전화번호") val phoneNumber: String,
    @SerializedName("위도") val latitude: Double,
    @SerializedName("경도") val longitude: Double,
    var distance: Float = 0f
)

data class HueyanglimData(
    @SerializedName("records") val records: List<HueyanglimRecord>
)

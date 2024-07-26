package com.example.san_lim.screens.map

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.InputStreamReader

@Composable
fun HueyanglimCard(record: HueyanglimRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "휴양림명: ${record.name}", style = MaterialTheme.typography.titleLarge)
            Text(text = "숙박 가능 여부: ${record.lodgingAvailable}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "주소: ${record.address}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "전화번호: ${record.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "거리: ${"%.2f".format(record.distance / 1000)} km", style = MaterialTheme.typography.bodyMedium)
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

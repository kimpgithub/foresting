package com.example.san_lim.screens.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

@Composable
fun InfoScreen(navController: NavController, recommendations: List<String>) {
    val context = LocalContext.current
    val lodges = loadForestLodgesFromJSON(context, "hueyanglim_data.json")
    val filteredLodges = lodges.filter { it.휴양림명 in recommendations }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(filteredLodges) { lodge ->
            LodgeCard(lodge)
        }
    }
}

@Composable
fun LodgeCard(lodge: ForestLodge) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${lodge.휴양림명}")
            Text(text = "City: ${lodge.시도명}")
            Text(text = "Type: ${lodge.휴양림구분}")
            Text(text = "Area: ${lodge.휴양림면적} m²")
            Text(text = "Capacity: ${lodge.수용인원수}")
            Text(text = "Entrance Fee: ${lodge.입장료}")
            Text(text = "Accommodation: ${lodge.숙박가능여부}")
            Text(text = "Main Facilities: ${lodge.주요시설명}")
            Text(text = "Address: ${lodge.소재지도로명주소}")
            Text(text = "Phone Number: ${lodge.휴양림전화번호 ?: "N/A"}")
        }
    }
}

// Utility functions

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
    val 경도: Double
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
            경도 = record["경도"]?.toDoubleOrNull() ?: 0.0
        )
    }
}


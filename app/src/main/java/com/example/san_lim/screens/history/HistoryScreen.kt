package com.example.san_lim.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import android.net.Uri
import android.util.Log
import com.example.san_lim.models.WeedData
import com.example.san_lim.utils.fetchDataFromFirebase
import com.example.san_lim.utils.uploadDataToFirebase

@Composable
fun HistoryScreen(navController: NavHostController) {
    val uploadSuccess = remember { mutableStateOf(false) }
    val uploadFailure = remember { mutableStateOf<Exception?>(null) }
    val weedDataList = remember { mutableStateListOf<WeedData>() }
    var fetchError by remember { mutableStateOf<Exception?>(null) }
    var dataFetched by remember { mutableStateOf(false) } // 데이터가 이미 로드되었는지 여부

    // 테스트를 위해 Uri를 고정된 값으로 사용. 실제로는 이미지 선택기 등을 통해 얻은 Uri를 사용
    val testImageUri = Uri.parse("android.resource://com.example.san_lim/drawable/m")

    // 데이터를 한 번만 가져오도록 설정
    LaunchedEffect(dataFetched) {
        if (!dataFetched) {
            fetchDataFromFirebase(
                onDataLoaded = { data ->
                    weedDataList.clear()
                    weedDataList.addAll(data)
                    dataFetched = true // 데이터가 로드되었음을 표시
                },
                onFailure = { exception ->
                    fetchError = exception
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                uploadDataToFirebase(
                    name = "산",
                    latitude = 37.5665,
                    longitude = 126.9780,
                    date = "2023-07-22",
                    weedType = "잡초 종류",
                    comment = "한줄 코멘트",
                    imageUri = testImageUri,
                    onSuccess = {
                        uploadSuccess.value = true
                        Log.d("HistoryScreen", "Upload successful")
                        dataFetched = false // 데이터를 다시 가져오도록 설정
                    },
                    onFailure = { exception ->
                        uploadFailure.value = exception
                        Log.e("HistoryScreen", "Upload failed", exception)
                    }
                )
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text("Upload Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(weedDataList) { weedData ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Name: ${weedData.name}")
                    Text(text = "Latitude: ${weedData.latitude}")
                    Text(text = "Longitude: ${weedData.longitude}")
                    Text(text = "Date: ${weedData.date}")
                    Text(text = "Weed Type: ${weedData.weedType}")
                    Text(text = "Comment: ${weedData.comment}")
                    Text(text = "Image URL: ${weedData.imageUrl}")
                }
            }
        }

        if (uploadSuccess.value) {
            Text("Upload Successful!")
        }
        uploadFailure.value?.let {
            Text("Upload Failed: ${it.message}")
        }
        fetchError?.let {
            Text("Fetch Failed: ${it.message}")
        }
    }
}

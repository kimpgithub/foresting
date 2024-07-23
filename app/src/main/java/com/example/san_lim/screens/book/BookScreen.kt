package com.example.san_lim.screens.book

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.san_lim.models.WeedData
import com.example.san_lim.viewmodels.BookViewModel

@Composable
fun BookScreen(navController: NavHostController) {
    val bookViewModel: BookViewModel = viewModel()
    val weedDataList by bookViewModel.weedDataList.collectAsState()
    val fetchError by bookViewModel.fetchError.collectAsState()
    val dataFetched by bookViewModel.dataFetched.collectAsState()
    var selectedWeedData by remember { mutableStateOf<WeedData?>(null) }

    // 뷰모델을 통해 데이터 로드를 처리
    LaunchedEffect(dataFetched) {
        if (!dataFetched) {
            bookViewModel.fetchWeedData()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // LazyVerticalGrid를 사용하여 데이터 리스트를 표시
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(weedDataList) { weedData ->
                ImageListItem(weedData) { selectedWeedData = it }
            }
        }

        // 오류 메시지를 표시
        fetchError?.let {
            Text("Fetch Failed: ${it.message}")
        }

        // 선택된 데이터의 상세 정보를 다이얼로그로 표시
        selectedWeedData?.let { weedData ->
            WeedDataDialog(weedData) { selectedWeedData = null }
        }
    }
}

@Composable
fun ImageListItem(weedData: WeedData, onClick: (WeedData) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(weedData) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Image(
                painter = rememberAsyncImagePainter(weedData.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(text = weedData.name, style = MaterialTheme.typography.h6)
        Text(text = "Weed Type: ${weedData.weedType}")
    }
}

@Composable
fun WeedDataDialog(weedData: WeedData, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(weedData.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Name: ${weedData.name}", style = MaterialTheme.typography.h6)
                Text(text = "Date: ${weedData.date}")
                Text(text = "Latitude: ${weedData.latitude}")
                Text(text = "Longitude: ${weedData.longitude}")
                Text(text = "Weed Type: ${weedData.weedType}")
                Text(text = "Comment: ${weedData.comment}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}

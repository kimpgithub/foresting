//package com.example.san_lim.screens.history
//
//import android.net.Uri
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import coil.compose.rememberAsyncImagePainter
//import com.example.san_lim.models.WeedData
//import com.example.san_lim.viewmodels.HistoryViewModel
//
//@Composable
//fun HistoryScreen(navController: NavHostController) {
//    val historyViewModel: HistoryViewModel = viewModel()
//    val weedDataList by historyViewModel.weedDataList.collectAsState()
//    val fetchError by historyViewModel.fetchError.collectAsState()
//    val uploadSuccess by historyViewModel.uploadSuccess.collectAsState()
//    val uploadFailure by historyViewModel.uploadFailure.collectAsState()
//    var selectedWeedData by remember { mutableStateOf<WeedData?>(null) }
//
//    val testImageUri = Uri.parse("android.resource://com.example.san_lim/drawable/m")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        UploadButton(
//            testImageUri = testImageUri,
//            onUpload = { historyViewModel.uploadWeedData(testImageUri) }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(4),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(weedDataList) { weedData ->
//                ImageListItem(weedData) { selectedWeedData = it }
//            }
//        }
//
//        if (uploadSuccess) {
//            Text("Upload Successful!")
//        }
//        uploadFailure?.let {
//            Text("Upload Failed: ${it.message}")
//        }
//        fetchError?.let {
//            Text("Fetch Failed: ${it.message}")
//        }
//
//        selectedWeedData?.let { weedData ->
//            WeedDataDialog(weedData) { selectedWeedData = null }
//        }
//    }
//}
//
//@Composable
//fun UploadButton(testImageUri: Uri, onUpload: () -> Unit) {
//    Button(
//        onClick = onUpload,
//        modifier = Modifier
//            .width(200.dp)
//            .height(50.dp)
//    ) {
//        Text("Upload Data")
//    }
//}
//
//@Composable
//fun ImageListItem(weedData: WeedData, onClick: (WeedData) -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { onClick(weedData) }
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f)
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter(weedData.imageUrl),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//        }
//        Text(text = weedData.name, style = MaterialTheme.typography.h6)
//        Text(text = "Weed Type: ${weedData.weedType}")
//    }
//}
//
//@Composable
//fun WeedDataDialog(weedData: WeedData, onDismiss: () -> Unit) {
//    Dialog(onDismissRequest = onDismiss) {
//        Surface(
//            shape = MaterialTheme.shapes.medium,
//            color = Color.White,
//            elevation = 8.dp
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Image(
//                    painter = rememberAsyncImagePainter(weedData.imageUrl),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .height(300.dp)
//                        .fillMaxWidth(),
//                    contentScale = ContentScale.Crop
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = "Name: ${weedData.name}", style = MaterialTheme.typography.h6)
//                Text(text = "Date: ${weedData.date}")
//                Text(text = "Latitude: ${weedData.latitude}")
//                Text(text = "Longitude: ${weedData.longitude}")
//                Text(text = "Weed Type: ${weedData.weedType}")
//                Text(text = "Comment: ${weedData.comment}")
//                Spacer(modifier = Modifier.height(16.dp))
//                Button(onClick = onDismiss) {
//                    Text("Close")
//                }
//            }
//        }
//    }
//}

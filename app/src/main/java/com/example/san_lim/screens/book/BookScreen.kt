//package com.example.san_lim.screens.book
//
//import android.content.Context
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.rememberAsyncImagePainter
//import com.example.san_lim.R
//import com.example.san_lim.models.WeedData
//import com.example.san_lim.utils.loadPlantsFromJson
//import com.example.san_lim.viewmodels.BookViewModel
//import androidx.navigation.NavHostController
//
//@Composable
//fun BookScreen(navController: NavHostController, context: Context) {
//    val plantViewModel: BookViewModel = viewModel()
//    val plantDataList by plantViewModel.weedDataList.collectAsState()
//    val fetchError by plantViewModel.fetchError.collectAsState()
//    val dataFetched by plantViewModel.dataFetched.collectAsState()
//
//    val localPlantList = remember { loadPlantsFromJson(context) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (!dataFetched) {
//            Text("Loading...")
//        } else {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items(localPlantList) { plant ->
//                    val plantData = plantDataList.find { it.name == plant.name }
//                    PlantItem(plant.name, plantData)
//                }
//            }
//        }
//
//        fetchError?.let {
//            Text("Fetch Failed: ${it.message}")
//        }
//    }
//}
//
//@Composable
//fun PlantItem(plantName: String, plantData: WeedData?) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { /* TODO: 클릭 시 행동 정의 */ }
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f)
//        ) {
//            if (plantData?.imageUrl != null) {
//                Image(
//                    painter = rememberAsyncImagePainter(plantData.imageUrl),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_launcher_background),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//        Text(text = plantName, style = MaterialTheme.typography.h6)
//    }
//}

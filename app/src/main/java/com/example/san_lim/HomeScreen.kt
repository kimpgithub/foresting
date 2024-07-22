package com.example.san_lim

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { navController.navigate("weed_detection") }) {
            Text("Weed Detection")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("data_visualization") }) {
            Text("Data Visualization")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("history") }) {
            Text("History")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("map_screen") }) {
            Text("Map Screen")
        }
    }
}

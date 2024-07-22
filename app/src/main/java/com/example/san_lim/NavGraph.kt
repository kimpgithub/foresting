package com.example.san_lim

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") { HomeScreen(navController) }
        composable("weed_detection") { WeedDetectionScreen(navController) }
        composable("data_visualization") { DataVisualizationScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("map_screen") { MapScreen(navController) }
    }
}
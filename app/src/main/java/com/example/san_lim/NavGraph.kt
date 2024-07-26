package com.example.san_lim

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.san_lim.screens.home.HomeScreen
import com.example.san_lim.screens.home.SelectScreen
import com.example.san_lim.screens.info.InfoScreen
import com.example.san_lim.screens.map.MapScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("San Lim") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(painterResource(id = R.drawable.ic_menu), contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("select_screen") { SelectScreen(navController) }
            composable(
                "info_screen/{recommendations}",
                arguments = listOf(navArgument("recommendations") { defaultValue = "" })
            ) { backStackEntry ->
                val recommendationsString = backStackEntry.arguments?.getString("recommendations")
                val recommendations = recommendationsString?.split(",")?.map { it.trim() } ?: emptyList()
                InfoScreen(navController, recommendations)
            }
            composable("map_screen") { MapScreen(navController) }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    BottomAppBar {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home")
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("_screen") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_map),
                contentDescription = "Map Screen"
            )
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("map_screen") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_map),
                contentDescription = "Map Screen"
            )
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("info_screen") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = "Info Screen"
            )
        }
    }
}

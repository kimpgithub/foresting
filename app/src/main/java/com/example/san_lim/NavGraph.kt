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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.san_lim.screens.home.ForestInfoScreen
import com.example.san_lim.screens.home.HomeScreen
import com.example.san_lim.screens.home.SelectScreen
import com.example.san_lim.screens.info.InfoScreen
import com.example.san_lim.screens.login.LoginScreen
import com.example.san_lim.screens.map.MapScreen
import com.example.san_lim.screens.map.RegisterVisitScreen
import com.example.san_lim.screens.profile.ProfileScreen
import com.example.san_lim.screens.trekking.TrekkingScreen
import com.example.san_lim.ui.theme.ColorPalette

@Composable
fun NavGraph(startDestination: String = "login") {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val showTopBarAndBottomBar = remember { mutableStateOf(false) }

    // Observe the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Update the visibility of top and bottom bars based on the current route
    LaunchedEffect(navBackStackEntry) {
        showTopBarAndBottomBar.value = navBackStackEntry?.destination?.route != "login"
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (showTopBarAndBottomBar.value) {
                TopAppBar(
                    title = { Text("Foresting") },
                    backgroundColor = ColorPalette.primaryGreen, // Top Bar Color
                    navigationIcon = if (navBackStackEntry?.destination?.route !in listOf("login", "home")) {
                        {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "Back"
                                )
                            }
                        }
                    } else null
                )
            }
        },
        bottomBar = {
            if (showTopBarAndBottomBar.value) {
                BottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("home") { HomeScreen(navController) }
            composable("select_screen") { SelectScreen(navController) }
            composable(
                "info_screen/{recommendations}",
                arguments = listOf(navArgument("recommendations") { defaultValue = "" })
            ) { backStackEntry ->
                val recommendationsString = backStackEntry.arguments?.getString("recommendations")
                val recommendations =
                    recommendationsString?.split(",")?.map { it.trim() } ?: emptyList()
                InfoScreen(navController, recommendations)
            }
            composable("map_screen") { MapScreen(navController) }
            composable("profile_screen") { ProfileScreen(navController) }
            composable(
                "register_visit_screen/{lodgeName}",
                arguments = listOf(navArgument("lodgeName") { defaultValue = "" })
            ) { backStackEntry ->
                val lodgeName = backStackEntry.arguments?.getString("lodgeName") ?: ""
                RegisterVisitScreen(navController, lodgeName)
            }

            composable("trekking_screen") { TrekkingScreen(navController) } //
            composable("forest_info_screen") { ForestInfoScreen(navController) } // ForestInfoScreen 경로 추가
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    BottomAppBar(
        backgroundColor = ColorPalette.primaryGreen // Bottom Bar Color
    ) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = "Home",
                tint = ColorPalette.lightGreen // ic_home color
            )
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("history") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "History",
                tint = ColorPalette.lightGreen // ic_history color
            )
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("map_screen") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_map),
                contentDescription = "Map Screen",
                tint = ColorPalette.lightGreen // ic_map color
            )
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("profile_screen") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Profile Screen",
                tint = ColorPalette.lightGreen // ic_profile color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavGraphPreview() { NavGraph(startDestination = "home") }
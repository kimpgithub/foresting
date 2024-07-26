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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.san_lim.screens.home.HomeScreen
import com.example.san_lim.screens.home.SelectScreen
import com.example.san_lim.screens.info.InfoScreen
import com.example.san_lim.screens.login.LoginScreen
import com.example.san_lim.screens.map.MapScreen
import com.example.san_lim.screens.profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(startDestination: String = "login") {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
                    title = { Text("San Lim") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_menu),
                                contentDescription = "Menu"
                            )
                        }
                    }
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
            composable("profile_screen") { ProfileScreen(navController) } // 추가된 부분
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
        IconButton(onClick = { navController.navigate("history") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "History"
            )
        }
        Spacer(modifier = Modifier.weight(1f, true))
        IconButton(onClick = { navController.navigate("book_screen") }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_photo_library_24),
                contentDescription = "Book Screen"
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
            IconButton(onClick = { navController.navigate("profile_screen") }) { // 변경된 부분
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile Screen"
                )
            }
        }
    }
}

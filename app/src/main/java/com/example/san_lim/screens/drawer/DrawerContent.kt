package com.example.san_lim.screens.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavHostController, scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Navigation Drawer",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Divider()
        DrawerItem(navController, scaffoldState, scope, "Home", "home")
        DrawerItem(navController, scaffoldState, scope, "Data Visualization", "data_visualization")
        DrawerItem(navController, scaffoldState, scope, "History", "history")
        DrawerItem(navController, scaffoldState, scope, "Map Screen", "map_screen")
        DrawerItem(navController, scaffoldState, scope, "Info Screen", "info_screen")
    }
}

@Composable
fun DrawerItem(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    title: String,
    route: String
) {
    TextButton(onClick = {
        scope.launch {
            scaffoldState.drawerState.close()
        }
        navController.navigate(route)
    }) {
        Text(title, modifier = Modifier.fillMaxWidth().padding(16.dp))
    }
}

package com.example.san_lim.screens.map

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val mapViewModel: MapViewModel = viewModel()
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    DisposableEffect(context) {
        Log.d("MapScreen", "DisposableEffect started")
        mapView.onCreate(Bundle())
        mapView.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5665, 126.9780), 10f))
            mapViewModel.setMap(googleMap)
        }
        mapView.onStart()
        mapView.onResume()

        onDispose {
            Log.d("MapScreen", "DisposableEffect disposed")
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
            mapView.onLowMemory()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Forest Management Map") }
        )
        AndroidView(factory = { mapView })
    }
}

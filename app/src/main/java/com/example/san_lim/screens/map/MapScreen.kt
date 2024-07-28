package com.example.san_lim.screens.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val mapViewModel: MapViewModel = viewModel()
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var hueyanglimRecords by remember { mutableStateOf(loadHueyanglimData(context)) }

    // 권한 요청
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            mapViewModel.initializeLocationClient(context)
            mapViewModel.updateLocationToCurrent(context)
        } else {
            Log.e("MapScreen", "Location permission denied")
        }
    }

    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        } else {
            mapViewModel.initializeLocationClient(context)
            mapViewModel.updateLocationToCurrent(context)
        }
    }

    DisposableEffect(context) {
        Log.d("MapScreen", "DisposableEffect started")
        mapView.onCreate(Bundle())
        mapView.getMapAsync { googleMap ->
            mapViewModel.setMap(googleMap, context)
            mapViewModel.updateLocationToCurrent(context)
            mapViewModel.addMarkers(hueyanglimRecords)
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

    LaunchedEffect(mapViewModel.currentLocation) {
        if (mapViewModel.currentLocation != null) {
            hueyanglimRecords = hueyanglimRecords.map { record ->
                val distance = calculateDistance(mapViewModel.currentLocation!!, LatLng(record.latitude, record.longitude))
                record.copy(distance = distance)
            }.sortedBy { it.distance }
                .take(10)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hueyanglimRecords) { record ->
                HueyanglimCard(record, navController)
                }
        }
    }
}

fun calculateDistance(from: LatLng, to: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results)
    return results[0]
}

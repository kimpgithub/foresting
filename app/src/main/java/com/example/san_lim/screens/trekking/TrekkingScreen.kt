package com.example.san_lim.screens.trekking

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@Composable
fun TrekkingScreen(navController: NavController) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()

    AndroidView({ mapView }, modifier = Modifier.fillMaxSize()) { mapView ->
        mapView.getMapAsync { googleMap ->
            CoroutineScope(Dispatchers.IO).launch {
                val coordinates = readJsonFiles(context)
                withContext(Dispatchers.Main) {
                    drawPolylines(googleMap, coordinates)
                }
            }

            // Set initial camera position to South Korea
            val southKoreaLatLng = LatLng(36.0, 127.5)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(southKoreaLatLng, 7f))
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    DisposableEffect(mapView) {
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.onResume()

        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    return mapView
}

private fun readJsonFiles(context: Context): List<Pair<List<LatLng>, Double>> {
    val coordinates = mutableListOf<Pair<List<LatLng>, Double>>()
    val rootDir = "top100mt_json"
    val directories = context.assets.list(rootDir) ?: emptyArray()

    for (dir in directories) {
        val dirPath = "$rootDir/$dir"
        val jsonFiles = context.assets.list(dirPath) ?: emptyArray()

        for (fileName in jsonFiles) {
            val jsonString = context.assets.open("$dirPath/$fileName").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val latLngList = mutableListOf<LatLng>()
            val jsonArray = jsonObject.getJSONArray("coordinates")
            for (i in 0 until jsonArray.length()) {
                val coordinate = jsonArray.getJSONObject(i)
                val lat = coordinate.getDouble("latitude")
                val lng = coordinate.getDouble("longitude")
                latLngList.add(LatLng(lat, lng))
            }
            val totalDistance = jsonObject.getDouble("total_distance")
            coordinates.add(latLngList to totalDistance)
        }
    }

    return coordinates
}

private fun drawPolylines(googleMap: GoogleMap, coordinates: List<Pair<List<LatLng>, Double>>) {
    for ((latLngList, totalDistance) in coordinates) {
        val polyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(latLngList)
                .color(0xFF008000.toInt()) // Set polyline color to #008000
        )
        googleMap.setOnPolylineClickListener {
            if (it == polyline) {
                // Show totalDistance in a popup
                showDistancePopup(totalDistance)
            }
        }
    }
    if (coordinates.isNotEmpty()) {
        val firstLatLng = coordinates.first().first.first()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10f))
    }
}

private fun showDistancePopup(totalDistance: Double) {
    // Implement the logic to show a popup with the totalDistance value
}

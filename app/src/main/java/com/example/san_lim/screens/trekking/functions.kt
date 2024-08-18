package com.example.san_lim.screens.trekking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

data class MountainRoute(val name: String, val routes: List<Route>)
data class Route(val latLngList: List<LatLng>, val totalDistance: Double)

fun readJsonFiles(context: Context, userLocation: LatLng): List<MountainRoute> {
    val rootDir = "top100mt_json"
    val directories = context.assets.list(rootDir) ?: emptyArray()
    val mountains = mutableListOf<MountainRoute>()

    for (dir in directories) {
        val dirPath = "$rootDir/$dir"
        val jsonFiles = context.assets.list(dirPath) ?: emptyArray()
        val routes = mutableListOf<Route>()

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
            routes.add(Route(latLngList, totalDistance))
        }

        val mountainLatLng = routes.firstOrNull()?.latLngList?.firstOrNull()
        if (mountainLatLng != null) {
            mountains.add(MountainRoute(dir, routes))
        }
    }

    return mountains.sortedBy { calculateDistance(userLocation, it.routes.first().latLngList.first()) }
}

private fun calculateDistance(start: LatLng, end: LatLng): Double {
    val theta = start.longitude - end.longitude
    var dist = sin(deg2rad(start.latitude)) * sin(deg2rad(end.latitude)) + cos(deg2rad(start.latitude)) * cos(deg2rad(end.latitude)) * cos(deg2rad(theta))
    dist = acos(dist)
    dist = rad2deg(dist)
    return dist * 60 * 1.1515 * 1.609344 // Distance in kilometers
}

private fun deg2rad(deg: Double): Double = deg * Math.PI / 180.0
private fun rad2deg(rad: Double): Double = rad * 180.0 / Math.PI

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (LatLng) -> Unit) {
    ActivityCompat.requestPermissions(
        context as androidx.activity.ComponentActivity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
        1
    )
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            location?.let {
                onLocationReceived(LatLng(it.latitude, it.longitude))
            } ?: Log.d("TrekkingScreen", "Location is null.")
        }
        .addOnFailureListener { exception ->
            Log.d("TrekkingScreen", "Failed to get location: ${exception.message}")
        }
}


fun drawPolylines(googleMap: GoogleMap, routes: List<Route>) {
    for (route in routes) {
        val polyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(route.latLngList)
                .color(0xFF008000.toInt()) // Set polyline color to #008000
        )
        googleMap.setOnPolylineClickListener {
            if (it == polyline) {
                showDistancePopup()
            }
        }
    }
}

private fun showDistancePopup() {
    // Implement the logic to show a popup with the totalDistance value
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
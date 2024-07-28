package com.example.san_lim.screens.trekking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrekkingScreen(navController: NavController) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    val trekkingRoutes = remember { mutableStateOf<List<MountainRoute>>(emptyList()) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val currentLocation = remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        getCurrentLocation(context, fusedLocationClient) { location ->
            currentLocation.value = location
            trekkingRoutes.value = readJsonFiles(context, location)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trekking Routes") })
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()) {
                    AndroidView({ mapView }, modifier = Modifier.fillMaxSize()) { mapView ->
                        mapView.getMapAsync { googleMap ->
                            googleMap.uiSettings.isZoomControlsEnabled = true
                            currentLocation.value?.let { location ->
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
                                trekkingRoutes.value.forEach { mountainRoute ->
                                    drawPolylines(googleMap, mountainRoute.routes)
                                }
                            } ?: run {
                                // Set initial camera position to South Korea
                                val southKoreaLatLng = LatLng(36.0, 127.5)
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(southKoreaLatLng, 7f))
                            }
                        }
                    }
                }
                TrekkingRouteList(
                    mountains = trekkingRoutes.value,
                    onRouteSelected = { selectedRoute ->
                        mapView.getMapAsync { googleMap ->
                            drawPolylines(googleMap, listOf(selectedRoute))
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedRoute.latLngList.first(), 10f))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    )
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (LatLng) -> Unit) {
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

data class MountainRoute(val name: String, val routes: List<Route>)
data class Route(val latLngList: List<LatLng>, val totalDistance: Double)

private fun readJsonFiles(context: Context, userLocation: LatLng): List<MountainRoute> {
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

private fun drawPolylines(googleMap: GoogleMap, routes: List<Route>) {
    for (route in routes) {
        val polyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(route.latLngList)
                .color(0xFF008000.toInt()) // Set polyline color to #008000
        )
        googleMap.setOnPolylineClickListener {
            if (it == polyline) {
                showDistancePopup(route.totalDistance)
            }
        }
    }
}

private fun showDistancePopup(totalDistance: Double) {
    // Implement the logic to show a popup with the totalDistance value
}

@Composable
fun TrekkingRouteList(
    mountains: List<MountainRoute>,
    onRouteSelected: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        mountains.forEach { mountain ->
            item {
                Text(
                    text = mountain.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(mountain.routes) { route ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onRouteSelected(route) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Distance: %.2f km".format(route.totalDistance / 1000))
                        // Add more details if needed
                    }
                }
            }
        }
    }
}

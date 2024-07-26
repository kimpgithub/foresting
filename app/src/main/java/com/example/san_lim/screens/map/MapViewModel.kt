package com.example.san_lim.screens.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel : ViewModel() {
    var savedInstanceState: Bundle? by mutableStateOf(null)
    private var googleMap: GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var currentLocation: LatLng? by mutableStateOf(null)

    fun setMap(map: GoogleMap, context: Context) {
        googleMap = map
        googleMap?.let {
            // Enable the My Location layer on the map
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            it.isMyLocationEnabled = true
        }
    }

    fun initializeLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun updateLocationToCurrent(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 10f))
                Log.d("MapViewModel", "Current location: $currentLocation")
            } else {
                Log.e("MapViewModel", "Failed to get current location")
            }
        }
    }

    fun addMarkers(hueyanglimRecords: List<HueyanglimRecord>) {
        googleMap?.let { map ->
            hueyanglimRecords.forEach { record ->
                val position = LatLng(record.latitude, record.longitude)
                val markerOptions = MarkerOptions().position(position).title(record.name)
                map.addMarker(markerOptions)
            }
        }
    }
}


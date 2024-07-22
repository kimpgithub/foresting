package com.example.san_lim.screens.map

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {
    var savedInstanceState: Bundle? by mutableStateOf(null)
    private var googleMap: GoogleMap? = null

    fun setMap(map: GoogleMap) {
        googleMap = map
    }

    fun onWeedTypeClick(weedType: String) {
        when (weedType) {
            "북한산" -> moveCameraToBukSan()
            "도봉산" -> moveCameraToDobong()
        }
    }

    private fun moveCameraToBukSan() {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.715235242491296, 126.9841557896699), 15f))
    }

    private fun moveCameraToDobong() {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.6898, 127.0443), 15f))
    }
}

package com.example.san_lim.models

data class WeedData(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val date: String = "",
    val weedType: String = "",
    val comment: String = "",
    val imageUrl: String = ""
)

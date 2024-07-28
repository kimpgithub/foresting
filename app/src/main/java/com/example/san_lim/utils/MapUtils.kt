package com.example.san_lim.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.san_lim.screens.map.Location

fun getLocationFromImageUri(context: Context, imageUri: Uri): Location? {
    val cursor = context.contentResolver.query(
        imageUri,
        arrayOf(MediaStore.Images.ImageColumns.LATITUDE, MediaStore.Images.ImageColumns.LONGITUDE),
        null, null, null
    )
    cursor?.use {
        if (it.moveToFirst()) {
            val latitude = it.getDouble(it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LATITUDE))
            val longitude = it.getDouble(it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LONGITUDE))
            return Location(latitude, longitude)
        }
    }
    return null
}

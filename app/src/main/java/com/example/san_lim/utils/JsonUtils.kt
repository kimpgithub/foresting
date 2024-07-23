package com.example.san_lim.utils

import android.content.Context
import com.example.san_lim.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.san_lim.models.WeedData
import java.io.InputStreamReader

fun loadPlantsFromJson(context: Context): List<WeedData> {
    val inputStream = context.resources.openRawResource(R.raw.plants)
    val reader = InputStreamReader(inputStream)
    val plantType = object : TypeToken<Map<String, List<String>>>() {}.type
    val plantMap: Map<String, List<String>> = Gson().fromJson(reader, plantType)
    return plantMap["plants"]?.map { WeedData(name = it) } ?: emptyList()
}

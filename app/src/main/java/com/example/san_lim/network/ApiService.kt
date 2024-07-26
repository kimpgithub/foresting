package com.example.san_lim.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RecommendationRequest(
    val user_id: String,
    val region: String,
    val activities: List<String>,
    val facilities: List<String>
)

interface ApiService {
    @POST("/recommend")
    fun getRecommendations(@Body request: RecommendationRequest): Call<List<String>>
}

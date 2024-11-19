package com.example.tgsroom.network

import com.example.tgsroom.model.ApiData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("chart")
    fun getChartData(): Call<ApiData>
}
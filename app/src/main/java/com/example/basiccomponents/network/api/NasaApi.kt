package com.example.basiccomponents.network.api

import com.example.basiccomponents.network.models.RemoteNasaDailyImage
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {

    @GET("/planetary/apod")
    suspend fun getDaily(
        @Query("date") date: String?,
        @Query("api_key") apiKey: String
    ): RemoteNasaDailyImage
}
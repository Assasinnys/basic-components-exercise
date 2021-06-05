package com.example.basiccomponents.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteNasaDailyImage(
    @field:Json(name = "copyright")
    val copyright: String?,

    @field:Json(name = "date")
    val date: String,

    @field:Json(name = "explanation")
    val explanation: String?,

    @field:Json(name = "hdurl")
    val hdImageUrl: String,

    @field:Json(name = "media_type")
    val mediaType: String,

    @field:Json(name = "service_version")
    val serviceVersion: String,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "url")
    val imageUrl: String
)
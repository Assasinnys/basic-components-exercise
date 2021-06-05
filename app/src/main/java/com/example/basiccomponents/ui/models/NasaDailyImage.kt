package com.example.basiccomponents.ui.models

data class NasaDailyImage(
    val copyright: String?,
    val date: String,
    val explanation: String?,
    val hdImageUrl: String,
    val mediaType: String,
    val title: String,
    val imageUrl: String
)
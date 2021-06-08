package com.example.basiccomponents.network.mapping

import com.example.basiccomponents.network.models.RemoteNasaDailyImage
import com.example.basiccomponents.ui.models.NasaDailyImage

fun RemoteNasaDailyImage.toImageOfDay() = NasaDailyImage(
    copyright = copyright,
    date = date,
    explanation = explanation,
    hdImageUrl = hdImageUrl,
    mediaType = mediaType,
    title = title,
    imageUrl = imageUrl
)
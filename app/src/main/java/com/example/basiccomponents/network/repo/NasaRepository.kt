package com.example.basiccomponents.network.repo

import com.example.basiccomponents.network.api.NasaApi
import com.example.basiccomponents.network.mapping.toImageOfDay
import com.example.basiccomponents.ui.models.NasaDailyImage
import java.text.SimpleDateFormat
import java.util.*

class NasaRepository(private val nasaApi: NasaApi) {

    suspend fun getDailyImage(
        date: Date? = null,
        //apiKey: String = "DEMO_KEY"
        apiKey: String = "SOMCMuCbyw1OdDcqAs5YxafGJ40DOExrRDuzMQWy"
    ): NasaDailyImage {
        val stringDate =  with(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) {
            date?.let { format(date) }
        }

        return nasaApi.getDaily(stringDate, apiKey).toImageOfDay()
    }
}
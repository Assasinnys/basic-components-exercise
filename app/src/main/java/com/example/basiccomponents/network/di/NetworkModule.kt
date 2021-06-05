package com.example.basiccomponents.network.di

import com.example.basiccomponents.network.api.NasaApi
import com.example.basiccomponents.network.repo.NasaRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_NASA_URL = "https://api.nasa.gov/"

val networkModule = module {
    single {
        val client = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_NASA_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    single { get<Retrofit>().create(NasaApi::class.java) }

    single { NasaRepository(get()) }
}
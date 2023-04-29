package com.umsl.wdhq58.weatherfinal.utility.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val client = OkHttpClient.Builder().build()

    private val retroFit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}
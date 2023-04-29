package com.umsl.wdhq58.weatherfinal.utility.api

import com.umsl.wdhq58.weatherfinal.utility.api.data.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double,
                   @Query("units") units: String = "imperial", @Query("appid") key: String): Call<WeatherData>
}
package com.umsl.wdhq58.weatherfinal.utility.api.data

data class WeatherData(
    val coord: Coordinates,
    val weather: List<Weather>,
    val base: String,
    val main: WeatherDetails,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val sys: SysData,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int

)


data class Coordinates (
    val lat: Double,
    val lon: Double
)

data class Weather(
    val id: String,
    val main: String,
    val description: String,
    val icon: String
)

data class WeatherDetails(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Rain(
    val oneHour: Double
)

data class Clouds(
    val all: Int
)

data class SysData(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

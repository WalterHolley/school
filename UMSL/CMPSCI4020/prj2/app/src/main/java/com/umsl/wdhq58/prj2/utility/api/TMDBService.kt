package com.umsl.wdhq58.prj2.utility.api

import com.umsl.wdhq58.prj2.utility.api.data.PopularMovies
import com.umsl.wdhq58.prj2.utility.api.data.PopularPersons
import com.umsl.wdhq58.prj2.utility.api.data.UpcomingMovies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") key: String): Call<PopularMovies>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") key: String): Call<UpcomingMovies>

    @GET("person/popular")
    fun getPopularPeople(@Query("api_key") key: String): Call<PopularPersons>
}
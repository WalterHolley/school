package com.umsl.wdhq58.prj2.utility.api.data

data class PopularMovies(
    val results: List<PopularMovie>
)

data class PopularMovie(
    val id: Int,
    val overview: String,
    val poster_path: String,
    val releaseDate: String,
    val title: String,
    val vote_average: Double,
    val voteCount: Int
)

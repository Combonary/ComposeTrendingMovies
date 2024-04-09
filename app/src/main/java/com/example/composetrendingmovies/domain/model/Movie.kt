package com.example.composetrendingmovies.domain.model

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val vote_count: Int,
    val original_language: String,
    val original_title: String,
    val id: Int,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val poster_path: String,
    val overview: String,
    val release_date: String,
    val popularity: Double,
    val media_type: String
)

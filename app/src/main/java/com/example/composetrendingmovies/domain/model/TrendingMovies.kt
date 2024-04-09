package com.example.composetrendingmovies.domain.model


data class TrendingMovies(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

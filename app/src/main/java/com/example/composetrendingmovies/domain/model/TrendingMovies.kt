package com.example.composetrendingmovies.domain.model

import com.google.gson.annotations.SerializedName


data class TrendingMovies(
    val page: Int,
    @SerializedName("results")
    val moviesList: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

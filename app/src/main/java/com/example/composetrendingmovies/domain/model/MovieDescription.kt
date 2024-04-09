package com.example.composetrendingmovies.domain.model

import com.google.gson.annotations.SerializedName

data class MovieDescription(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val genres: List<Genres>
)

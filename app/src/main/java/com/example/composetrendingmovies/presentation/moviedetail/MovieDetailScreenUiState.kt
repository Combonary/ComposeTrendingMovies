package com.example.composetrendingmovies.presentation.moviedetail

import io.github.kotlin.imdb.model.MovieDescription

data class MovieDetailScreenUiState(
    val isLoading: Boolean = false,
    val movie: MovieDescription? = null,
    val error: String? = null
)
package com.example.composetrendingmovies.presentation.moviedetail

import io.github.kotlin.imdb.model.MovieEntity

data class MovieDetailScreenUiState(
    val isLoading: Boolean = false,
    val movie: MovieEntity? = null,
    val error: String? = null
)
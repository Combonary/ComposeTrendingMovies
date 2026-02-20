package com.example.composetrendingmovies.presentation.moviedetail

import com.example.composetrendingmovies.domain.model.MovieDescription

data class MovieDetailScreenUiState(
    val isLoading: Boolean = false,
    val movie: MovieDescription? = null,
    val error: String? = null
)
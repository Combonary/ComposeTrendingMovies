package com.example.composetrendingmovies.presentation.moviedetail

import io.github.kotlin.imdb.model.MovieEntity

sealed interface MovieDetailScreenUiState {
    data object Loading : MovieDetailScreenUiState
    data class Success(val movie: MovieEntity) : MovieDetailScreenUiState
    data class Error(val message: String) : MovieDetailScreenUiState
}

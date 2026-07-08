package com.example.composetrendingmovies.presentation.movieslist

import androidx.compose.runtime.Immutable

@Immutable
data class MovieUiModel(
    val id: Int,
    val title: String,
    val summary: String,
    val posterPath: String?
)

@Immutable
data class MovieListScreenUIState(
    val isLoading: Boolean = false,
    val movies: List<MovieUiModel> = emptyList(),
    val error: String? = null
)

package com.example.composetrendingmovies.presentation.movieslist

import com.example.composetrendingmovies.domain.model.Movie

data class MovieListScreenUIState(
    val isLoading: Boolean = false,
    val movies: List<Movie>? = null,
    val error: String? = null
)

package com.example.composetrendingmovies.presentation.movieslist

import io.github.kotlin.imdb.model.MovieEntity

data class MovieListScreenUIState(
    val isLoading: Boolean = false,
    val movies: List<MovieEntity>? = null,
    val error: String? = null
)

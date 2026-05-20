package com.example.composetrendingmovies.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object MovieListRoute

@Serializable
data class MovieDetailRoute(val movie_id: Int)

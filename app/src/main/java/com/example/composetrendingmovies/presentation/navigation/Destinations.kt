package com.example.composetrendingmovies.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object MovieListRoute : NavKey

@Serializable
data class MovieDetailRoute(val movieId: Int) : NavKey

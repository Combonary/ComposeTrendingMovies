package com.example.composetrendingmovies.domain.repository

import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.model.TrendingMovies
import kotlinx.coroutines.flow.Flow

interface TrendingMoviesRepository {
    suspend fun getTrendingMovies() : Flow<ServerResult<TrendingMovies>?>

    suspend fun getMovie(id: Int) : Flow<ServerResult<MovieDescription>>
}
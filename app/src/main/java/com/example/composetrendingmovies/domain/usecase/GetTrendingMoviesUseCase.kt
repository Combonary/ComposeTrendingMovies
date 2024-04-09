package com.example.composetrendingmovies.domain.usecase

import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.model.TrendingMovies
import com.example.composetrendingmovies.domain.repository.TrendingMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(private val repository: TrendingMoviesRepository) {
    suspend operator fun invoke(): Flow<ServerResult<TrendingMovies>?> {
        return repository.getTrendingMovies()
    }
}
package com.example.composetrendingmovies.domain.usecase

import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.repository.TrendingMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val repository: TrendingMoviesRepository) {
    suspend operator fun invoke(id: Int): Flow<ServerResult<MovieDescription>> {
        return repository.getMovie(id)
    }
}
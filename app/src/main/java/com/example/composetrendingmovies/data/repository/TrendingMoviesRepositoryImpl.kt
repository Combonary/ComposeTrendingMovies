package com.example.composetrendingmovies.data.repository

import com.example.composetrendingmovies.data.data_source.remote.TrendingMoviesApi
import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.model.TrendingMovies
import com.example.composetrendingmovies.domain.repository.TrendingMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrendingMoviesRepositoryImpl @Inject constructor(private val api: TrendingMoviesApi) : TrendingMoviesRepository {
    override suspend fun getTrendingMovies(): Flow<ServerResult<TrendingMovies>?> {
        return flow {
            emit(ServerResult.loading())
            val result = api.getTrendingMoviesList()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMovie(id: Int): Flow<ServerResult<MovieDescription>> {
        return flow {
            emit(ServerResult.loading())
            emit(api.getMovie(id))
        }.flowOn(Dispatchers.IO)
    }
}
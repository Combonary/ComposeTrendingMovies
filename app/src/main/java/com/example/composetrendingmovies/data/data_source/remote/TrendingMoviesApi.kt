package com.example.composetrendingmovies.data.data_source.remote

import android.util.Log
import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.model.TrendingMovies
import com.example.composetrendingmovies.utils.ErrorUtil
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class TrendingMoviesApi @Inject constructor(
    private val retrofit: Retrofit,
    private val retrofitService: RetrofitService
) {

    private suspend fun <T> getResponse(request: suspend() -> Response<T>, errorMessage: String): ServerResult<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful){
                return ServerResult.success(result.body())
            } else {
                val errorResponse = ErrorUtil.parseError(result, retrofit)
                ServerResult.error(errorResponse?.errorMessage ?: errorMessage, errorResponse)
            }
        } catch (e: Throwable){
            println(Log.d("API", e.message?: "none"))
            ServerResult.error("Unknown Error", null)
        }
    }

    suspend fun getTrendingMoviesList(): ServerResult<TrendingMovies> {
        return getResponse(
            request = {retrofitService.getTrendingMovies()},
            errorMessage = "Error fetching trending movies"
        )
    }

    suspend fun getMovie(id: Int): ServerResult<MovieDescription> {
        return getResponse(
            request = {retrofitService.getMovie(id)},
            errorMessage = "Error fetching Movie Description"
        )
    }
}
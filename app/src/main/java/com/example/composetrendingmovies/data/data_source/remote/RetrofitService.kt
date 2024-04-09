package com.example.composetrendingmovies.data.data_source.remote

import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.TrendingMovies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitService {

    @Headers("Accept: application/json")
    @GET("/3/trending/movie/week")
    suspend fun getTrendingMovies(): Response<TrendingMovies>

    @Headers("Accept: application/json")
    @GET("/3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int) : Response<MovieDescription>
}
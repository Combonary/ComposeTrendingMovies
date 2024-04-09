package com.example.composetrendingmovies.di

import com.example.composetrendingmovies.data.data_source.remote.AuthInterceptor
import com.example.composetrendingmovies.data.data_source.remote.RetrofitService
import com.example.composetrendingmovies.data.data_source.remote.TrendingMoviesApi
import com.example.composetrendingmovies.data.repository.TrendingMoviesRepositoryImpl
import com.example.composetrendingmovies.domain.repository.TrendingMoviesRepository
import com.example.composetrendingmovies.domain.usecase.GetMovieUseCase
import com.example.composetrendingmovies.domain.usecase.GetTrendingMoviesUseCase
import com.example.composetrendingmovies.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Remote {
    private const val BASE_URL = Constants.BASE_URL

    @Provides
    fun injectInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    fun injectOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(Constants.API_KEY_V3))
            .build()
    }

    @Provides
    fun injectRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(api: TrendingMoviesApi): TrendingMoviesRepository {
        return TrendingMoviesRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetTrendingMoviesUseCase(repository: TrendingMoviesRepository): GetTrendingMoviesUseCase {
        return GetTrendingMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(repository: TrendingMoviesRepository): GetMovieUseCase {
        return GetMovieUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService = retrofit.create(RetrofitService::class.java)
}
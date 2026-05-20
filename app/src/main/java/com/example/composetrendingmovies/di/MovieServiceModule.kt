package com.example.composetrendingmovies.di

import com.example.composetrendingmovies.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kotlin.imdb.db.MovieDao
import io.github.kotlin.imdb.db.MovieDatabase
import io.github.kotlin.imdb.db.getDatabase
import io.github.kotlin.imdb.service.TmdbService
import io.github.kotlin.imdb.service.provideTmdbService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieServiceModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(): MovieDatabase {
        return getDatabase()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }


    @Provides
    @Singleton
    fun provideTmdbService(): TmdbService {
        return provideTmdbService(token = Constants.API_KEY_V4)
    }
}
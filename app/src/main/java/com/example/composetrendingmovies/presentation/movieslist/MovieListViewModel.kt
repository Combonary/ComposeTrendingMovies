package com.example.composetrendingmovies.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kotlin.imdb.db.MovieDao
import io.github.kotlin.imdb.model.toEntity
import io.github.kotlin.imdb.service.TmdbService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val tmdbService: TmdbService,
    private val movieDao: MovieDao
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovieListScreenUIState> =
        MutableStateFlow(MovieListScreenUIState())
    val uiState: StateFlow<MovieListScreenUIState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getMovies()
        }
    }

    private suspend fun getMovies() {
        _uiState.value = MovieListScreenUIState(isLoading = true)

        try {
            val cachedData = movieDao.getAllMovies().first()

            if (cachedData.isNotEmpty()) {
                _uiState.value = MovieListScreenUIState(
                    isLoading = false,
                    movies = cachedData
                )
            }

            val trendingMovies = tmdbService.getPopularMovies()
            val entities = trendingMovies.results.map { it.toEntity() }
            movieDao.insertMovies(entities)
            _uiState.value = MovieListScreenUIState(
                isLoading = false,
                movies = entities
            )
        } catch (e: Exception) {
            _uiState.value = MovieListScreenUIState(
                isLoading = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }
}
package com.example.composetrendingmovies.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.utils.Constants
import com.example.composetrendingmovies.utils.GenreMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kotlin.imdb.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<MovieListScreenUIState> = moviesRepository.movies
        .map { movies ->
            movies.map { movie ->
                MovieUiModel(
                    id = movie.id,
                    title = movie.title,
                    summary = GenreMap.getGenre(movie.genreIds),
                    // Pre-concatenate the full URL here
                    posterPath = Constants.IMAGE_URL + movie.posterPath
                )
            }
        }
        .combine(_isLoading) { movies, isLoading ->
            MovieListScreenUIState(
                movies = movies,
                isLoading = isLoading,
                error = _error.value
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, MovieListScreenUIState())

    init {
        refreshMovies()
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                moviesRepository.refreshMovies()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

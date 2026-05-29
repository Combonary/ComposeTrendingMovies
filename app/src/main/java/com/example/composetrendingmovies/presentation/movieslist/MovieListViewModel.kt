package com.example.composetrendingmovies.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kotlin.imdb.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MovieListScreenUIState> =
        MutableStateFlow(MovieListScreenUIState())
    val uiState: StateFlow<MovieListScreenUIState>
        get() = _uiState.asStateFlow()

    init {
        // Collect movies from the repository - Single Source of Truth
        viewModelScope.launch {
            moviesRepository.movies.collect { movies ->
                _uiState.update { it.copy(movies = movies, isLoading = false) }
            }
        }
        
        refreshMovies()
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                moviesRepository.refreshMovies()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
            }
        }
    }
}

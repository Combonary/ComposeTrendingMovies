package com.example.composetrendingmovies.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.utils.Constants
import com.example.composetrendingmovies.utils.GenreMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kotlin.imdb.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
        // Collect movies from the repository and transform to UI models
        viewModelScope.launch {
            moviesRepository.movies
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
                .collect { uiMovies ->
                    _uiState.update { it.copy(movies = uiMovies, isLoading = false) }
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

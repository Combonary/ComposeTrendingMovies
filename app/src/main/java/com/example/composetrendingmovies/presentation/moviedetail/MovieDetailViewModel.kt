package com.example.composetrendingmovies.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kotlin.imdb.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailScreenUiState> =
        MutableStateFlow(MovieDetailScreenUiState.Loading)
    val uiState: StateFlow<MovieDetailScreenUiState> =
        _uiState.asStateFlow()

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            getMovie(id)
        }
    }

    private suspend fun getMovie(id: Int) {
        _uiState.value = MovieDetailScreenUiState.Loading

        try {
            val movie = moviesRepository.getMovieById(id)
            _uiState.value = MovieDetailScreenUiState.Success(movie)
        } catch (e: Exception) {
            _uiState.value = MovieDetailScreenUiState.Error(
                e.message ?: "Unknown error occurred"
            )
        }
    }
}

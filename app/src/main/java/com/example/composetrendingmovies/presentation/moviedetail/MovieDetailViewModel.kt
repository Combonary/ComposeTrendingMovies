package com.example.composetrendingmovies.presentation.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kotlin.imdb.service.TmdbService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tmdbService: TmdbService
) : ViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailScreenUiState> =
        MutableStateFlow(MovieDetailScreenUiState())
    val uiState: StateFlow<MovieDetailScreenUiState> =
        _uiState.asStateFlow()


    init {
        savedStateHandle.get<Int>("movie_id")?.let { id ->
            viewModelScope.launch {
                getMovie(id)
            }
        }
    }

    private suspend fun getMovie(id: Int) {
        _uiState.value = MovieDetailScreenUiState(isLoading = true)

        try {
            val movie = tmdbService.getMovie(id)
            _uiState.value = MovieDetailScreenUiState(isLoading = false, movie = movie)
        } catch (e: Exception) {
            _uiState.value = MovieDetailScreenUiState(
                isLoading = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }
}

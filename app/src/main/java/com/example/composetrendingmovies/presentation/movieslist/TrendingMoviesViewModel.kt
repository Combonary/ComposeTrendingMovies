package com.example.composetrendingmovies.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.usecase.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val trendingMoviesUseCase: GetTrendingMoviesUseCase
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
        trendingMoviesUseCase.invoke().collect { result ->
            result?.let { (status, data, error, message) ->
                when (status) {
                    ServerResult.Status.LOADING -> {
                        _uiState.value = MovieListScreenUIState(isLoading = true)
                    }

                    ServerResult.Status.SUCCESS -> {
                        _uiState.value = MovieListScreenUIState(isLoading = false, data?.moviesList)
                    }

                    ServerResult.Status.ERROR -> {
                        _uiState.value = MovieListScreenUIState(
                            isLoading = false,
                            error = error?.errorMessage ?: message
                        )
                    }
                }
            }
        }
    }
}
package com.example.composetrendingmovies.presentation.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.usecase.GetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieDetailUseCase: GetMovieUseCase
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
        movieDetailUseCase.invoke(id).collect { serverResult ->
            when (serverResult.status) {
                ServerResult.Status.SUCCESS -> {
                    _uiState.value = MovieDetailScreenUiState(isLoading = false, movie = serverResult.data)
                }

                ServerResult.Status.ERROR -> {
                    _uiState.value = MovieDetailScreenUiState(isLoading = false, error = serverResult.message)
                }

                ServerResult.Status.LOADING -> {
                    _uiState.value = MovieDetailScreenUiState(isLoading = true)
                }
            }
        }
    }
}

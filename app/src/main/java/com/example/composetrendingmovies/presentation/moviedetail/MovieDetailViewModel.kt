package com.example.composetrendingmovies.presentation.moviedetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.usecase.GetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieDetailUseCase: GetMovieUseCase
): ViewModel() {

    private var _movie: MutableState<MovieDescription?> = mutableStateOf(null)
    val movie: MutableState<MovieDescription?>
        get() = _movie

    private var _apiStatus: MutableState<ServerResult.Status> = mutableStateOf(ServerResult.Status.LOADING)
    val apiStatus: MutableState<ServerResult.Status>
        get() = _apiStatus

    init {
        savedStateHandle.get<Int>("movieId")?.let { id ->
            viewModelScope.launch {
                getMovie(id)
            }
        }
    }

    private suspend fun getMovie(id: Int) {
        movieDetailUseCase.invoke(id).collect {
            _apiStatus.value = it.status
            _movie.value = it.data
        }
    }
}

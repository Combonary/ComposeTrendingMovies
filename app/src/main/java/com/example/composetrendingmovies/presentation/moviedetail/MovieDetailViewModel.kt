package com.example.composetrendingmovies.presentation.moviedetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.usecase.GetMovieUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailViewModel.MovieDetailViewModelFactory::class)
class MovieDetailViewModel @AssistedInject constructor(
    @Assisted private val id: Int,
    private val movieDetailUseCase: GetMovieUseCase
): ViewModel() {

    private var _movie: MutableState<MovieDescription?> = mutableStateOf(null)
    val movie: MutableState<MovieDescription?>
        get() = _movie

    private var _apiStatus: MutableState<ServerResult.Status> = mutableStateOf(ServerResult.Status.LOADING)
    val apiStatus: MutableState<ServerResult.Status>
        get() = _apiStatus

    @AssistedFactory
    interface MovieDetailViewModelFactory {
        fun create(id: Int): MovieDetailViewModel
    }

    init {
        viewModelScope.launch {
            getMovie(id)
        }
    }

    private suspend fun getMovie(id: Int) {
        movieDetailUseCase.invoke(id).collect {
            _apiStatus.value = it.status
            _movie.value = it.data
        }
    }
}
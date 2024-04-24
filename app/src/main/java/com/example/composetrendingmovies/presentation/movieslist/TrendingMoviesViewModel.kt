package com.example.composetrendingmovies.presentation.movieslist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetrendingmovies.domain.model.Movie
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.domain.model.TrendingMovies
import com.example.composetrendingmovies.domain.usecase.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(private val trendingMoviesUseCase: GetTrendingMoviesUseCase) :
    ViewModel() {

    private var _trendingMoviesList = mutableStateOf(listOf<Movie>())
    val trendingMoviesList: MutableState<List<Movie>>
        get() = _trendingMoviesList
    private var _apiStatus: MutableState<ServerResult.Status?> =
        mutableStateOf(ServerResult.Status.LOADING)
    var apiStatus: State<ServerResult.Status?> = _apiStatus

    init {
        viewModelScope.launch {
            getMovies()
        }
    }

    private suspend fun getMovies() {
        trendingMoviesUseCase.invoke().collect {
            _trendingMoviesList.value = it?.data?.results ?: emptyList()
            _apiStatus.value = it?.status
        }
    }
}
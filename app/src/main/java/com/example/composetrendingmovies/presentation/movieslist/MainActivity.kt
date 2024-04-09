package com.example.composetrendingmovies.presentation.movieslist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composetrendingmovies.presentation.moviedetail.MovieDetailActivity
import com.example.composetrendingmovies.presentation.movieslist.MovieListScreen
import com.example.composetrendingmovies.ui.theme.ComposeTrendingMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieListScreen(
                onBackClicked = { onBackPressedDispatcher.onBackPressed()},
                onMovieItemClicked = { goToMovieDetailScreen(it) }
            )
        }
    }

    private fun goToMovieDetailScreen(id: Int) {
        val movieDetailIntent = Intent(this, MovieDetailActivity::class.java)
        movieDetailIntent.putExtra("movie_id", id)
        startActivity(movieDetailIntent)
    }
}
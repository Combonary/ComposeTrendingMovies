package com.example.composetrendingmovies.presentation.movieslist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composetrendingmovies.presentation.moviedetail.MovieDetailActivity
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
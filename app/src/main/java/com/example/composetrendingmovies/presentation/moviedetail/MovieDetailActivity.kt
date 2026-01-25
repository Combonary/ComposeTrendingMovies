package com.example.composetrendingmovies.presentation.moviedetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieDetailScreen (
                onBackClicked = { onBackPressedDispatcher.onBackPressed() }
            )
        }
    }
}

package com.example.composetrendingmovies.presentation.movieslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.composetrendingmovies.presentation.moviedetail.MovieDetailScreen
import com.example.composetrendingmovies.presentation.navigation.MovieDetailRoute
import com.example.composetrendingmovies.presentation.navigation.MovieListRoute
import com.example.composetrendingmovies.presentation.navigation.Navigator
import com.example.composetrendingmovies.presentation.navigation.rememberNavigationState
import com.example.composetrendingmovies.presentation.navigation.toEntries
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Constant set of routes should be remembered
            val topLevelRoutes = remember { setOf(MovieListRoute) }
            val navigationState = rememberNavigationState(
                startRoute = MovieListRoute,
                topLevelRoutes = topLevelRoutes
            )
            val navigator = remember(navigationState) { Navigator(navigationState) }

            val onBack = remember { { finish() } }
            val onMovieClicked = remember(navigator) {
                { id: Int -> navigator.navigate(MovieDetailRoute(movieId = id)) }
            }

            val entryProvider = entryProvider {
                entry<MovieListRoute> {
                    MovieListScreen(
                        onBackClicked = onBack,
                        onMovieItemClicked = onMovieClicked
                    )
                }
                entry<MovieDetailRoute> { route ->
                    MovieDetailScreen(
                        onBackClicked = { navigator.goBack() },
                        movieId = route.movieId
                    )
                }
            }

            NavDisplay(
                entries = navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }
}

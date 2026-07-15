package com.example.composetrendingmovies.presentation.movieslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.composetrendingmovies.presentation.moviedetail.MovieDetailScreen
import com.example.composetrendingmovies.presentation.navigation.MovieDetailRoute
import com.example.composetrendingmovies.presentation.navigation.MovieListRoute
import com.example.composetrendingmovies.presentation.navigation.Navigator
import com.example.composetrendingmovies.presentation.navigation.rememberNavigationState
import com.example.composetrendingmovies.presentation.navigation.toEntries
import com.example.composetrendingmovies.presentation.ui.theme.ComposeTrendingMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTrendingMoviesTheme {
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

                val onBackToNavigator = remember(navigator) {
                    { navigator.goBack() }
                }

                val entryProvider = remember(navigator, onBack, onMovieClicked, onBackToNavigator) {
                    entryProvider {
                        entry<MovieListRoute> {
                            MovieListScreen(
                                onBackClicked = onBack,
                                onMovieItemClicked = onMovieClicked
                            )
                        }
                        entry<MovieDetailRoute> { route ->
                            MovieDetailScreen(
                                onBackClicked = onBackToNavigator,
                                movieId = route.movieId
                            )
                        }
                    }
                }

                val entries = navigationState.toEntries(entryProvider)

                NavDisplay(
                    entries = entries,
                    onBack = onBackToNavigator
                )
            }
        }
    }
}

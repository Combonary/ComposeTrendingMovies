package com.example.composetrendingmovies.presentation.movieslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.composetrendingmovies.presentation.moviedetail.MovieDetailScreen
import com.example.composetrendingmovies.presentation.navigation.MovieDetailRoute
import com.example.composetrendingmovies.presentation.navigation.MovieListRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = MovieListRoute
            ) {
                composable<MovieListRoute> {
                    MovieListScreen(
                        onBackClicked = { finish() },
                        onMovieItemClicked = { id ->
                            navController.navigate(MovieDetailRoute(movie_id = id))
                        }
                    )
                }
                composable<MovieDetailRoute> { backStackEntry ->
                    // Though MovieDetailViewModel uses SavedStateHandle, 
                    // we can also pass the id explicitly if we wanted to refactor.
                    // But SavedStateHandle.get<Int>("movie_id") will work if we use 
                    // the property name 'movie_id' in the Route class.
                    MovieDetailScreen(
                        onBackClicked = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

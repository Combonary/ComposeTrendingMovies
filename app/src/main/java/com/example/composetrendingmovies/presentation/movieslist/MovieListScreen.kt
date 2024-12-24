package com.example.composetrendingmovies.presentation.movieslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.composetrendingmovies.R
import com.example.composetrendingmovies.domain.model.Movie
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.ui.theme.ComposeTrendingMoviesTheme
import com.example.composetrendingmovies.utils.Constants
import com.example.composetrendingmovies.utils.GenreMap

@Composable
fun MovieListScreen(
    viewModel: TrendingMoviesViewModel = viewModel(),
    onBackClicked: () -> Unit,
    onMovieItemClicked: (Int) -> Unit
) {
    val movieList by remember {
        viewModel.trendingMoviesList
    }

    val apiStatus by remember {
        viewModel.apiStatus
    }

    MoviesListScreenContent(
        onBackClicked = onBackClicked,
        onMovieItemClicked = {
            onMovieItemClicked(it)
        },
        movieList = movieList,
        apiStatus = apiStatus
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesListScreenContent(
    onBackClicked: () -> Unit = {},
    onMovieItemClicked: (Int) -> Unit = {},
    movieList: List<Movie> = listOf(),
    apiStatus: ServerResult.Status? = null
) {
    var loading by remember {
        mutableStateOf(false)
    }

    when (apiStatus) {
        ServerResult.Status.SUCCESS -> {
            loading = false
        }

        ServerResult.Status.ERROR -> {
            loading = false
        }

        ServerResult.Status.LOADING -> {
            loading = true
        }

        null -> {}
    }
    ComposeTrendingMoviesTheme {


        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Trending Movies") },
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Close App"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->

            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(64.dp)
                                .align(Alignment.Center)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(movieList) { movie ->
                            MovieItem(
                                imageUrl = movie.poster_path,
                                movieName = movie.title,
                                summary = GenreMap.getGenre(movie.genre_ids),
                                movieId = movie.id,
                                onItemSelected = { onMovieItemClicked(it) }
                            )
                            HorizontalDivider(color = Color.Cyan)
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(
    imageUrl: String,
    movieName: String,
    summary: String,
    movieId: Int,
    onItemSelected: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(
                enabled = true,
                onClick = { onItemSelected(movieId) }
            )
    ) {
        GlideImage(
            modifier = Modifier
                .width(250.dp)
                .height(400.dp)
                .padding(4.dp),
            model = Constants.IMAGE_URL + imageUrl,
            contentDescription = "movie poster",
            loading = placeholder(R.drawable.round_downloading_24),
            failure = placeholder(R.drawable.round_downloading_24)
        )

        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = movieName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = summary,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Preview
@Composable
fun MovieListScreenPreview() {
    MoviesListScreenContent()
}
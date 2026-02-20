package com.example.composetrendingmovies.presentation.movieslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.composetrendingmovies.R
import com.example.composetrendingmovies.domain.model.Movie
import com.example.composetrendingmovies.presentation.ui.theme.ComposeTrendingMoviesTheme
import com.example.composetrendingmovies.utils.Constants
import com.example.composetrendingmovies.utils.GenreMap

@Composable
fun MovieListScreen(
    viewModel: TrendingMoviesViewModel = viewModel(),
    onBackClicked: () -> Unit,
    onMovieItemClicked: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesListScreenContent(
        onBackClicked = onBackClicked,
        onMovieItemClicked = {
            onMovieItemClicked(it)
        },
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesListScreenContent(
    onBackClicked: () -> Unit = {},
    onMovieItemClicked: (Int) -> Unit = {},
    uiState: MovieListScreenUIState
) {
    ComposeTrendingMoviesTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.movie_list_activity_title)) },
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
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(64.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                    uiState.error?.let {
                        Text(
                            text = uiState.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.movies?.let {
                        HorizontalDivider(color = Color.Black)
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(uiState.movies) { movie ->
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
    // Use BoxWithConstraints to read available width and adapt image size accordingly
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        val availableWidth = maxWidth
        // Choose an image width relative to available width with sensible caps
        val imageWidth = when {
            availableWidth < 360.dp -> availableWidth * 0.32f
            availableWidth < 600.dp -> availableWidth * 0.28f
            else -> 150.dp
        }

        // spacing between image and text
        val spacing = 8.dp
        val textColumnWidthCandidate = availableWidth - imageWidth - spacing
        val textColumnWidth = if (textColumnWidthCandidate > 0.dp) textColumnWidthCandidate else 0.dp

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemSelected(movieId) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                modifier = Modifier
                    .width(imageWidth)
                    .aspectRatio(2f / 3f)
                    .padding(end = spacing)
                    .clip(RoundedCornerShape(8.dp)),
                model = Constants.IMAGE_URL + imageUrl,
                contentDescription = "movie poster",
                contentScale = ContentScale.Crop,
                loading = placeholder(R.drawable.round_downloading_24),
                failure = placeholder(R.drawable.round_downloading_24)
            )

            Column(
                modifier = Modifier
                    .width(textColumnWidth)
                    .padding(start = 4.dp)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = movieName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = summary,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Preview
@Composable
fun MovieListScreenPreview() {
    MoviesListScreenContent(
        uiState = MovieListScreenUIState(
            movies = listOf(
                Movie(
                    adult = false,
                    backdrop_path = "",
                    genre_ids = listOf(28),
                    vote_count = 100,
                    original_language = "en",
                    original_title = "Dummy Movie",
                    id = 1,
                    title = "Dummy Movie",
                    video = false,
                    vote_average = 7.5,
                    poster_path = "",
                    overview = "This is a dummy movie for preview.",
                    release_date = "2023-01-01",
                    popularity = 100.0,
                    media_type = "movie"
                )
            )
        )
    )
}
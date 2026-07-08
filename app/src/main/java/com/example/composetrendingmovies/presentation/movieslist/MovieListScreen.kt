package com.example.composetrendingmovies.presentation.movieslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.remember
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.composetrendingmovies.R
import com.example.composetrendingmovies.presentation.ui.theme.ComposeTrendingMoviesTheme

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onMovieItemClicked: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesListScreenContent(
        onBackClicked = onBackClicked,
        onMovieItemClicked = onMovieItemClicked,
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

                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                uiState.movies,
                                key = { movie -> movie.id }
                            ) { movie ->
                                MovieItem(
                                    imageUrl = movie.posterPath,
                                    movieName = movie.title,
                                    summary = movie.summary,
                                    movieId = movie.id,
                                    onItemSelected = onMovieItemClicked
                                )
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
    imageUrl: String?,
    movieName: String,
    summary: String,
    movieId: Int,
    onItemSelected: (Int) -> Unit = {}
) {
    // Memoize the click lambda so ElevatedCard can skip recomposition
    val onClick = remember(movieId, onItemSelected) {
        { onItemSelected(movieId) }
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Properly remember the placeholder to prevent GlideImage from invalidating the Row
            val placeholderPainter = remember { placeholder(R.drawable.round_downloading_24) }

            GlideImage(
                modifier = Modifier
                    .weight(0.3f)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp)),
                model = imageUrl,
                contentDescription = "movie poster",
                contentScale = ContentScale.Crop,
                loading = placeholderPainter,
                failure = placeholderPainter
            )

            MovieItemDetails(
                title = movieName,
                summary = summary,
                modifier = Modifier
                    .weight(0.7f)
                    .padding(start = 12.dp)
            )
        }
    }
}

@Composable
private fun MovieItemDetails(
    title: String,
    summary: String,
    modifier: Modifier = Modifier
) {
    // We use a Column but apply the weights locally
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun MovieListScreenPreview() {
    MoviesListScreenContent(
        uiState = MovieListScreenUIState()
    )
}
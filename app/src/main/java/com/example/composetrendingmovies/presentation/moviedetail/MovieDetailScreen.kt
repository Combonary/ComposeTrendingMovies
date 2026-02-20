package com.example.composetrendingmovies.presentation.moviedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.composetrendingmovies.R
import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.presentation.ui.theme.ComposeTrendingMoviesTheme
import com.example.composetrendingmovies.utils.Constants

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailScreenContent(
        onBackClicked = onBackClicked,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailScreenContent(
    onBackClicked: () -> Unit,
    uiState: MovieDetailScreenUiState
) {
    // Derive concrete values from the sealed UI state
    val loading = uiState.isLoading
    val movie: MovieDescription? = uiState.movie
    val errorMessage: String? = uiState.error

    ComposeTrendingMoviesTheme {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { movie?.title?.let { Text(it) } ?: Text("Movie detail") },
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(64.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }

                    // Show error if present
                    errorMessage?.let { msg ->
                        Text(
                            text = msg,
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Content for successful movie load
                    movie?.let {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            // Responsive poster: center and size based on available width
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                BoxWithConstraints {
                                    val maxW = maxWidth
                                    val imageWidth = when {
                                        maxW < 360.dp -> maxW * 0.95f
                                        maxW < 600.dp -> maxW * 0.8f
                                        else -> 600.dp
                                    }

                                    GlideImage(
                                        modifier = Modifier
                                            .width(imageWidth)
                                            .aspectRatio(2f / 3f)
                                            .clip(RoundedCornerShape(8.dp)),
                                        model = Constants.IMAGE_URL + (movie.poster_path),
                                        contentDescription = "Movie poster",
                                        loading = placeholder(R.drawable.round_downloading_24),
                                        failure = placeholder(R.drawable.round_downloading_24)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = movie.overview
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailScreenContentPreview() {
    val sampleMovie = MovieDescription(
        id = 1,
        title = "Sample Movie",
        overview = "This is a sample overview for preview purposes.",
        poster_path = "/sample.jpg",
        genres = emptyList()
    )

    MovieDetailScreenContent(
        onBackClicked = {},
        uiState = MovieDetailScreenUiState(
            isLoading = false,
            movie = sampleMovie,
            error = null
        )
    )

}

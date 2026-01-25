package com.example.composetrendingmovies.presentation.moviedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.composetrendingmovies.R
import com.example.composetrendingmovies.domain.model.MovieDescription
import com.example.composetrendingmovies.domain.model.ServerResult
import com.example.composetrendingmovies.ui.theme.ComposeTrendingMoviesTheme
import com.example.composetrendingmovies.utils.Constants

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val movie by remember {
        viewModel.movie
    }
    val apiStatus by remember {
        viewModel.apiStatus
    }

    MovieDetailScreenContent(
        onBackClicked = onBackClicked,
        apiStatus = apiStatus,
        movie = movie
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailScreenContent(
    onBackClicked: () -> Unit,
    apiStatus: ServerResult.Status,
    movie: MovieDescription? = null
){
    var loading by remember { mutableStateOf(false) }

    loading = when (apiStatus) {
        ServerResult.Status.LOADING -> true
        ServerResult.Status.SUCCESS -> false
        ServerResult.Status.ERROR -> false
    }

    ComposeTrendingMoviesTheme {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { movie?.title?.let { Text(it) } },
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
                                .align(Alignment.Center)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }

                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        GlideImage(
                            modifier = Modifier
                                .height(1000.dp)
                                .width(600.dp)
                                .align(Alignment.CenterHorizontally)
                                .clip(RoundedCornerShape(2.dp)),
                            model = Constants.IMAGE_URL + movie?.poster_path,
                            contentDescription = "Movie poster",
                            loading = placeholder(R.drawable.round_downloading_24),
                            failure = placeholder(R.drawable.round_downloading_24)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        movie?.overview?.let {
                            Text(
                                text = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieDetailScreenContentPreview(){

}

package com.mac.swaps.presentation.ui.movie

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.coil.CoilImage
import com.mac.swaps.Config
import com.mac.swaps.presentation.components.ToolBar
import com.mac.swaps.presentation.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    viewModel: MovieViewModel
) {
    val loading = viewModel.loading.value
    val movieDesc = viewModel.movieDes.value
    val alertPopUp = viewModel.dialogQueue

    AppTheme(
        displayProgressBar = loading,
        popUpQueue = alertPopUp.queue.value
    ) {
        Scaffold(topBar = {
            ToolBar(
                title = "Movie Details", canNavigateBack = true,
                isChecked = false, filterCallback = {},
                navController = navController
            )
        }) {
            Box(modifier = Modifier.fillMaxSize()) {
                movieDesc?.let {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            CoilImage(
                                data = Config.IMAGE_URL + it.poster_path,
                                contentDescription = it.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(225.dp),
                                contentScale = ContentScale.Crop,
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        text = it.title,
                                        modifier = Modifier
                                            .fillMaxWidth(0.85f)
                                            .wrapContentWidth(Alignment.Start),
                                        style = MaterialTheme.typography.h3
                                    )
                                }
                                Text(
                                    text = it.overview,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 4.dp),
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
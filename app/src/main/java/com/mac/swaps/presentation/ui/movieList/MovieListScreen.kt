package com.mac.swaps.presentation.ui.movieList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mac.swaps.presentation.components.MovieCard
import com.mac.swaps.presentation.components.ToolBar
import com.mac.swaps.presentation.navigation.Screens
import com.mac.swaps.presentation.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun MovieListScreen(
    navController: NavHostController,
    onNavigateToMovieDetailScreen: (String) -> Unit,
    viewModel: MovieListViewModel
) {
    val loading = viewModel.loading.value
    val movies = viewModel.movies.value
    val filter = viewModel.filter.value
    val alertPopUp = viewModel.dialogQueue

    AppTheme(
        displayProgressBar = loading,
        popUpQueue = alertPopUp.queue.value
    ) {
        Scaffold(topBar = {
            ToolBar(
                title = "Movies", canNavigateBack = false,
                isChecked = filter, filterCallback = { value ->
                    viewModel.onFilterByFavorites(value)
                },
                navController = navController
            )
        }) {
            Box(
                modifier = Modifier.background(MaterialTheme.colors.surface)
            ) {
                LazyColumn {
                    items(count = movies.size,
                        itemContent = { index ->
                            MovieCard(movie = movies[index], onClick = {
                                val route = Screens.MovieDetail.route + "/${movies[index].id}"
                                onNavigateToMovieDetailScreen(route)
                            }, isFavorite = movies[index].isFavorite, isFavCallback = { value ->
                                viewModel.onFavoriteUpdate(value, movies[index].id)
                            })
                        }
                    )
                }
            }
        }
    }
}
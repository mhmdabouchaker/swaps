package com.mac.swaps.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.*
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.mac.swaps.presentation.navigation.Screens
import com.mac.swaps.presentation.ui.movie.MovieDetailScreen
import com.mac.swaps.presentation.ui.movie.MovieViewModel
import com.mac.swaps.presentation.ui.movieList.MovieListScreen
import com.mac.swaps.presentation.ui.movieList.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navigationController = rememberNavController()
            NavHost(navController = navigationController, startDestination = Screens.MovieList.route ){
                composable(route = Screens.MovieList.route){ navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: MovieListViewModel = viewModel(key = "MovieListViewModel", factory = factory)
                    MovieListScreen(
                        navigationController,
                        onNavigateToMovieDetailScreen = navigationController::navigate,
                        viewModel = viewModel)
                }
                composable(route = Screens.MovieDetail.route + "/{movieId}",
                    arguments = listOf(navArgument("movieId"){
                        type = NavType.IntType
                    })
                ){ navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: MovieViewModel = viewModel(key = "MovieViewModel", factory = factory)
                    MovieDetailScreen(navigationController, viewModel = viewModel)
                }
            }
        }
    }
}

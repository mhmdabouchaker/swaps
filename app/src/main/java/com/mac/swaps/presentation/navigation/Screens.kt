package com.mac.swaps.presentation.navigation

sealed class Screens(val route: String) {
    object MovieList: Screens("movieList")
    object MovieDetail: Screens("movieDetail")
}

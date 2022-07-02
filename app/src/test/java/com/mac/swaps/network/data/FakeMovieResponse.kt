package com.mac.swaps.network.data

import com.mac.swaps.model.Movie

object FakeMovieResponse {

    private val movieWithFavorite = Movie(1,"Hello","kkk",2.0,"124124", false)
    val movieFavoriteList= arrayListOf(movieWithFavorite)

    private val movieWithoutFavorite = Movie(1,"Hello","kkk",2.0,"124124", false)
    val movieUnFavoriteList = arrayListOf(movieWithoutFavorite)
}
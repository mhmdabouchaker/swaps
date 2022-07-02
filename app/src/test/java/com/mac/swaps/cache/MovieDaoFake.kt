package com.mac.swaps.cache

import com.mac.swaps.data.local.MovieDao
import com.mac.swaps.model.Movie

class MovieDaoFake (
    private val appDatabaseFake: AppDatabaseFake
        ): MovieDao{
    override suspend fun getAll(): List<Movie> {
        return appDatabaseFake.movies
    }

    override suspend fun insertMovie(movie: Movie) {
        appDatabaseFake.movies.add(movie)
    }

    override suspend fun insertAll(movies: List<Movie>) {
        appDatabaseFake.movies.addAll(movies)
    }

    override suspend fun getMovieById(id: Int): Movie? {
        return appDatabaseFake.movies.find { it.id == id }
    }

    override suspend fun updateFavorites(isFavorite: Boolean, id: Int) {
        appDatabaseFake.movies.forEach{
            if (it.id == id){
                it.isFavorite = isFavorite
            }
        }
    }

    override suspend fun filterByFavorites(isFiltered: Boolean): List<Movie> {
        return appDatabaseFake.movies
    }

    override suspend fun deleteAll(movie: List<Movie>) {
        appDatabaseFake.movies.removeAll(movie)
    }
}
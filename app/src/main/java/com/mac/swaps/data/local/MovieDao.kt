package com.mac.swaps.data.local

import androidx.room.*
import com.mac.swaps.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie order by popularity DESC")
    suspend fun getAll(): List<Movie>?

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieById(id: Int): Movie?

    @Query("UPDATE movie SET isFavorite=:isFavorite WHERE  id=:id")
    suspend fun updateFavorites(isFavorite: Boolean, id: Int)


    @Query("SELECT * FROM movie WHERE isFavorite  LIKE '%' || :isFiltered || '%' order by popularity DESC")
    suspend fun filterByFavorites(isFiltered: Boolean): List<Movie>

    @Delete
    suspend fun deleteAll(movie: List<Movie>)
}
package com.mac.swaps.data.local

import androidx.room.*
import com.mac.swaps.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie order by popularity DESC")
    fun getAll(): List<Movie>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Query("UPDATE movie SET isFavorite=:isFavorite WHERE  id=:id")
    fun updateFavorites(isFavorite: Boolean, id: Int)


    @Query("SELECT * FROM movie WHERE title LIKE '%' || :query || '%' AND isFavorite  LIKE '%' || :isFavorite || '%' order by popularity DESC")
    fun searchMovieByFavorite(query: String?, isFavorite: Boolean): List<Movie>

    @Query("SELECT * FROM movie WHERE isFavorite  LIKE '%' || :isFiltered || '%' order by popularity DESC")
    fun filterByFavorites(isFiltered: Boolean): List<Movie>

    @Delete
    fun delete(movie: Movie)

    @Delete
    fun deleteAll(movie: List<Movie>)
}
package com.mac.swaps.data

import com.mac.swaps.data.local.MovieDao
import com.mac.swaps.data.remote.MovieRemoteDataSource
import com.mac.swaps.di.IoDispatcher
import com.mac.swaps.model.Movie
import com.mac.swaps.model.TrendingMovieResponse
import com.mac.swaps.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository which fetches data from Remote or Local data sources
 */
class MovieRepository @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieDao: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    fun fetchMovies(): Flow<Result<TrendingMovieResponse>> {
        return flow {
            emit(Result.loading())
            var movies = fetchTrendingMoviesCached()
            if (movies.data != null && !movies.data?.results.isNullOrEmpty()) {
                emit(Result.success(movies.data))
            } else {
                val moviesFromNetwork = movieRemoteDataSource.fetchTrendingMovies()
                moviesFromNetwork.data?.let {
                    it.results?.let { it1 -> movieDao.deleteAll(it1) }
                    it.results?.let { it1 -> movieDao.insertAll(it1) }
                }
                movies = fetchTrendingMoviesCached()
                emit(Result.success(movies.data))
            }
        }.flowOn(ioDispatcher)
    }

    fun fetchMovieById(id: Int): Flow<Result<Movie>> {
        return flow {
            emit(Result.loading())
            // just to show loading, cache is fast
            delay(1000)
            var movie = fetchMovieByIdCached(id)
            if (movie != null) {
                emit(Result.success(movie))
            } else {
                val movieFromNetwork = movieRemoteDataSource.fetchMovie(id)
                movieFromNetwork.data?.let { movieDao.insertMovie(it) }

                movie = fetchMovieByIdCached(id)
                if (movie != null) {
                    emit(Result.success(movie))
                } else {
                    throw Exception("Unable to get movie from the cache.")
                }
            }
        }.flowOn(ioDispatcher)
    }

    fun filterMoviesByFavorite(isFiltered: Boolean): Flow<Result<TrendingMovieResponse>> {
        return flow {
            emit(Result.loading())
            if (isFiltered) {
                emit(filterByFavoritesCached())
            } else {
                emit(fetchTrendingMoviesCached())
            }
        }.flowOn(ioDispatcher)
    }

    fun updateFavorite(favorite: Boolean, id: Int): Flow<Result<TrendingMovieResponse>> {
        return flow {
            emit(Result.loading())
            movieDao.updateFavorites(favorite, id)
            emit(fetchTrendingMoviesCached())
        }.flowOn(ioDispatcher)
    }

    private suspend fun fetchTrendingMoviesCached(): Result<TrendingMovieResponse> =
        movieDao.getAll().let {
            Result.success(TrendingMovieResponse(it, false))
        }

    private suspend fun fetchMovieByIdCached(id: Int): Movie? {
        return movieDao.getMovieById(id)
    }

    private suspend fun filterByFavoritesCached(): Result<TrendingMovieResponse> =
        movieDao.filterByFavorites(true).let {
            Result.success(TrendingMovieResponse(it, true))
        }
}
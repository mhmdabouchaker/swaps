package com.mac.swaps.data

import com.mac.swaps.data.local.MovieDao
import com.mac.swaps.data.remote.MovieRemoteDataSource
import com.mac.swaps.di.IoDispatcher
import com.mac.swaps.model.Movie
import com.mac.swaps.model.MovieDesc
import com.mac.swaps.model.TrendingMovieResponse
import com.mac.swaps.model.Result
import kotlinx.coroutines.CoroutineDispatcher
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher) {

    var movieList: List<Movie> = emptyList()

    fun fetchTrendingMovies(): Flow<Result<TrendingMovieResponse>> {
        return flow {
            emit(Result.loading())
            val result = movieRemoteDataSource.fetchTrendingMovies()

            //Cache to database if response is successful
            if (result.data?.results != null) {
                result.data.results.let {
//                    movieDao.deleteAll(it)
//                    movieDao.insertAll(it)
                }
            }
            emit(fetchTrendingMoviesCached())
        }.flowOn(ioDispatcher)
    }

    fun fetchMovie(id: Int): Flow<Result<MovieDesc>> {
        return flow {
            emit(Result.loading())
            emit(movieRemoteDataSource.fetchMovie(id))
        }.flowOn(ioDispatcher)
    }


    fun filterMoviesByFavorite(isFiltered: Boolean): Flow<Result<TrendingMovieResponse>> {
        return flow {
            emit(Result.loading())
            if (isFiltered){
                emit(filterByFavoritesCached())
            }else{
                emit(fetchTrendingMoviesCached())
            }
        }.flowOn(ioDispatcher)
    }
    private fun fetchTrendingMoviesCached(): Result<TrendingMovieResponse> =
        movieDao.getAll().let {
            if (it != null) {
                movieList = it
            }
            Result.success(TrendingMovieResponse(it, false))
        }

    fun updateFavorite(favorite: Boolean, id: Int): Flow<Result<TrendingMovieResponse>> {
        return flow {
            emit(Result.loading())
            movieDao.updateFavorites(favorite, id)
            emit(fetchTrendingMoviesCached())
        }.flowOn(ioDispatcher)
    }

    private fun filterByFavoritesCached(): Result<TrendingMovieResponse> =
        movieDao.filterByFavorites(true).let {
            Result.success(TrendingMovieResponse(it, true))
        }
}
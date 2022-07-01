package com.mac.swaps.data.remote

import com.mac.swaps.model.MovieDesc
import com.mac.swaps.model.TrendingMovieResponse
import com.mac.swaps.network.services.MovieService
import com.mac.swaps.util.ErrorUtils
import com.mac.swaps.model.Result
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchTrendingMovies(): Result<TrendingMovieResponse> {
        val movieService = retrofit.create(MovieService::class.java)
        return getResponse(
            request = {movieService.getPopularMovies()},
            defaultErrorMessage = "Something Went Wrong"
        )
    }

    suspend fun fetchMovie(id: Int): Result<MovieDesc> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
            request = { movieService.getMovie(id) },
            defaultErrorMessage = "Error fetching Movie Description")
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error")
        }
    }
}
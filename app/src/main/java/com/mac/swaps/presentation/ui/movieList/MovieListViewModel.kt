package com.mac.swaps.presentation.ui.movieList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.swaps.data.MovieRepository
import com.mac.swaps.model.Movie
import com.mac.swaps.presentation.ui.util.PopUpQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    val movies: MutableState<List<Movie>> = mutableStateOf(ArrayList())
    val loading = mutableStateOf(false)
    val filter = mutableStateOf(false)
    val dialogQueue = PopUpQueue()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        movieRepository.fetchMovies().onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.results.let { list ->
                if (list != null) {
                    movies.value = list
                }
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }
    fun onFavoriteUpdate(favorite: Boolean, movieId: Int) {

        movieRepository.updateFavorite(favorite, movieId).onEach { dataState ->

            dataState.data?.results.let { list ->
                if (list != null) {
                    movies.value = list
                }
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }


    fun onFilterByFavorites(isFiltered: Boolean) {
        movieRepository.filterMoviesByFavorite(isFiltered).onEach { dataState ->
            filter.value = dataState.data?.isFiltered ?: false
            loading.value = dataState.loading

            dataState.data?.results.let { list ->
                if (list != null) {
                    movies.value = list
                }
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }
}
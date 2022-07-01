package com.mac.swaps.presentation.ui.movie

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.swaps.data.MovieRepository
import com.mac.swaps.model.MovieDesc
import com.mac.swaps.presentation.ui.util.PopUpQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

const val STATE_KEY_MOVIE = "movie.state.movie.key"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val state: SavedStateHandle) : ViewModel(){

    val movieDes: MutableState<MovieDesc?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    val dialogQueue = PopUpQueue()

    private val movieId: Int? = state.get("movieId")

    init {
        movieId?.let { getMovie(it) }
    }

    private fun getMovie(id: Int){
        movieRepository.fetchMovie(id).onEach { dataState->
            loading.value = dataState.loading

            dataState.data?.let { data ->
                movieDes.value = data
                state.set(STATE_KEY_MOVIE, data.id)
            }

            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }
}
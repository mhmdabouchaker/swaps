package com.mac.swaps.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.mac.swaps.TestCoroutineRule
import com.mac.swaps.cache.AppDatabaseFake
import com.mac.swaps.cache.MovieDaoFake
import com.mac.swaps.data.MovieRepository
import com.mac.swaps.model.Movie
import com.mac.swaps.model.Result
import com.mac.swaps.model.TrendingMovieResponse
import com.mac.swaps.network.data.FakeMovieResponse.movieUnFavoriteList
import com.mac.swaps.network.data.FakeMovieResponse.movieWithoutFavorite
import com.mac.swaps.presentation.ui.movie.MovieViewModel
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: MovieRepository

    private val appDatabase = AppDatabaseFake()

    private lateinit var dao: MovieDaoFake


    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        dao = MovieDaoFake(appDatabase)
    }

    @Test
    fun getMoviesFromNetwork_getMovieById() = testCoroutineRule.runBlockingTest {

        val channel = Channel<Result<Movie>>()
        val flow = channel.receiveAsFlow()

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())

        Mockito.`when`(repository.fetchMovieById(1)).thenReturn(flow)

        dao.insertAll(movieUnFavoriteList)

        // confirm the cache is not empty
        assert(dao.getAll().isNotEmpty())

        launch {
            channel.send(Result.success(movieWithoutFavorite))
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(MovieViewModel.STATE_KEY_MOVIE, 1)
        }
        val viewModel = MovieViewModel(repository, savedStateHandle)
        viewModel.getMovie(1)

       // emission should be the movie
        val movie = viewModel.movie
        assert(movie.value?.id == 1)

        // confirm it is actually a Movie object
        assert(movie.value is Movie)

        // `loading` should be false now
        assert(!viewModel.loading.value)
    }

    @Test
    fun getNullMovieFromCache_getMovieById() = testCoroutineRule.runBlockingTest {
        val channel = Channel<Result<Movie>>()
        val flow = channel.receiveAsFlow()

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())

        Mockito.`when`(repository.fetchMovieById(1)).thenReturn(flow)

        launch {
            channel.send(Result.success(movieWithoutFavorite))
        }

        val savedStateHandle = SavedStateHandle().apply {
            set(MovieViewModel.STATE_KEY_MOVIE, 1)
        }
        val viewModel = MovieViewModel(repository, savedStateHandle)

        viewModel.getMovie(1)

        // emission should be the movie
        val movie = viewModel.movie
        assert(movie.value?.id == 1)

        movie.value?.let { dao.insertMovie(it) }
        // confirm the movie is in the cache now
        assert(dao.getMovieById(1)?.id == 1)

        // confirm it is actually a Movie object
        assert(movie.value is Movie)

        // `loading` should be false now
        assert(!viewModel.loading.value)

    }
}
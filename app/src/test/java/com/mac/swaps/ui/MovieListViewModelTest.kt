package com.mac.swaps.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mac.swaps.TestCoroutineRule
import com.mac.swaps.cache.AppDatabaseFake
import com.mac.swaps.cache.MovieDaoFake
import com.mac.swaps.data.MovieRepository
import com.mac.swaps.model.Result
import com.mac.swaps.model.TrendingMovieResponse
import com.mac.swaps.network.data.FakeMovieResponse.movieFavoriteList
import com.mac.swaps.network.data.FakeMovieResponse.movieUnFavoriteList
import com.mac.swaps.presentation.ui.movieList.MovieListViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieListViewModelTest {

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
    fun getMoviesFromNetwork_getMoviesFromCache() = testCoroutineRule.runBlockingTest{
        val movieResponse = TrendingMovieResponse(movieUnFavoriteList, false)
        val channel = Channel<Result<TrendingMovieResponse>>()
        val flow = channel.receiveAsFlow()

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())


        `when`(repository.fetchMovies()).thenReturn(flow)

        dao.insertAll(movieUnFavoriteList)

        // confirm the cache is no longer empty
        assert(dao.getAll().isNotEmpty())

        launch {
            channel.send(Result.success(movieResponse))
        }

        val viewModel = MovieListViewModel(repository)
        viewModel.fetchMovies()

        // emission should be the list of movies
        val movieList = viewModel.movies.value


        Assert.assertEquals(1, movieList.size)

        // loading should be false now
        assert(!viewModel.loading.value)
    }

    @Test
    fun getMoviesFromNetwork_emptyList() = testCoroutineRule.runBlockingTest {
        val movieResponse = TrendingMovieResponse(emptyList(), false)
        val channel = Channel<Result<TrendingMovieResponse>>()
        val flow = channel.receiveAsFlow()

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())

        `when`(repository.fetchMovies()).thenReturn(flow)

        // confirm the cache is still empty
        assert(dao.getAll().isEmpty())

        launch {
            channel.send(Result.success(movieResponse))
        }

        val viewModel = MovieListViewModel(repository)
        viewModel.fetchMovies()
        // emission should be the list of movies
        val movieList = viewModel.movies.value

        assertEquals(0, movieList.size)

        // loading should be false now
        assert(!viewModel.loading.value)
    }

    @Test
    fun favoriteMovieFromCache() = testCoroutineRule.runBlockingTest{
        val movieResponse = TrendingMovieResponse(movieFavoriteList, false)
        val channel = Channel<Result<TrendingMovieResponse>>()
        val flow = channel.receiveAsFlow()

        assert(dao.getAll().isEmpty())

        `when`(repository.fetchMovies()).thenReturn(flow)

        dao.insertAll(movieFavoriteList)

        assert(dao.getAll().isNotEmpty())

        // get movie id from cache
        val movieId = dao.getAll()[0].id

        assert(dao.getAll().isNotEmpty())

        // add item as favorite
        dao.updateFavorites(true, movieId)

        launch {
            channel.send(Result.success(movieResponse))
        }

        val viewModel = MovieListViewModel(repository)

        viewModel.onFavoriteUpdate(true, movieId)

        // emission should be the list of movies
        val movieList = viewModel.movies.value

        assertEquals(true, movieList[0].isFavorite)

        // loading should be false now
        assert(!viewModel.loading.value)
    }

    @Test
    fun unFavoriteMovieFromCache() = testCoroutineRule.runBlockingTest{
        val movieResponse = TrendingMovieResponse(movieUnFavoriteList, false)
        val channel = Channel<Result<TrendingMovieResponse>>()
        val flow = channel.receiveAsFlow()

        assert(dao.getAll().isEmpty())

        `when`(repository.fetchMovies()).thenReturn(flow)

        dao.insertAll(movieUnFavoriteList)

        assert(dao.getAll().isNotEmpty())

        // get movie id from cache
        val movieId = dao.getAll()[0].id

        assert(dao.getAll().isNotEmpty())

        // add item as favorite
        dao.updateFavorites(false, movieId)

        launch {
            channel.send(Result.success(movieResponse))
        }

        val viewModel = MovieListViewModel(repository)
        viewModel.onFavoriteUpdate(false, movieId)

        // emission should be the list of movies
        val movieList = viewModel.movies.value

        assertEquals(false, movieList[0].isFavorite)

        // loading should be false now
        assert(!viewModel.loading.value)
    }
}
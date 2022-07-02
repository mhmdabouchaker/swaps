package com.mac.swaps.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mac.swaps.TestCoroutineRule
import com.mac.swaps.data.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: MovieRepository

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getMoviesFromNetwork_getMovieById() = testCoroutineRule.runBlockingTest {

    }
}
package br.bano.chucknorris.ui.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class JokeVIewModelUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun loadJoke() {
        val viewModel: JokeViewModel = JokeViewModelTest()
        viewModel.loadJoke()
        assertNotNull(viewModel.jokeLiveData.value)
        assertNotNull(viewModel.jokeLiveData.value?.id)
        assertNotNull(viewModel.jokeLiveData.value?.joke)
        assertEquals("unknown", viewModel.jokeLiveData.value?.category)
    }

    @Test
    fun loadCategories() {
        val viewModel: JokeViewModel = JokeViewModelTest(listOf(
            "teste1",
            "teste2",
            "teste3",
            "teste4",
            "teste5",
            "teste6",
            "teste7"
        ))
        viewModel.loadCategories()
        assertNotNull(viewModel.categoriesLiveData.value)
        assertTrue(viewModel.categoriesLiveData.value?.isEmpty() == false)
        assertEquals("unknown", viewModel.categoriesLiveData.value?.get(0))
        assertEquals(8, viewModel.categoriesLiveData.value?.size)
    }
}

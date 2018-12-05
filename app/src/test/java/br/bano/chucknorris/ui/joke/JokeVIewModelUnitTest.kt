package br.bano.chucknorris.ui.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class JokeVIewModelUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun loadJoke() {
        val viewModel = JokeViewModelTest()
        viewModel.loadJoke()
        assertEquals(5, viewModel.getJokeListTest().size)
        assertEquals(0, viewModel.getJokeIndex())
        assertNotNull(viewModel.jokeLiveData.value)
        assertNotNull(viewModel.jokeLiveData.value?.id)
        assertNotNull(viewModel.jokeLiveData.value?.joke)
        assertEquals("unknown", viewModel.jokeLiveData.value?.category)
    }

    @Test
    fun loadCategories() {
        val viewModel = JokeViewModelTest(listOf(
            "teste1",
            "teste2",
            "teste3",
            "teste4",
            "teste5",
            "teste6",
            "teste7"
        ))
        viewModel.loadCategories()
        assertEquals(0, viewModel.getCategoryIndexTest())
        assertNotNull(viewModel.categoriesLiveData.value)
        assertTrue(viewModel.categoriesLiveData.value?.isEmpty() == false)
        assertEquals("unknown", viewModel.categoriesLiveData.value?.get(0))
        assertEquals(8, viewModel.categoriesLiveData.value?.size)
    }

    private fun addCategoryFilterCase1(): JokeViewModelTest {
        val viewModel = JokeViewModelTest()
        assertEquals(0, viewModel.getCategoryIndexTest())
        viewModel.addCategoryFilter("teste")
        assertEquals("teste", viewModel.getCategoriesFilteredTest()[0])
        assertEquals(2, viewModel.getCategoriesFilteredTest().size)
        assertEquals(1, viewModel.getCategoryIndexTest())
        assertEquals(5, viewModel.getJokeListTest().size)
        assertEquals(0, viewModel.getJokeIndex())
        assertNotNull(viewModel.jokeLiveData.value)
        assertNotNull(viewModel.jokeLiveData.value?.id)
        assertNotNull(viewModel.jokeLiveData.value?.joke)
        assertNotNull(viewModel.jokeLiveData.value?.category)
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(1, viewModel.getCategoryIndexTest())
        assertEquals(5, viewModel.getJokeListTest().size)
        assertEquals(1, viewModel.getJokeIndex())
        assertEquals("unknown", viewModel.jokeLiveData.value?.category)
        viewModel.loadJoke()
        assertEquals(1, viewModel.getCategoryIndexTest())
        assertEquals(5, viewModel.getJokeListTest().size)
        assertEquals(2, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        return viewModel
    }

    @Test
    fun addCategoryFilter() {
        addCategoryFilterCase1()
    }

    @Test
    fun addCategoryFilter_case2() {
        addCategoryFilterCase2()
    }

    private fun addCategoryFilterCase2(): JokeViewModelTest {
        val viewModel = addCategoryFilterCase1()

        viewModel.addCategoryFilter("teste2")
        assertEquals("teste2", viewModel.getCategoriesFilteredTest()[0])
        assertEquals(3, viewModel.getCategoriesFilteredTest().size)
        assertEquals(0, viewModel.getCategoryIndexTest())
        assertEquals(5, viewModel.getJokeListTest().size)
        assertEquals(2, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(10, viewModel.getJokeListTest().size)
        assertEquals(3, viewModel.getJokeIndex())
        assertEquals("unknown", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(10, viewModel.getJokeListTest().size)
        assertEquals(4, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(10, viewModel.getJokeListTest().size)
        assertEquals(5, viewModel.getJokeIndex())
        assertEquals("teste2", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(10, viewModel.getJokeListTest().size)
        assertEquals(6, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(10, viewModel.getJokeListTest().size)
        assertEquals(7, viewModel.getJokeIndex())
        assertEquals("unknown", viewModel.jokeLiveData.value?.category)

        return viewModel
    }

    @Test
    fun removeCategoryFilterCase1() {
        val viewModel = JokeViewModelTest()
        assertEquals("unknown", viewModel.getCategoriesFilteredTest()[0])
        assertEquals(1, viewModel.getCategoriesFilteredTest().size)
        viewModel.removeCategoryFilter("unknown")
        assertEquals("unknown", viewModel.getCategoriesFilteredTest()[0])
        assertEquals(1, viewModel.getCategoriesFilteredTest().size)
    }

    @Test
    fun removeCategoryFilterCase2() {
        val viewModel = addCategoryFilterCase2()
        assertEquals(3, viewModel.getCategoriesFilteredTest().size)
        assertEquals("teste2", viewModel.getCategoriesFilteredTest()[0])
        assertEquals("unknown", viewModel.getCategoriesFilteredTest()[2])

        val jokeListSizeBeforeRemoveCategory = viewModel.getJokeListTest().filter { it.category != "unknown" }.size

        viewModel.removeCategoryFilter("unknown")
        assertEquals(2, viewModel.getCategoriesFilteredTest().size)
        assertEquals("teste2", viewModel.getCategoriesFilteredTest()[0])
        assertTrue(!viewModel.getCategoriesFilteredTest().contains("unknown"))

        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(jokeListSizeBeforeRemoveCategory, viewModel.getJokeListTest().size)
        assertEquals(0, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(jokeListSizeBeforeRemoveCategory, viewModel.getJokeListTest().size)
        assertEquals(1, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(jokeListSizeBeforeRemoveCategory, viewModel.getJokeListTest().size)
        assertEquals(3, viewModel.getJokeIndex())
        assertEquals("teste2", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(2, viewModel.getCategoryIndexTest())
        assertEquals(jokeListSizeBeforeRemoveCategory, viewModel.getJokeListTest().size)
        assertEquals(4, viewModel.getJokeIndex())
        assertEquals("teste", viewModel.jokeLiveData.value?.category)

        viewModel.loadJoke()
        assertEquals(1, viewModel.getCategoryIndexTest())
        assertEquals(jokeListSizeBeforeRemoveCategory + 5, viewModel.getJokeListTest().size)
        assertEquals(5, viewModel.getJokeIndex())
        assertEquals("teste2", viewModel.jokeLiveData.value?.category)
        assertEquals("teste", viewModel.getJokeListTest()[viewModel.getJokeIndex() + 1].category)
        assertEquals("teste2", viewModel.getJokeListTest()[viewModel.getJokeIndex() + 2].category)
        assertEquals("teste", viewModel.getJokeListTest()[viewModel.getJokeIndex() + 3].category)
    }
}

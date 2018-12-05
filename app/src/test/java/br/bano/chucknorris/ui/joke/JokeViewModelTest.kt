package br.bano.chucknorris.ui.joke

import br.bano.chucknorris.data.joke.JokeRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class JokeViewModelTest(categories: List<String>? = null) : JokeViewModel() {

    override val jokeRemote: JokeRemote = JokeRemoteMock(categories)

    override fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job {
        return runBlocking {
            this.launch(block = block)
        }
    }

    fun getCategoriesFilteredTest() = categoriesFiltered
    fun getJokeListTest() = jokeList
    fun getJokeIndex() = index
    fun getCategoryIndexTest() = categoryIndex
}
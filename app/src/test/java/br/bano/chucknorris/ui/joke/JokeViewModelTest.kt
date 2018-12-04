package br.bano.chucknorris.ui.joke

import br.bano.chucknorris.data.joke.Joke
import br.bano.chucknorris.data.joke.JokeRemote
import kotlinx.coroutines.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*


class JokeViewModelTest(categories: List<String>? = null) : JokeViewModel() {

    override val jokeRemote: JokeRemote = mock(JokeRemote::class.java)

    init {
        val joke = Joke()
        joke.id = UUID.randomUUID().toString()
        joke.value = joke.id
        val deferred = CompletableDeferred(joke)
        `when`(jokeRemote.getJoke(getNextCategory())).thenReturn(deferred)

        if (categories != null) {
            val categoriesDeferred = CompletableDeferred(categories)
            `when`(jokeRemote.getCategories()).thenReturn(categoriesDeferred)
        }
    }

    override fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job {
        return runBlocking {
            this.launch(block = block)
        }
    }
}
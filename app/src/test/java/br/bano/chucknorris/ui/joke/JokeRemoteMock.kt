package br.bano.chucknorris.ui.joke

import br.bano.chucknorris.data.joke.Joke
import br.bano.chucknorris.data.joke.JokeRemote
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import java.util.*

class JokeRemoteMock(private val categories: List<String>? = null) : JokeRemote() {

    override fun getJoke(category: String?): Deferred<Joke> {
        val joke = Joke()
        joke.id = UUID.randomUUID().toString()
        joke.value = joke.id
        joke.category = if (category == null) category else listOf(category)
        return CompletableDeferred(joke)
    }

    override fun getCategories(): Deferred<List<String>> {
        return CompletableDeferred(categories ?: listOf())
    }
}
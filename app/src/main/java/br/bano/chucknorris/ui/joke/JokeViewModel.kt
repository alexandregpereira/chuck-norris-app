package br.bano.chucknorris.ui.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.bano.chucknorris.data.joke.Joke
import br.bano.chucknorris.data.joke.JokeRemote
import kotlinx.coroutines.*

class JokeViewModel : ViewModel() {

    private val jokeRemote = JokeRemote()
    val jokeLiveData = MutableLiveData<JokeUiData>()
    val loadingLiveData = MutableLiveData<Boolean>()
    private var index = 0
    private val jokeList = mutableListOf<JokeUiData>()

    private val rangeCallTotal = 4

    fun loadJoke(category: String? = null, previousJoke: Boolean = false) = CoroutineScope(Dispatchers.Main).launch {
        if (previousJoke) {
            if (index == 0) return@launch
            --index
            jokeLiveData.value = jokeList[index]
            return@launch
        }

        if (!jokeList.isEmpty()) ++index

        if (index > jokeList.size - 3) {
            if (jokeList.size > index) jokeLiveData.value = jokeList[index]
            loadingLiveData.value = true
            jokeList.addAll(loadNextJokes(category))
            loadingLiveData.value = false
            if (jokeList.size == rangeCallTotal + 1) jokeLiveData.value = jokeList[index]
            return@launch
        }

        jokeLiveData.value = jokeList[index]
    }

    private suspend fun loadNextJokes(category: String? = null): List<JokeUiData> {
        val jokes = mutableListOf<Deferred<Joke>>()
        for (i in 0..rangeCallTotal) jokes.add(jokeRemote.getJoke(category))
        return jokes.awaitAll().map { getJokeUiData(it) }
    }

    private suspend fun getJokeUiData(joke: Joke): JokeUiData = withContext(Dispatchers.IO) {
        val categories = joke.category
        val value = joke.value

        JokeUiData(
            joke.id ?: "",
            if (value != null) "\" $value" else "",
            categories?.joinToString()
        )
    }
}

package br.bano.chucknorris.ui.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.bano.chucknorris.data.joke.Joke
import br.bano.chucknorris.data.joke.JokeRemote
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

open class JokeViewModel : ViewModel() {

    private val unknownCategory = "unknown"
    private val rangeCallTotal = 4

    protected open val jokeRemote = JokeRemote()

    val jokeLiveData = MutableLiveData<JokeUiData>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val categoriesLiveData = MutableLiveData<List<String>>()
    val categoriesLoadingLiveData = MutableLiveData<Boolean>()

    private var index = 0
    private val jokeList = mutableListOf<JokeUiData>()
    private var categoryIndex = 0
    private val categoriesFiltered = mutableListOf(unknownCategory)

    fun loadJoke(previousJoke: Boolean = false) = getCoroutineScopeMain loadJokeLabel@ {
        if (previousJoke) {
            if (index == 0) return@loadJokeLabel
            --index
            jokeLiveData.value = jokeList[index]
            return@loadJokeLabel
        }

        if (categoriesLoadingLiveData.value == false && categoriesLiveData.value.isNullOrEmpty()) {
            loadCategories()
        }

        if (!jokeList.isEmpty() && index < jokeList.lastIndex) ++index

        if (jokeList.size > index) jokeLiveData.value = jokeList[index]

        loadNextJokes()
    }

    fun loadCategories() = getCoroutineScopeMain {
        if (categoriesLiveData.value != null) return@getCoroutineScopeMain

        categoriesLiveData.value = try {
            categoriesLoadingLiveData.value = true
            val categories = ArrayList(jokeRemote.getCategories().await())
            categories.add(0, unknownCategory)
            categories
        } catch (ex: Exception) {
            when(ex) {
                is UnknownHostException, is SocketTimeoutException, is ConnectException-> {
                    null
                }
                else -> throw ex
            }
        } finally {
            categoriesLoadingLiveData.value = false
        }
    }

    private suspend fun loadNextJokes(): Boolean {
        if (index >= jokeList.size - 2) {
            loadingLiveData.value = true
            jokeList.addAll(getNextJokes())

            val distinctList = jokeList.distinct()
            if (distinctList.size != jokeList.size) {
                jokeList.clear()
                jokeList.addAll(distinctList)
            }

            loadingLiveData.value = false
            val currentJoke = jokeLiveData.value
            if ((currentJoke == null || currentJoke.id.isEmpty()) && index <= jokeList.lastIndex) jokeLiveData.value = jokeList[index]
            return true
        }

        return false
    }

    private suspend fun getNextJokes(): List<JokeUiData> {
        val jokes = mutableListOf<Deferred<Joke>>()
        for (i in 0..rangeCallTotal) jokes.add(jokeRemote.getJoke(getNextCategory()))
        return jokes.map {
            try {
                it.await()
            } catch (ex: Exception) {
                when(ex) {
                    is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                        Joke()
                    }
                    else -> throw ex
                }
            }
        }.filter { it.id != null }.map { joke -> getJokeUiData(joke) }
    }

    protected fun getNextCategory(): String? {
        ++categoryIndex
        if (categoryIndex >= categoriesFiltered.size) categoryIndex = 0
        val category = categoriesFiltered[categoryIndex]
        return if (category == unknownCategory) null else category
    }

    private fun getJokeUiData(joke: Joke): JokeUiData {
        val categories = joke.category
        val value = joke.value

        return JokeUiData(
            joke.id ?: "",
            if (value != null) "\" $value" else "",
            categories?.joinToString() ?: unknownCategory
        )
    }

    private fun changeCategoryFilter(changeCategoriesFiltered: () -> Boolean) {
        if (!changeCategoriesFiltered()) return

        val currentJoke = jokeLiveData.value ?: return

        val newJokeList = jokeList.filter { categoriesFiltered.contains(it.category) }
        jokeList.clear()
        jokeList.addAll(newJokeList)

        this.index = if (jokeList.contains(currentJoke)) jokeList.indexOf(currentJoke) else 0

        when {
            jokeList.isNotEmpty() -> jokeLiveData.value = jokeList[index]
            else -> jokeLiveData.value = JokeUiData("", "", null)
        }

        getCoroutineScopeMain {
            loadNextJokes()
        }
    }

    fun addCategoryFilter(category: String) {
        changeCategoryFilter {
            val index = categoriesFiltered.indexOf(category)
            if (index == 0) return@changeCategoryFilter false

            if (index < 0) {
                categoriesFiltered.add(0, category)
            } else if (index != 0) {
                categoriesFiltered.removeAt(index)
                categoriesFiltered.add(0, category)
            }

            true
        }
    }

    fun removeCategoryFilter(category: String) {
        changeCategoryFilter {
            categoriesFiltered.remove(category)
            true
        }
    }

    fun isFirstJoke(jokeUiData: JokeUiData) = jokeList.isEmpty() || jokeUiData == jokeList.first()

    fun isLastJoke(jokeUiData: JokeUiData) = jokeList.isEmpty() || jokeUiData == jokeList.last()

    protected open fun getCoroutineScopeMain(block: suspend CoroutineScope.() -> Unit): Job = CoroutineScope(Dispatchers.Main).launch(block = block)
}

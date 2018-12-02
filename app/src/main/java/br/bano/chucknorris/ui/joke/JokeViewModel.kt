package br.bano.chucknorris.ui.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.bano.chucknorris.data.joke.JokeRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeViewModel : ViewModel() {

    private val jokeRemote = JokeRemote()
    val jokeLiveData = MutableLiveData<JokeUiData>()

    fun loadJoke(category: String? = null) = CoroutineScope(Dispatchers.Main).launch {
        jokeLiveData.value = getJoke(category)
    }

    private suspend fun getJoke(category: String?): JokeUiData = withContext(Dispatchers.Main) {
        val joke = jokeRemote.getJoke(category)
        val categories = joke.category
        val value = joke.value

        JokeUiData(
            joke.id ?: "",
            if (value != null) "\" $value" else "",
            categories?.joinToString()
        )
    }
}

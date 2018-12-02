package br.bano.chucknorris.ui.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class JokeViewModel : ViewModel() {

    val jokeLiveData = MutableLiveData<JokeUiData>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun loadJoke(category: String?) = CoroutineScope(Dispatchers.Main).launch {
        loadingLiveData.value = true
        jokeLiveData.value = getJoke()
        loadingLiveData.value = false
    }

    private suspend fun getJoke(): JokeUiData {
        return withContext(Dispatchers.Default) {
            delay(2000)

            JokeUiData(
                "id",
                "\" Chuck Norris was the Fourth Wiseman. He gave Baby Jesus the gift of Beard. Jesus loved it so much the other Wisemen were jealous of his obvious gift favoritism. They then used their combined influence to have Chuck Norris omitted from the Bible. All three later died mysteriously from roundhouse-kick related issues."
            )
        }
    }
}

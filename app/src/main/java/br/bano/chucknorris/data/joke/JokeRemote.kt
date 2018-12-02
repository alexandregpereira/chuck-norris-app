package br.bano.chucknorris.data.joke

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeRemote {

    private val service = Retrofit.Builder()
        .baseUrl("https://api.chucknorris.io")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<JokeApi>(JokeApi::class.java)

    suspend fun getJoke(category: String?): Joke {
        return service.getJoke(category).await()
    }
}
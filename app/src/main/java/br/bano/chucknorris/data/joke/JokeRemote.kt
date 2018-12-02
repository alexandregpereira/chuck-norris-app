package br.bano.chucknorris.data.joke

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeRemote {

    private val service = Retrofit.Builder()
        .baseUrl("https://api.chucknorris.io")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(buildClient())
        .build().create<JokeApi>(JokeApi::class.java)

    private fun buildClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)
        return httpClient.build()
    }

    fun getJoke(category: String?): Deferred<Joke> {
        return service.getJoke(category)
    }

    fun getCategories(): Deferred<List<String>> {
        return service.getCategories()
    }
}
package br.bano.chucknorris.data.joke

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface JokeApi {

    @GET("jokes/random")
    fun getJoke(@Query("category") category: String?): Deferred<Joke>

    @GET("jokes/categories")
    fun getCategories(): Deferred<List<String>>
}
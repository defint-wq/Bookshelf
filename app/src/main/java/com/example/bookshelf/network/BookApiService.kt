package com.example.bookshelf.network

import com.example.bookshelf.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/books/v1/"

private val json = Json {
    ignoreUnknownKeys = true
}

private val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter("key", BuildConfig.BOOKS_API_KEY)
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface BooksApiService {
    @GET("volumes")
    suspend fun getBooks(@Query("q") searchQuery: String): BookResponse
}

object BooksApi {
    val retrofitService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }
}
package com.utad.freetoplaylist.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FreeToPlayApi {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.freetogame.com/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: FreeToPlayService by lazy {
        retrofit.create(FreeToPlayService::class.java)
    }
}
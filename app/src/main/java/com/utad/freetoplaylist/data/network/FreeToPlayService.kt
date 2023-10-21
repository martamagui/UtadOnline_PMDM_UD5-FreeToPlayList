package com.utad.freetoplaylist.data.network

import com.utad.freetoplaylist.data.network.model.GameDetailResponse
import com.utad.freetoplaylist.data.network.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToPlayService {
    @GET("games")
    suspend fun getGames(): Response<List<GameResponse>>

    @GET("games")
    suspend fun getGamesByCategory(@Query("category") category: String): Response<List<GameResponse>>

    @GET("game")
    suspend fun getGameDetails(@Query("id") id: Int): Response<GameDetailResponse>
}
package com.utad.freetoplaywishlist.network

import com.utad.freetoplaywishlist.network.model.GameDetailResponse
import com.utad.freetoplaywishlist.network.model.GameResponse
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
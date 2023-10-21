package com.utad.freetoplaylist.data.network.repository

import com.utad.freetoplaylist.data.network.model.GameDetailResponse
import com.utad.freetoplaylist.data.network.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToPlayRepository {

    suspend fun getGames(): Response<List<GameResponse>>


    suspend fun getGamesByCategory(category: String): Response<List<GameResponse>>


    suspend fun getGameDetails(id: Int): Response<GameDetailResponse>

}


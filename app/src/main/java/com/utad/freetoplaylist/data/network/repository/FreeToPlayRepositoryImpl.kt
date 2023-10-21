package com.utad.freetoplaylist.data.network.repository

import com.utad.freetoplaylist.data.network.FreeToPlayApi
import com.utad.freetoplaylist.data.network.model.GameDetailResponse
import com.utad.freetoplaylist.data.network.model.GameResponse
import retrofit2.Response

class FreeToPlayRepositoryImpl: FreeToPlayRepository {
    override suspend fun getGames(): Response<List<GameResponse>> {
        return FreeToPlayApi.service.getGames()
    }

    override suspend fun getGamesByCategory(category: String): Response<List<GameResponse>> {
        return FreeToPlayApi.service.getGamesByCategory(category)
    }

    override suspend fun getGameDetails(id: Int): Response<GameDetailResponse> {
        return FreeToPlayApi.service.getGameDetails(id)
    }

}


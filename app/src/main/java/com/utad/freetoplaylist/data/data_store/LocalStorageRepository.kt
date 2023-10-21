package com.utad.freetoplaylist.data.data_store

interface LocalStorageRepository {

    suspend fun isUserLogged(): Boolean

    suspend fun setUserLogged(value: Boolean)

    suspend fun logOut()

}
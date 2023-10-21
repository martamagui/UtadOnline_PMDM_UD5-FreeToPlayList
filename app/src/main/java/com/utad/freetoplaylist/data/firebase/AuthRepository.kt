package com.utad.freetoplaylist.data.firebase

interface AuthRepository {

    suspend fun createUserFirebaseEmailAndPassword(email: String, password: String): Boolean

    suspend fun signInFirebaseEmailAndPassword(email: String, password: String): Boolean

    fun signOut()

}

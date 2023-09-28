package com.utad.freetoplaywishlist.firebase.realtime_database

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.utad.freetoplaywishlist.firebase.realtime_database.model.User
import kotlinx.coroutines.tasks.await

class RealTimeDatabaseManager {

    private val databaseReference = FirebaseDatabase.getInstance().reference

   suspend fun addUser(user: User): User? {
        val connection = databaseReference.child("user")
        val key = connection.push().key
        if (key != null) {
            val updatedUser = user.copy(key = key)
            connection.child("${updatedUser.email}").setValue(updatedUser).await()

            Log.d("addUser", "Usuario guardado")
            return updatedUser
        } else {
            Log.d("addUser", "Fallo al guardar ")
            return null
        }
    }

    fun deleteFavourite(email: String) {
        val connection = databaseReference.child("user")
        connection.child(email).removeValue()
    }

    suspend fun readUser(email: String): User? {
        val connection = databaseReference.child("user")
        val snapshot = connection.get()
        snapshot.await()

        snapshot.result.children.mapNotNull { dataSnapshot ->
            val user = dataSnapshot.getValue(User::class.java)

            val key = dataSnapshot.key
            if (key != null && user != null && user.email == email) {
                return user
            }
        }
        return null
    }

}

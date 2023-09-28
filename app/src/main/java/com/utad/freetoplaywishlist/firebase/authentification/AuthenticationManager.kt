package com.utad.freetoplaywishlist.firebase.authentification

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthenticationManager {

    private val auth = Firebase.auth

    fun isUserLogged(): Boolean {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("FirebaseEmailAndPassAuth", "usuario logeado")
        }
        return currentUser != null
    }

    suspend fun createUserFirebaseEmailAndPassword(email: String, password: String): Boolean {
        val result = auth.createUserWithEmailAndPassword(email, password)
        //Esperamos el resultado del registro
        result.await()
        if (result.isSuccessful) {
            Log.d("FirebaseAuth", "createUserFirebaseEmailAndPassword:success")
            return true
        } else {
            Log.d("FirebaseAuth", "createUserFirebaseEmailAndPassword:failure", result.exception)
            return false
        }
    }

    suspend fun signInFirebaseEmailAndPassword(email: String, password: String): Boolean {
        val result = auth.signInWithEmailAndPassword(email, password)
        //Esperamos el resulta del login
        result.await()
        if (result.isSuccessful) {
            Log.d("FirebaseAuth", "signInFirebaseEmailAndPassword:success")
            return true
        } else {
            Log.d("FirebaseAuth", "signInFirebaseEmailAndPassword:failure", result.exception)
            return false
        }
    }


    fun signOut() {
        auth.signOut()
    }


}
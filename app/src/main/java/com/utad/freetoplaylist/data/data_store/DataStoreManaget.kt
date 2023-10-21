package com.utad.freetoplaylist.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "FREE_TO_PLAY_DATA_STORE")

class DataStoreManager(private val context: Context) {

    private val isLoggedKey = "IS_LOGGED"

    private suspend fun putBoolean(key: String, value: Boolean) {
        context.dataStore.edit { editor ->
            editor[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun isUserLogged(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[booleanPreferencesKey(isLoggedKey)] ?: false
    }

    suspend fun setUserLogged(value: Boolean) {
        putBoolean(isLoggedKey, value)
    }

    suspend fun logOut() {
        context.dataStore.edit { editor -> editor.clear() }
    }

}
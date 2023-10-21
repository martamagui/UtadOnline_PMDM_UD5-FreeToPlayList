package com.utad.freetoplaylist.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "FREE_TO_PLAY_DATA_STORE")

class LocalStorageRepositoryImpl(private val context: Context) :LocalStorageRepository {

    private val isLoggedKey = "IS_LOGGED"

    private suspend fun putBoolean(key: String, value: Boolean) {
        context.dataStore.edit { editor ->
            editor[booleanPreferencesKey(key)] = value
        }
    }

    override suspend fun isUserLogged(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[booleanPreferencesKey(isLoggedKey)] ?: false
    }

    override suspend fun setUserLogged(value: Boolean) {
        putBoolean(isLoggedKey, value)
    }

    override suspend fun logOut() {
        context.dataStore.edit { editor -> editor.clear() }
    }

}
package com.example.minibus.dataStoreManager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.minibus.models.User
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_data")

class DataStoreManager(val context: Context) {

    suspend fun saveUserData(user: User) {

        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_NAME_KEY] = user.firstName
            preferences[PreferencesKeys.LAST_NAME_KEY] = user.lastName
            preferences[PreferencesKeys.PHONE_KEY] = user.phoneNumber
        }
    }

    fun getUserData() = context.dataStore.data
        .map { preferences ->
            return@map User(
                preferences[PreferencesKeys.PHONE_KEY] ?: "Терешкевич",
                preferences[PreferencesKeys.FIRST_NAME_KEY] ?: "Сергей",
                preferences[PreferencesKeys.LAST_NAME_KEY] ?: "Александрович"
            )

        }
}

object PreferencesKeys {
    val FIRST_NAME_KEY = stringPreferencesKey("first_name")
    val PHONE_KEY = stringPreferencesKey("phone")
    val LAST_NAME_KEY = stringPreferencesKey("last_name")
}
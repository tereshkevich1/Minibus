package com.example.minibus.dataStoreManager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.minibus.models.User
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_data")

class DataStoreManager(private val context: Context) {

    suspend fun updateUserData(firstName: String, lastName: String, phoneNumber: String) {
        context.dataStore.edit { preferences ->
            //preferences[PreferencesKeys.USER_ID] = 3
            preferences[PreferencesKeys.PHONE_KEY] = phoneNumber
            preferences[PreferencesKeys.FIRST_NAME_KEY] = firstName
            preferences[PreferencesKeys.LAST_NAME_KEY] = lastName
        }
        Log.d("DATaSTORE", "$firstName, $lastName")
        var name: String? = "хуй"
        context.dataStore.data.collect { s ->
            name = s[PreferencesKeys.FIRST_NAME_KEY]

        }
        Log.d("DATaSTORE", "вот и $name")
    }

    fun getUserData() = context.dataStore.data
        .map { preferences ->
            return@map User(
                preferences[PreferencesKeys.USER_ID] ?: 0,
                preferences[PreferencesKeys.FIRST_NAME_KEY] ?: "",
                preferences[PreferencesKeys.LAST_NAME_KEY] ?: "",
                preferences[PreferencesKeys.PHONE_KEY] ?: "",
                false,
                ""
            )
        }

    suspend fun saveUserData(user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = user.id
            preferences[PreferencesKeys.PHONE_KEY] = user.phoneNumber
            preferences[PreferencesKeys.FIRST_NAME_KEY] = user.firstName
            preferences[PreferencesKeys.LAST_NAME_KEY] = user.lastName
        }
    }

    suspend fun clearUserData(){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = 0
            preferences[PreferencesKeys.PHONE_KEY] = ""
            preferences[PreferencesKeys.FIRST_NAME_KEY] = ""
            preferences[PreferencesKeys.LAST_NAME_KEY] = ""
        }
    }


    fun check(): Boolean {
        var id: Int? = null
        context.dataStore.data.map { preferences ->
            id = preferences[PreferencesKeys.USER_ID]
        }
        return if (id != null) {
            id!! > 0
        } else false
    }

}

object PreferencesKeys {
    val USER_ID = intPreferencesKey("user_id")
    val FIRST_NAME_KEY = stringPreferencesKey("first_name")
    val PHONE_KEY = stringPreferencesKey("phone")
    val LAST_NAME_KEY = stringPreferencesKey("last_name")
}
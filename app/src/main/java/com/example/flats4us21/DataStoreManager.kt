package com.example.flats4us21

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flats4us21.data.dto.LoginResponse
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.internal.toLongOrDefault

val Context.dataStore by preferencesDataStore(name = "authentication")
private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
private val USER_EXPIRES_AT_KEY = stringPreferencesKey("user_expires_at")
private val USER_ROLE_KEY = stringPreferencesKey("user_role")

object DataStoreManager {

    private lateinit var dataStore: DataStore<Preferences>

    private val _userRole = MutableLiveData<String?>(null)
    val userRole: LiveData<String?>
        get() = _userRole

    fun initialize(context: Context) {
        dataStore = context.dataStore
    }
    suspend fun saveUserData(loginResponse: LoginResponse) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = loginResponse.token
            preferences[USER_EXPIRES_AT_KEY] = loginResponse.expiresAt.toString()
            preferences[USER_ROLE_KEY] = loginResponse.role
        }
        _userRole.value = loginResponse.role
    }

    suspend fun readUserData(): LoginResponse? {
        val preferences = dataStore.data.firstOrNull()
        return preferences?.let {
            val token = it[USER_TOKEN_KEY].toString()
            val expiresAt = it[USER_EXPIRES_AT_KEY]?.toLongOrDefault(0)
            val role = it[USER_ROLE_KEY].toString()
            LoginResponse(token, expiresAt!!, role)
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN_KEY)
            preferences.remove(USER_EXPIRES_AT_KEY)
            preferences.remove(USER_ROLE_KEY)
        }
        _userRole.postValue(null)
    }
}
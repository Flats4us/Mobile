package com.example.flats4us21

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flats4us21.data.dto.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.toLongOrDefault
import java.time.Instant

val Context.dataStore by preferencesDataStore("authentication")
private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
private val USER_EXPIRES_AT_KEY = stringPreferencesKey("user_expires_at")
private val USER_ROLE_KEY = stringPreferencesKey("user_role")
private val USER_VERIFICATION_STATUS_KEY = intPreferencesKey("verification_status")

object DataStoreManager {

    private lateinit var dataStore: DataStore<Preferences>
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _userRole = MutableLiveData<String?>(null)
    val userRole: LiveData<String?>
        get() = _userRole

    private val _tokenExpiresAt = MutableLiveData<Long?>(null)
    val tokenExpiresAt: LiveData<Long?>
        get() = _tokenExpiresAt

    fun initialize(context: Context) {
        dataStore = context.dataStore
        observeTokenExpiresAt()
    }

    fun isInitialized(): Boolean {
        return ::dataStore.isInitialized
    }

    private fun observeTokenExpiresAt() {
        scope.launch {
            dataStore.data.map { preferences ->
                preferences[USER_EXPIRES_AT_KEY]?.toLongOrDefault(0)
            }.collect {
                _tokenExpiresAt.postValue(it)
            }
        }
    }

    suspend fun saveUserData(loginResponse: LoginResponse) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = loginResponse.token
            preferences[USER_EXPIRES_AT_KEY] = loginResponse.expiresAt.toString()
            preferences[USER_ROLE_KEY] = loginResponse.role
            preferences[USER_VERIFICATION_STATUS_KEY] = loginResponse.verificationStatus
        }
        _userRole.postValue(loginResponse.role)
        _tokenExpiresAt.postValue(loginResponse.expiresAt)
    }

    suspend fun readUserData(): LoginResponse? {
        val preferences = dataStore.data.firstOrNull()
        return preferences?.let {
            val token = it[USER_TOKEN_KEY]
            val expiresAt = it[USER_EXPIRES_AT_KEY]?.toLongOrDefault(0) ?: 0
            val role = it[USER_ROLE_KEY].toString()
            val verificationStatus = it[USER_VERIFICATION_STATUS_KEY] ?: 0
            if(token != null) {
                LoginResponse(token, expiresAt, role, verificationStatus)
            } else {
                null
            }
        }
    }

    fun getToken() = dataStore.data.map {
        it[USER_TOKEN_KEY]
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        _userRole.postValue(null)
        _tokenExpiresAt.postValue(null)
    }

    suspend fun isUserTokenEmpty(): Boolean {
        val preferences = dataStore.data.firstOrNull()
        val token = preferences?.get(USER_TOKEN_KEY)
        return token.isNullOrEmpty()
    }

    suspend fun isTokenExpired(): Boolean {
        val currentTime = Instant.now().epochSecond
        val expiresAt = _tokenExpiresAt.value ?: 0
        return currentTime > expiresAt
    }
}

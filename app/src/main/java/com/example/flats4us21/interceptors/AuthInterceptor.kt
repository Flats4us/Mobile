package com.example.flats4us21.interceptors

import com.example.flats4us21.DataStoreManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getTokenFromDataStore()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }

    private fun getTokenFromDataStore(): String {
        var token: String
        runBlocking {
            token = DataStoreManager.readUserData()?.token ?: ""
        }
        return token
    }
}
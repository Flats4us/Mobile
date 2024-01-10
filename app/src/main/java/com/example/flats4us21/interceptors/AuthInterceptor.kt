package com.example.flats4us21.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val context = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getTokenFromDataStore()  // Implement this method to retrieve the token from DataStore
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }

    private fun getTokenFromDataStore(): String {
        // TODO: Implement retrieval of the token from the DataStore
        return ""
    }
}

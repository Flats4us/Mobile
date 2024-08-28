package com.example.flats4us.interceptors

import android.util.Log
import com.example.flats4us.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val token = DataStoreManager.getToken().first()
            Log.i("AuthInterceptor", token!!)
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            val response = chain.proceed(newRequest)
            response
        }
    }

}
package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Notification
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiNotificationDataSource"
class ApiNotificationDataSource : NotificationDataSource {


    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: NotificationService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NotificationService::class.java)
    }

    private val signalRNotificationService = SignalRNotificationService()

    fun startConnection() {
        signalRNotificationService.startConnection()
        Log.d(TAG, "Started connection")
    }

    fun stopConnection() {
        signalRNotificationService.stopConnection()
        Log.d(TAG, "Stopped connection")
    }

    override suspend fun getUnreadNotifications(): ApiResult<List<Notification>> {
        return try {
            val response = api.getUnreadNotifications()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getAllNotifications(): ApiResult<List<Notification>> {
        return try {
            val response = api.getAllNotifications()
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun markNotificationsAsRead(notificationIds: List<Int>): ApiResult<String> {
        return try {
            val response = api.markNotificationsAsRead(notificationIds)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("Response body is null")
                }
            } else {
                ApiResult.Error("Failed to fetch data: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    fun setOnReceiveNotificationCallback(callback: (String, String) -> Unit) {
        signalRNotificationService.setOnReceiveNotificationCallback(callback)
        Log.d(TAG, "Set receive private message callback")
    }

}
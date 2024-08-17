package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Notification
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiNotificationDataSource"
class ApiNotificationDataSource : NotificationDataSource {


    val gson: Gson = GsonBuilder()
        .setLenient()
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_unread_notifications")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_unread_notifications")
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
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_notifications")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_retrieve_notifications")
        }
    }

    override suspend fun markNotificationsAsRead(notificationIds: List<Int>): ApiResult<String> {
        return try {
            val jsonArray = JSONArray(notificationIds)
            val jsonObject = JSONObject()
            jsonObject.put("notificationIds", jsonArray)
            val requestBody : RequestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val response = api.markNotificationsAsRead(requestBody)
            if(response.isSuccessful) {
                ApiResult.Success("success_marked_notification_as_read")
            } else {
                ApiResult.Error("error_failed_to_mark_notification_as_read")
            }
        } catch (e: Exception) {
            ApiResult.Error("internal_error_failed_to_mark_notification_as_read")
        }
    }

    fun setOnReceiveNotificationCallback(callback: (String, String, String, Boolean, Int) -> Unit) {
        signalRNotificationService.setOnReceiveNotificationCallback(callback)
        Log.d(TAG, "Set receive private message callback")
    }

}
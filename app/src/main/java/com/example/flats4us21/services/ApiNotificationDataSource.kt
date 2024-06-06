package com.example.flats4us21.services

import com.example.flats4us21.URL
import com.example.flats4us21.data.Notification
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiNotificationDataSource : NotificationDataSource {


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

    override suspend fun getNotification(): List<Notification> {
        return api.getNotifications()
    }

    override suspend fun getNotification(notificationId: Int): Notification {
        return api.getNotifications(notificationId)
    }

}
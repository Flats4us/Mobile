package com.example.flats4us21.services

import com.example.flats4us21.data.Notification
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiNotificationDataSource : NotificationDataSource {

    private const val baseUrl = "https://raw.githubusercontent.com"

    val gson: Gson = GsonBuilder()
        .create()

    private val api: NotificationService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
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
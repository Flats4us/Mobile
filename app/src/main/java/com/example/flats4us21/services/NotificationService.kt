package com.example.flats4us21.services

import com.example.flats4us21.data.Notification
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationService {
    @GET("/s22677/JSON-data-example/main/Notification/Notification.json")
    suspend fun getNotifications() : List<Notification>

    @GET("/s22677/JSON-data-example/main/Notification/{id}/Notification.json")
    suspend fun getNotifications(@Path("id")notificationId: Int) : Notification
}
package com.example.flats4us.services

import com.example.flats4us.data.Notification
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationService {
    @GET("api/notifications/unread")
    suspend fun getUnreadNotifications() : Response<List<Notification>>

    @GET("api/notifications/all")
    suspend fun getAllNotifications() : Response<List<Notification>>

    @POST("api/notifications/read")
    suspend fun markNotificationsAsRead(@Body notificationIds: RequestBody) : Response<Void>

}
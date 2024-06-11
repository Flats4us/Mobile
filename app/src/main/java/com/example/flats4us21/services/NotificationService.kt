package com.example.flats4us21.services

import com.example.flats4us21.data.Notification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationService {
    @POST("api/Notification")
    suspend fun getNotifications(@Query("userId") userId: Int, @Body notification: Notification) : Response<List<Notification>>

}
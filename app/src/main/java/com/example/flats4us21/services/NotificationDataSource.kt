package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Notification

interface NotificationDataSource {
    suspend fun getNotification(userId: Int, notification: Notification) : ApiResult<List<Notification>>
}
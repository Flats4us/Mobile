package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Notification

interface NotificationDataSource {
    suspend fun getUnreadNotifications() : ApiResult<List<Notification>>

    suspend fun getAllNotifications() : ApiResult<List<Notification>>

    suspend fun markNotificationsAsRead(notificationIds: List<Int>) : ApiResult<String>
}
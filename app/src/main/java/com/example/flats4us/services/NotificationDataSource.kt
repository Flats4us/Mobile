package com.example.flats4us.services

import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Notification

interface NotificationDataSource {
    suspend fun getUnreadNotifications() : ApiResult<List<Notification>>

    suspend fun getAllNotifications() : ApiResult<List<Notification>>

    suspend fun markNotificationsAsRead(notificationIds: List<Int>) : ApiResult<String>
}
package com.example.flats4us21.services

import com.example.flats4us21.data.Notification

interface NotificationDataSource {
    suspend fun getNotification() : List<Notification>

    suspend fun getNotification(notificationId : Int) : Notification
}
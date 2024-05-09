package com.example.flats4us21.data

data class Notification(
    val notificationId : Int,
    val title : String,
    val description : String,
    val time : String,
    val isRead : Boolean
)

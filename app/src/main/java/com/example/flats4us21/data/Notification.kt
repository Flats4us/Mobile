package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("notificationId")
    val notificationId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("read")
    var read: Boolean,
    @SerializedName("isChat")
    val isChat: Boolean
)
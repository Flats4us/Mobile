package com.example.flats4us21.data


import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("chatId")
    val chatId: Int,
    @SerializedName("otherUserId")
    val otherUserId: Int,
    @SerializedName("otherUsername")
    val otherUsername: String
)
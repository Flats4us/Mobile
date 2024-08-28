    package com.example.flats4us.data


    import com.google.gson.annotations.SerializedName

    data class ChatMessage(
        @SerializedName("chatMessageId")
        val chatMessageId: Int,
        @SerializedName("content")
        val content: String,
        @SerializedName("dateTime")
        val dateTime: String,
        @SerializedName("senderId")
        val senderId: Int
    )
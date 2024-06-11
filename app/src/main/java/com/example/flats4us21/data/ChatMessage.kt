    package com.example.flats4us21.data


    import com.google.gson.annotations.SerializedName

    data class ChatMessage(
        @SerializedName("chatMessageId")
        val chatMessageId: Int,
        @SerializedName("content")
        val content: String,
        @SerializedName("dateTime")
        val dateTime: String,
        @SerializedName("senderId")
        val senderId: Int,
        @SerializedName("groupChatId")
        val groupChatId: Any?,
        @SerializedName("chatId")
        val chatId: Int,
        @SerializedName("groupChat")
        val groupChat: Any?,
        @SerializedName("chat")
        val chat: Any?
    )
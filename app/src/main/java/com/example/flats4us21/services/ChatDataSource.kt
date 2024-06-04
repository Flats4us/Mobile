package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage

interface ChatDataSource {
    suspend fun sendMessage(receiverUserId: Int, message: String): ApiResult<Boolean>

    suspend fun getChatHistory(chatId: Int): ApiResult<List<ChatMessage>>

    suspend fun getChatParticipants(chatId: Int): ApiResult<Int>

    suspend fun getUserChats(): ApiResult<List<Chat>>
}
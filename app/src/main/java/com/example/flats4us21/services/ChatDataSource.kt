package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.data.GroupChatInfo
import retrofit2.http.Path

interface ChatDataSource {
    suspend fun sendMessage(receiverUserId: Int, message: String): ApiResult<Boolean>

    suspend fun sendGroupMessage(groupChatId: Int, message: String): ApiResult<Boolean>

    suspend fun getChatHistory(chatId: Int): ApiResult<List<ChatMessage>>

    suspend fun getChatParticipants(chatId: Int): ApiResult<Int>

    suspend fun getUserChats(): ApiResult<List<Chat>>

    suspend fun getGroupChatInfo(chatId: Int): ApiResult<GroupChatInfo>

    suspend fun getGroupChatHistory(@Path("groupChatId") chatId: Int): ApiResult<List<ChatMessage>>
}
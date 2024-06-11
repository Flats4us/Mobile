package com.example.flats4us21.services

import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ApiChatDataSource : ChatDataSource {

    private val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val chatRepository = ChatRepository(okHttpClient)

    init {
        chatRepository.startConnection()
    }

    override suspend fun sendMessage(receiverUserId: Int, message: String): ApiResult<Boolean> {
        return try {
            chatRepository.sendPrivateMessage(receiverUserId, message)
            ApiResult.Success(true)
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getChatHistory(chatId: Int): ApiResult<List<ChatMessage>> {
        return try {
            val messages = chatRepository.getMessages(chatId.toString())
            ApiResult.Success(messages)
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getChatParticipants(chatId: Int): ApiResult<Int> {
        return try {
            val participantId = chatRepository.getParticipantId(chatId.toString())
            ApiResult.Success(participantId.toInt())
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    override suspend fun getUserChats(): ApiResult<List<Chat>> {
        return try {
            val chats = chatRepository.getConversations()
            ApiResult.Success(chats)
        } catch (e: Exception) {
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }
}

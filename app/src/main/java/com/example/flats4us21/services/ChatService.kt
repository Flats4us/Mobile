package com.example.flats4us21.services

import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatService {

    @POST("api/Chat/sendmessage")
    suspend fun sendMessage(@Query("receiverUserId") receiverUserId: Int, @Query("message") message: String): Response<Void>

    @GET("api/Chat/history/{chatId}")
    suspend fun getChatHistory(@Path("chatId") chatId: Int): Response<List<ChatMessage>>

    @GET("api/Chat/participant/{chatId}")
    suspend fun getChatParticipants(@Path("chatId") chatId: Int): Response<Int>

    @GET("api/Chat/user/chats")
    suspend fun getUserChats(): Response<List<Chat>>
}
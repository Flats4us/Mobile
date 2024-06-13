    package com.example.flats4us21.services

    import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
    import com.example.flats4us21.data.NewPropertyApiResponse
    import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

    interface ChatService {
        @GET("api/chats/{chatId}/history")
        suspend fun getChatHistory(@Path("chatId") chatId: Int): Response<List<ChatMessage>>

        @GET("api/chats/{chatId}/participant")
        suspend fun getChatParticipants(@Path("chatId") chatId: Int): Response<NewPropertyApiResponse<Int>>

        @GET("api/chats/user")
        suspend fun getUserChats(): Response<List<Chat>>
    }
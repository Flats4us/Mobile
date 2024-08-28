    package com.example.flats4us.services

    import com.example.flats4us.data.Chat
import com.example.flats4us.data.ChatMessage
    import com.example.flats4us.data.GroupChatInfo
    import com.example.flats4us.data.NewPropertyApiResponse
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

        @GET("api/group-chats/{groupChatId}")
        suspend fun getGroupChatInfo(@Path("groupChatId") chatId: Int): Response<GroupChatInfo>

        @GET("api/group-chats/{groupChatId}/history")
        suspend fun getGroupChatHistory(@Path("groupChatId") chatId: Int): Response<List<ChatMessage>>
    }
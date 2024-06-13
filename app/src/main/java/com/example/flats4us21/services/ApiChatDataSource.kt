package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.URL
import com.example.flats4us21.data.ApiResult
import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
import com.example.flats4us21.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiChatDataSource {

    val gson: Gson = GsonBuilder()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: ChatService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ChatService::class.java)
    }

    internal val chatRepository = ChatRepository()

    fun startConnection() {
        chatRepository.startConnection()
        Log.d("ApiChatDataSource", "Started connection")
    }

    fun stopConnection() {
        chatRepository.stopConnection()
        Log.d("ApiChatDataSource", "Stopped connection")
    }

    suspend fun sendMessage(receiverUserId: Int, message: String): ApiResult<Boolean> {
        return try {
            chatRepository.sendMessage(receiverUserId, message)
            Log.d("ApiChatDataSource", "Message sent to $receiverUserId: $message")
            ApiResult.Success(true)
        } catch (e: Exception) {
            Log.e("ApiChatDataSource", "Error sending message: ${e.message}")
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    suspend fun getChatHistory(chatId: Int): ApiResult<List<ChatMessage>> {
        return try {
            val response = api.getChatHistory(chatId)
            if(response.isSuccessful) {
                val data = response.body()
                Log.d("ApiChatDataSource", "Received chat history for chatId: $chatId")
                if(data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("No data found")
                }
            } else {
                ApiResult.Error("An error occurred: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ApiChatDataSource", "Error getting chat history: ${e.message}")
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    suspend fun getChatParticipants(chatId: Int): ApiResult<Int> {
        return try {
            val response = api.getChatParticipants(chatId)
            if(response.isSuccessful) {
                val data = response.body()
                Log.d("ApiChatDataSource", "Received chat participants for chatId: $chatId")
                if(data != null) {
                    ApiResult.Success(data.result)
                } else {
                    ApiResult.Error("No data found")
                }
            } else {
                ApiResult.Error("An error occurred: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ApiChatDataSource", "Error getting chat participants: ${e.message}")
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    suspend fun getUserChats(): ApiResult<List<Chat>> {
        return try {
            val response = api.getUserChats()
            if(response.isSuccessful) {
                val data = response.body()
                Log.d("ApiChatDataSource", "Received user chats")
                if(data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("No data found")
                }
            } else {
                ApiResult.Error("An error occurred: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ApiChatDataSource", "Error getting user chats: ${e.message}")
            ApiResult.Error("An internal error occurred: ${e.message}")
        }
    }

    fun setOnReceivePrivateMessageCallback(callback: (Int, String, String) -> Unit) {
        chatRepository.setOnReceivePrivateMessageCallback(callback)
        Log.d("ApiChatDataSource", "Set receive private message callback")
    }
}
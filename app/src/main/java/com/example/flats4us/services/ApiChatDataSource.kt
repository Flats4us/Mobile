package com.example.flats4us.services

import android.util.Log
import com.example.flats4us.URL
import com.example.flats4us.data.ApiResult
import com.example.flats4us.data.Chat
import com.example.flats4us.data.ChatMessage
import com.example.flats4us.data.GroupChatInfo
import com.example.flats4us.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ApiChatDataSource"
class ApiChatDataSource :ChatDataSource {

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

    private val signalRChatService = SignalRChatService()

    fun startConnection() {
        signalRChatService.startConnection()
        Log.d(TAG, "Started connection")
    }

    fun stopConnection() {
        signalRChatService.stopConnection()
        Log.d(TAG, "Stopped connection")
    }

    override suspend fun sendMessage(receiverUserId: Int, message: String): ApiResult<Boolean> {
        return try {
            signalRChatService.sendMessage(receiverUserId, message)
            Log.d(TAG, "Message sent to $receiverUserId: $message")
            ApiResult.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            ApiResult.Error("internal_error_sending_message")
        }
    }

    override suspend fun sendGroupMessage(groupChatId: Int, message: String): ApiResult<Boolean> {
        return try {
            signalRChatService.sendGroupMessage(groupChatId, message)
            Log.d(TAG, "Message sent to $groupChatId: $message")
            ApiResult.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            ApiResult.Error("internal_error_sending_group_message")
        }
    }

    override suspend fun getChatHistory(chatId: Int): ApiResult<List<ChatMessage>> {
        return try {
            val response = api.getChatHistory(chatId)
            if(response.isSuccessful) {
                val data = response.body()
                Log.d(TAG, "Received chat history for chatId: $chatId")
                if(data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_chat_history")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting chat history: ${e.message}")
            ApiResult.Error("internal_error_failed_to_retrieve_chat_history")
        }
    }

    override suspend fun getChatParticipants(chatId: Int): ApiResult<Int> {
        return try {
            val response = api.getChatParticipants(chatId)
            if(response.isSuccessful) {
                val data = response.body()
                if(data != null) {
                    ApiResult.Success(data.result)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_chat_participants")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting chat participants: ${e.message}")
            ApiResult.Error("internal_error_failed_to_retrieve_chat_participants")
        }
    }

    override suspend fun getUserChats(): ApiResult<List<Chat>> {
        return try {
            val response = api.getUserChats()
            if(response.isSuccessful) {
                val data = response.body()
                if(data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_user_chats")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user chats: ${e.message}")
            ApiResult.Error("internal_error_failed_to_retrieve_user_chats")
        }
    }

    override suspend fun getGroupChatInfo(chatId: Int): ApiResult<GroupChatInfo> {
        return try {
            val response = api.getGroupChatInfo(chatId)
            if(response.isSuccessful) {
                val data = response.body()
                Log.d(TAG, "Received group chat info for chatId: $chatId: $data")
                if(data != null) {
                    Log.d("ApiChatDataSource", "Group chat info: $data")
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_group_chat_info")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting group chat info: ${e.message}")
            ApiResult.Error("internal_error_failed_to_retrieve_group_chat_info")
        }
    }

    override suspend fun getGroupChatHistory(chatId: Int): ApiResult<List<ChatMessage>> {
        return try {
            val response = api.getGroupChatHistory(chatId)
            if(response.isSuccessful) {
                val data = response.body()
                Log.d(TAG, "Received group chat history for chatId: $chatId")
                if(data != null) {
                    ApiResult.Success(data)
                } else {
                    ApiResult.Error("error_empty_body")
                }
            } else {
                ApiResult.Error("error_failed_to_retrieve_group_chat_history")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting group chat history: ${e.message}")
            ApiResult.Error("internal_error_failed_to_retrieve_group_chat_history")
        }
    }

    fun setOnReceivePrivateMessageCallback(callback: (Int, String, String) -> Unit) {
        signalRChatService.setOnReceivePrivateMessageCallback(callback)
        Log.d(TAG, "Set receive private message callback")
    }

    fun setOnReceiveGroupMessageCallback(callback: (Int, Int, String, String) -> Unit) {
        signalRChatService.setOnReceiveGroupMessageCallback(callback)
        Log.d(TAG, "Set receive group message callback")
    }
}
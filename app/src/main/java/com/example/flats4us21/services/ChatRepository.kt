package com.example.flats4us21.services

import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.data.Chat
import com.example.flats4us21.data.ChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ChatRepository(private val httpClient: OkHttpClient) {

    private val apiRoute = "http://172.21.40.120:5166/api/chat"
    private val baseRoute = "http://172.21.40.120:5166"
    private lateinit var hubConnection: HubConnection
    private val onReceivePrivateMessageCallbacks: MutableList<(Int, String) -> Unit> = mutableListOf()
    private val gson = Gson()

    fun startConnection() {
        stopConnection()
        hubConnection = HubConnectionBuilder.create("$baseRoute/chatHub")
            .withAccessTokenProvider(getTokenFromDataStore())
            .build()

        hubConnection.start().blockingAwait()
        registerEventHandlers()
    }

    private fun registerEventHandlers() {
        onReceivePrivateMessageCallbacks.forEach { callback ->
            hubConnection.on("ReceivePrivateMessage", { user: Int, message: String ->
                callback(user, message)
            }, Int::class.java, String::class.java)
        }
    }

    fun addReceivePrivateMessageHandler(callback: (Int, String) -> Unit) {
        onReceivePrivateMessageCallbacks.add(callback)
    }

    fun sendPrivateMessage(receiverId: Int, message: String) {
        hubConnection.send("SendPrivateMessage", receiverId, message)
    }

    fun sendMessage(user: String, message: String) {
        hubConnection.send("SendMessage", user, message)
    }

    fun isConnected(): Boolean {
        return this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED
    }

    fun stopConnection() {
        if (isConnected()) {
            hubConnection.stop()
        }
    }

    private fun getTokenFromDataStore(): Single<String> {
        return Single.fromCallable {
            var token = ""
            runBlocking {
                token = DataStoreManager.readUserData()?.token ?: ""
            }
            if (token.isNotEmpty()) {
                token
            } else {
                throw Throwable("Token not found")
            }
        }
    }

    suspend fun getConversations(): List<Chat> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("$apiRoute/user/chats")
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonResponse = response.body?.string() ?: ""
                val listType = object : TypeToken<List<Chat>>() {}.type
                gson.fromJson(jsonResponse, listType)
            }
        }
    }

    suspend fun getMessages(chatId: String): List<ChatMessage> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("$apiRoute/history/$chatId")
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonResponse = response.body?.string() ?: ""
                val listType = object : TypeToken<List<ChatMessage>>() {}.type
                gson.fromJson(jsonResponse, listType)
            }
        }
    }

    suspend fun getParticipantId(chatId: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("$apiRoute/participant/$chatId")
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.string() ?: ""
            }
        }
    }
}

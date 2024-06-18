package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.URL
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking

class SignalRChatService {

    private lateinit var hubConnection: HubConnection
    private val onReceivePrivateMessageCallbacks: MutableList<(Int, String, String) -> Unit> = mutableListOf()
    private val onReceiveGroupMessageCallbacks: MutableList<(Int, Int, String, String) -> Unit> = mutableListOf()

    fun startConnection() {
        if (::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED) {
            Log.d("SignalR", "Already connected")
            return
        }

        hubConnection = HubConnectionBuilder.create("$URL/chatHub")
            .withAccessTokenProvider(getTokenFromDataStore())
            .build()

        hubConnection.start()
            .doOnComplete {
                Log.d("SignalR", "Hub connection started: ${hubConnection.connectionState}")
                registerEventHandlers()
            }
            .doOnError { error ->
                Log.e("SignalR", "Error starting connection: ${error.message}")
            }
            .blockingAwait()
    }

    private fun registerEventHandlers() {
        hubConnection.on("ReceivePrivateMessage", { userId: Int, message: String, date: String ->
            Log.d("SignalR", "Received message from $userId: $message")
            onReceivePrivateMessage(userId, message, date)
        }, Int::class.java, String::class.java, String::class.java)

        hubConnection.on("ReceiveGroupMessage", { groupChatId: Int, senderId: Int, message: String, date: String ->
            Log.d("SignalR", "Received message from group chat $groupChatId: $message")
            onReceiveGroupMessage(groupChatId, senderId, message, date)
        }, Int::class.java, Int::class.java, String::class.java, String::class.java)
    }

    private var onReceivePrivateMessageCallback: ((Int, String, String) -> Unit)? = null

    private var onReceiveGroupMessageCallback: ((Int, Int, String, String) -> Unit)? = null

    fun setOnReceivePrivateMessageCallback(callback: (Int, String, String) -> Unit) {
        onReceivePrivateMessageCallback = callback
        Log.d("ChatRepository", "Set receive private message callback")
    }

    fun setOnReceivePrivateMessageCallback(callback: (Int, Int, String, String) -> Unit) {
        onReceiveGroupMessageCallback = callback
        Log.d("ChatRepository", "Set receive private message callback")
    }

    private fun onReceivePrivateMessage(userId: Int, message: String, date: String) {
        Log.d("SignalR", "Handling message from $userId: $message")
        onReceivePrivateMessageCallback?.invoke(userId, message, date)
        onReceivePrivateMessageCallbacks.forEach { it.invoke(userId, message, date) }
    }

    private fun onReceiveGroupMessage(groupChatId: Int, senderId: Int, message: String, date: String) {
        Log.d("SignalR", "Handling message from $groupChatId: $message")
        onReceiveGroupMessageCallback?.invoke(groupChatId, senderId, message, date)
        onReceiveGroupMessageCallbacks.forEach { it.invoke(groupChatId, senderId, message, date) }
    }

    fun sendMessage(receiverId: Int, message: String) {
        if (isConnected()) {
            hubConnection.send("SendPrivateMessage", receiverId, message)
            Log.d("SignalR", "Message sent to $receiverId: $message")
        } else {
            Log.d("SignalR", "Cannot send message, not connected")
        }
    }

    fun sendGroupMessage(groupChatId: Int, message: String) {
        if (isConnected()) {
            hubConnection.send("SendGroupChatMessage", groupChatId, message)
            Log.d("SignalR", "Message sent to $groupChatId: $message")
        } else {
            Log.d("SignalR", "Cannot send message, not connected")
        }
    }

    private fun isConnected(): Boolean {
        val isConnected = this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED
        Log.d("SignalR", "Connection state: $isConnected")
        return isConnected
    }

    fun stopConnection() {
        if (isConnected()) {
            hubConnection.stop()
            Log.d("SignalR", "Hub connection stopped")
        }
    }

    private fun getTokenFromDataStore(): Single<String> {
        return Single.fromCallable {
            var token = ""
            runBlocking {
                token = DataStoreManager.readUserData()?.token ?: ""
            }
            Log.d("SignalR", "Retrieved token: $token")
            if (token.isNotEmpty()) {
                token
            } else {
                throw Throwable("Token not found")
            }
        }
    }
}

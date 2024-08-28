package com.example.flats4us.services

import android.util.Log
import com.example.flats4us.DataStoreManager
import com.example.flats4us.URL
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking

private const val TAG = "SignalRChatService"
class SignalRChatService {
    private lateinit var hubConnection: HubConnection
    private val onReceivePrivateMessageCallbacks: MutableList<(Int, String, String) -> Unit> = mutableListOf()
    private val onReceiveGroupMessageCallbacks: MutableList<(Int, Int, String, String) -> Unit> = mutableListOf()
    private var onReceivePrivateMessageCallback: ((Int, String, String) -> Unit)? = null
    private var onReceiveGroupMessageCallback: ((Int, Int, String, String) -> Unit)? = null

    fun startConnection() {
        if (::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED) {
            Log.d(TAG, "Already connected")
            return
        }
        hubConnection = HubConnectionBuilder.create("$URL/chatHub")
            .withAccessTokenProvider(getTokenFromDataStore())
            .build()
        registerEventHandlers()
        hubConnection.start()
            .blockingAwait()
    }

    private fun registerEventHandlers() {
        hubConnection.on("ReceivePrivateMessage", { userId: Int, message: String, date: String ->
            onReceivePrivateMessage(userId, message, date)
        }, Int::class.java, String::class.java, String::class.java)

        hubConnection.on("ReceiveGroupMessage", { groupChatId: Int, senderId: Int, message: String, date: String ->
            onReceiveGroupMessage(groupChatId, senderId, message, date)
        }, Int::class.java, Int::class.java, String::class.java, String::class.java)
    }

    fun setOnReceivePrivateMessageCallback(callback: (Int, String, String) -> Unit) {
        onReceivePrivateMessageCallback = callback
        Log.d(TAG, "Set receive private message callback")
    }

    fun setOnReceiveGroupMessageCallback(callback: (Int, Int, String, String) -> Unit) {
        onReceiveGroupMessageCallback = callback
        Log.d(TAG, "Set receive group message callback")
    }

    private fun onReceivePrivateMessage(userId: Int, message: String, date: String) {
        Log.d(TAG, "Message from $userId: $message")
        onReceivePrivateMessageCallback?.invoke(userId, message, date)
        onReceivePrivateMessageCallbacks.forEach { it.invoke(userId, message, date) }
    }

    private fun onReceiveGroupMessage(groupChatId: Int, senderId: Int, message: String, date: String) {
        Log.d(TAG, "Message from $groupChatId: $message")
        onReceiveGroupMessageCallback?.invoke(groupChatId, senderId, message, date)
        onReceiveGroupMessageCallbacks.forEach { it.invoke(groupChatId, senderId, message, date) }
    }

    fun sendMessage(receiverId: Int, message: String) {
        if (isConnected()) {
            hubConnection.send("SendPrivateMessage", receiverId, message)
            Log.d(TAG, "Message sent to $receiverId: $message")
        } else {
            Log.d(TAG, "Not connected")
        }
    }

    fun sendGroupMessage(groupChatId: Int, message: String) {
        if (isConnected()) {
            hubConnection.send("SendGroupChatMessage", groupChatId, message)
            Log.d(TAG, "Message sent to $groupChatId: $message")
        } else {
            Log.d(TAG, "Not connected")
        }
    }

    private fun isConnected(): Boolean {
        val isConnected = ::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED
        Log.d(TAG, "Connection: $isConnected")
        return isConnected
    }

    fun stopConnection() {
        if (isConnected()) {
            hubConnection.stop()
            Log.d(TAG, "Stopped hub connection")
        }
    }

    private fun getTokenFromDataStore(): Single<String> {
        return Single.fromCallable {
            var token = ""
            runBlocking {
                token = DataStoreManager.readUserData()?.token ?: ""
            }
            Log.d(TAG, "Retrieved token: $token")
            if (token.isNotEmpty()) {
                token
            } else {
                throw Throwable("Token not found")
            }
        }
    }
}

package com.example.flats4us21.services

import android.util.Log
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.URL
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking

private const val TAG = "SignalRService"
class SignalRNotificationService {

    private lateinit var hubConnection: HubConnection
    private val onReceiveNotificationCallbacks: MutableList<(String, String, String, Boolean, Int) -> Unit> = mutableListOf()

    fun startConnection() {
        if (::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED) {
            Log.d("SignalR", "Already connected")
            return
        }

        hubConnection = HubConnectionBuilder.create("$URL/notificationHub")
            .withAccessTokenProvider(getTokenFromDataStore())
            .build()
        registerEventHandlers()
        hubConnection.start()
            .blockingAwait()
    }

    private fun registerEventHandlers() {
        hubConnection.on("ReceiveNotification", { title: String, body: String, date: String, isChat: Boolean, id: Int ->
            Log.d("SignalR", "Received message from group chat $title: $body")
            onReceiveNotification(title, body, date, isChat, id)
        }, String::class.java, String::class.java, String::class.java, Boolean::class.java, Int::class.java)
    }

    private var onReceiveNotificationCallback: ((String, String, String, Boolean, Int) -> Unit)? = null

    fun setOnReceiveNotificationCallback(callback: (String, String, String, Boolean, Int) -> Unit) {
        onReceiveNotificationCallback = callback
        Log.d(TAG, "Set receive private message callback")
    }

    private fun onReceiveNotification(
        title: String,
        body: String,
        date: String,
        isChat: Boolean,
        id: Int
    ) {
        onReceiveNotificationCallback?.invoke(title, body, date, isChat, id)
        onReceiveNotificationCallbacks.forEach { it.invoke(title, body, date, isChat, id) }
    }

    private fun isConnected(): Boolean {
        val isConnected = this::hubConnection.isInitialized && hubConnection.connectionState == HubConnectionState.CONNECTED
        Log.d("SignalR", "Connection state: $isConnected")
        return isConnected
    }

    fun stopConnection() {
        if (isConnected()) {
            hubConnection.stop().blockingAwait()
            Log.d("SignalR", "Hub connection stopped: ${hubConnection.connectionState}")
        }
    }

    private fun getTokenFromDataStore(): Single<String> {
        return Single.fromCallable {
            var token = ""
            runBlocking {
                token = DataStoreManager.readUserData()?.token ?: ""
            }
            Log.d(TAG, "Pobrany token: $token")
            if (token.isNotEmpty()) {
                token
            } else {
                throw Throwable("Nie znaleziono tokenu")
            }
        }
    }
}

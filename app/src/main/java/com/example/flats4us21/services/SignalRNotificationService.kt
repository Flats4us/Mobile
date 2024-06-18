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
    private val onReceiveNotificationCallbacks: MutableList<(String, String) -> Unit> = mutableListOf()

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
        hubConnection.on("ReceiveGroupMessage", { title: String, body: String ->
            Log.d("SignalR", "Received message from group chat $title: $body")
            onReceiveNotification(title, body)
        }, String::class.java, String::class.java)
    }

    private var onReceiveNotificationCallback: ((String, String) -> Unit)? = null

    fun setOnReceiveNotificationCallback(callback: (String, String) -> Unit) {
        onReceiveNotificationCallback = callback
        Log.d(TAG, "Set receive private message callback")
    }

    private fun onReceiveNotification(title: String, body: String) {
        onReceiveNotificationCallback?.invoke(title, body)
        onReceiveNotificationCallbacks.forEach { it.invoke(title, body) }
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
            Log.d(TAG, "Pobrany token: $token")
            if (token.isNotEmpty()) {
                token
            } else {
                throw Throwable("Nie znaleziono tokenu")
            }
        }
    }
}

package com.example.flats4us21.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.flats4us21.URL
import com.example.flats4us21.data.Notification
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder

class SignalRService {

    private lateinit var hubConnection: HubConnection
    val notifications = MutableLiveData<Notification>()

    fun startConnection() {
        hubConnection = HubConnectionBuilder.create(URL+"/api/Notification").build()

        hubConnection.on("ReceiveNotification", { notification: Notification ->
            // Obsługa odebranego powiadomienia
            Log.d("SignalRService", "Received notification: $notification")
            notifications.postValue(notification)
        }, Notification::class.java)

        hubConnection.start().blockingAwait()
    }

    fun stopConnection() {
        hubConnection.stop().blockingAwait()
    }
}

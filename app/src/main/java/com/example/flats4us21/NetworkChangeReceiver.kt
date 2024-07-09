package com.example.flats4us21

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.flats4us21.data.utils.NetworkUtils

class NetworkChangeReceiver(private val listener: NetworkChangeListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.extras != null) {
            val isConnected = NetworkUtils.isNetworkConnected(context)
            listener.onNetworkChanged(isConnected)
        }
    }

    interface NetworkChangeListener {
        fun onNetworkChanged(isConnected: Boolean)
    }
}

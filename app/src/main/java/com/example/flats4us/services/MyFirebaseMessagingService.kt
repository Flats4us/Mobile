package com.example.flats4us.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.flats4us.DrawerActivity
import com.example.flats4us.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "com.example.flats4us"
private const val TAG = "DrawerActivity"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: ${message.from}")

        if (message.data.isNotEmpty()) {
            val data = message .data
            val title = data["title"] ?: "Default title"
            val body = data["body"] ?: "Default message"

            val translatedTitle = translateNotification(title)
            val translatedBody = translateNotification(body)
            generateNotification(translatedTitle, translatedBody)
        }

        if (message.notification != null) {
            Log.d(TAG, "Message Notification Body: ${message.notification!!.body}")

            val title = translateNotification(message.notification!!.title!!)
            val body = translateNotification(message.notification!!.body!!)
            generateNotification(title, body)
        } else {
            Log.d(TAG, "test")
        }
    }

    private fun translateNotification(resourceString: String): String {
        val resourceId = resources.getIdentifier(resourceString.replace('-', '_').trim(), "string", packageName)

        return if (resourceId != 0) {
            getString(resourceId)
        } else {
            ""
        }
    }


    private fun getRemoteView(title: String, description: String): RemoteViews? {
        val remoteView = RemoteViews("com.example.flats4us", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.description, description)
        remoteView.setImageViewResource(R.id.appLogo, R.drawable.logo)

        return remoteView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, description: String) {
        val intent = Intent(this, DrawerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, description))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }
}

package com.sjaindl.s11

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class S11FirebaseMessagingService: FirebaseMessagingService() {

    private val tag = "S11FirebaseMessagingService"

    override fun onNewToken(token: String) {
        println("Push Notification onNewToken: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(tag, "From: ${message.from}")

        if (message.data.isNotEmpty()) {
            Log.d(tag, "Message data payload: ${message.data}")

            // no message handling needed
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            Log.d(tag, "Message Notification Body: ${it.body}")
        }
    }
}

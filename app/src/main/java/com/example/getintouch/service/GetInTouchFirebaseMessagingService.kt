package com.example.getintouch.service

import android.os.Build
import android.util.Log
import androidx.compose.runtime.remember
import com.example.getintouch.data.datastore.DeviceIdManager
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.NotificationRepository
import com.example.getintouch.data.repository.PersonRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GetInTouchFirebaseMessagingService : FirebaseMessagingService() {
    val deviceIdManager = DeviceIdManager(this)
    val notificationRepository = NotificationRepository(this)
    val authPreferences = AuthPreferences(this)
    val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"

    override fun onNewToken(fcmToken: String) {
        super.onNewToken(fcmToken)

        CoroutineScope(Dispatchers.IO).launch {
            syncFcmToken(fcmToken)
        }
    }

    private suspend fun syncFcmToken(fcmToken: String) {
        val deviceId = deviceIdManager.getOrCreateDeviceId()
        val token = authPreferences.tokenFlow.first()

        if (!token.isNullOrBlank()) {
            notificationRepository.updateFcmToken(
                deviceId = deviceId,
                deviceName = deviceName,
                fcmToken = fcmToken,
                token = token
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", "Message received")
    }
}
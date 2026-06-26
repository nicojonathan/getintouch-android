package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.GetUnreadNotificationsResponse
import com.example.getintouch.data.api.MessageOnlyResponse

class NotificationRepository(
    private val context: Context,
) {
    fun getUnreadNotifications(
        token: String
    ): ApiResponse<GetUnreadNotificationsResponse> {
        return ApiClient.get<GetUnreadNotificationsResponse>(
            "notifications",
            token
        )
    }

    fun syncFcmToken(
        deviceId: String,
        deviceName: String,
        fcmToken: String,
        token: String
    ) {
        updateFcmToken(
            deviceId = deviceId,
            deviceName = deviceName,
            fcmToken = fcmToken,
            token = token
        )
    }

    fun updateFcmToken(
        deviceId: String,
        deviceName: String,
        fcmToken: String,
        token: String
    ): ApiResponse<MessageOnlyResponse> {
        val json = """
        {
          "device_id":"${deviceId}",
          "device_name":"${deviceName}",
          "fcm_token":"${fcmToken}"
        }
        """.trimIndent()

        return ApiClient.post<MessageOnlyResponse>(
            "users/fcmtoken/update",
            json,
            token
        )
    }
}
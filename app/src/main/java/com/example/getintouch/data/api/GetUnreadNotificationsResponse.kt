package com.example.getintouch.data.api

import com.example.getintouch.data.model.NotificationDto
import com.example.getintouch.ui.model.NotificationUi

data class GetUnreadNotificationsResponse(
    val message: String,
    val notifications: List<NotificationUi>
)

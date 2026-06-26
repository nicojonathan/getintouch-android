package com.example.getintouch.data.api

import com.example.getintouch.data.model.NotificationDto


data class SayHiResponse(
    val message: String,
    val notification: NotificationDto
)
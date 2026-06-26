package com.example.getintouch.data.model

data class NotificationEvent(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val type: String
)

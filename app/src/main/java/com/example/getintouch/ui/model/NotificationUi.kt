package com.example.getintouch.ui.model

import com.google.gson.annotations.SerializedName

data class NotificationUi(
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("type")
    val type: NotificationType,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("sender_name")
    val senderName: String,
    @SerializedName("sender_profile_url")
    val senderProfileUrl: String,
)

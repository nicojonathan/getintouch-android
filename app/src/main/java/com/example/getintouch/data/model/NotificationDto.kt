package com.example.getintouch.data.model

import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName

@Serializable
data class NotificationDto(
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    val type: String,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
)

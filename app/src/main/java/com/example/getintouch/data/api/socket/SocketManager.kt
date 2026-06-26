package com.example.getintouch.data.api.socket

import com.example.getintouch.BuildConfig
import com.example.getintouch.data.model.NotificationEvent
import com.example.getintouch.ui.model.NotificationType
import com.example.getintouch.ui.model.NotificationUi
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.json.JSONObject

object SocketManager {

    private const val SERVER_URL = BuildConfig.BASE_URL

    private var socket: Socket? = null

    private val _notifications =
        MutableSharedFlow<NotificationUi>(
            extraBufferCapacity = 10
        )

    val notifications: SharedFlow<NotificationUi>
        get() = _notifications

    fun connect(userId: Int) {

        if (socket?.connected() == true) {
            return
        }

        socket = IO.socket(SERVER_URL)

        socket?.on(Socket.EVENT_CONNECT) {

            println("Socket connected")

            socket?.emit(
                "register",
                userId
            )
        }

        socket?.on(Socket.EVENT_DISCONNECT) {

            println("Socket disconnected")
        }

        socket?.on("notification") { args ->

            try {

                if (args.isEmpty()) {
                    return@on
                }

                val data = args[0] as JSONObject

                val notification = NotificationUi(
                    senderId = data.getInt("sender_id"),
                    receiverId = data.getInt("receiver_id"),
                    type = NotificationType.SAY_HI,
                    isRead = data.getBoolean("is_read"),
                    createdAt = data.getString("created_at"),
                    senderName = data.getString("sender_name"),
                    senderProfileUrl = data.getString("sender_profile_url")
                )

                _notifications.tryEmit(notification)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        socket?.connect()
    }

    fun disconnect() {

        socket?.off()

        socket?.disconnect()

        socket = null
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }
}
package com.example.getintouch.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.getintouch.BuildConfig
import com.example.getintouch.R
import com.example.getintouch.data.api.socket.SocketManager
import com.example.getintouch.data.model.NotificationDto
import com.example.getintouch.ui.model.NotificationUi

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    notifications: List<NotificationUi>,
    socketManager: SocketManager,
    onNewNotificationArrive: (notification: NotificationUi) -> Unit,
    onNotificationClicked: (personId: Int) -> Unit
) {

    LaunchedEffect(Unit) {
        socketManager.notifications.collect { event ->
            Log.d("Notification", "Received: $event")
            onNewNotificationArrive(event)
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        notifications.forEach { notification ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(CircleShape)
                ) {
                    val imageUrl =
                        if (BuildConfig.ENV == "development") {
                            BuildConfig.BASE_URL + notification.senderProfileUrl.removePrefix("/")
                        } else {
                            notification.senderProfileUrl
                        }

                    AsyncImage(
                        model = imageUrl,
                        placeholder = painterResource(R.drawable.default_profile),
                        error = painterResource(R.drawable.default_profile),
                        contentDescription = "sender picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                onNotificationClicked(notification.senderId)
                            }
                    )
                }

                Text(text = "${notification.senderName} said hi to you")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        modifier = Modifier,
        notifications = emptyList(),
        socketManager = SocketManager,
        onNewNotificationArrive = {},
        onNotificationClicked = {}
    )
}


package com.example.getintouch

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.getintouch.ui.screen.AppRoot
import com.example.getintouch.ui.screen.MainScreen
import com.example.getintouch.ui.screen.SignUpScreen
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appLinkData = intent?.data
        val token = appLinkData?.getQueryParameter("token")



        Log.d("APP_LINK", "Token: $token")

        enableEdgeToEdge()
        setContent {
            AppRoot(
                context = this,
                inviteToken = token
            )
        }
    }
}
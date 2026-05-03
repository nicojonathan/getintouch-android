package com.example.getintouch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.getintouch.ui.screen.AppRoot
import com.example.getintouch.ui.screen.MainScreen
import com.example.getintouch.ui.screen.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot(context = this)
        }
    }
}
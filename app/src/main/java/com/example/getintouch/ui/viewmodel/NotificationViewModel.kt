package com.example.getintouch.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.model.NotificationDto
import com.example.getintouch.data.repository.NotificationRepository
import com.example.getintouch.ui.model.NotificationUi
import com.example.getintouch.utils.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(
    private val authPreferences: AuthPreferences,
    private val repo: NotificationRepository,
): ViewModel() {
    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")

    var notifications by mutableStateOf<List<NotificationUi>>(emptyList())

    init {
        startPollingNotifications()
    }

    private fun startPollingNotifications() {
        viewModelScope.launch {
            while (true) {
                loadUnreadNotifications()
                delay(10_000)
            }
        }
    }

    fun loadUnreadNotifications() {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first()

            if (!token.isNullOrBlank()) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        repo.getUnreadNotifications(token)
                    }

                    if (isSuccess(response.statusCode)) {
                        notifications = response.body.notifications
                    } else {
                        errorMessage = response.body.message
                        showErrorMessage = true
                    }
                } catch (e: Exception) {
                    errorMessage = "Terjadi Kesalahan"
                    showErrorMessage = true
                }
            }
        }
    }

    fun addNotification(notification: NotificationUi) {
        notifications = listOf(notification) + notifications // prepend
        // or notifications = notifications + notification // append
    }
}
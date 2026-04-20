package com.example.getintouch.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.getintouch.ui.model.PersonUi

class SessionViewModel() : ViewModel() {
    var currentUser by mutableStateOf<PersonUi?>(null)
        private set

    fun login(user: PersonUi) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }
}
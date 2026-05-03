package com.example.getintouch.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.repository.AuthRepository
import com.example.getintouch.ui.model.PersonUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionViewModel() : ViewModel() {
    private val repo = AuthRepository();

    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")


    var currentUser by mutableStateOf<PersonUi?>(null)
        private set

    fun adminSignUp(
        organizationName: String,
        name: String,
        email: String,
        phone: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = withContext(Dispatchers.IO) {
                    repo.adminSignup(organizationName, name, email, phone, password)
                }

                if (response.statusCode in 200..299) {
                    currentUser = response.body.user
                } else {
                    errorMessage = response.body.message
                    showErrorMessage = true
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: ""
                showErrorMessage = true
            } finally {
                isLoading = false
            }
        }
    }

    fun login(user: PersonUi) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }
}
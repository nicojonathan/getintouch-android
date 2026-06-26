package com.example.getintouch.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.OrganizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InviteMemberViewModel(
    private val repo: OrganizationRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")
    var showSuccessMessage by mutableStateOf(false)
    var successMessage by mutableStateOf<String>("")
    var inviteLink by mutableStateOf<String>("")

    fun inviteMember(
        email: String
    ) {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first()

            if (token == null) return@launch

            try {
                isLoading = true

                val response = withContext(Dispatchers.IO) {
                    repo.inviteMember(email, token)
                }

                if (response.statusCode in 200..299) {
                    successMessage = response.body.message
                    inviteLink = response.body.inviteLink
                    showSuccessMessage = true

                    errorMessage = ""
                    showErrorMessage = false
                } else {
                    errorMessage = response.body.message
                    showErrorMessage = true

                    successMessage = ""
                    showSuccessMessage = false
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: ""
                showErrorMessage = true
            } finally {
                isLoading = false
            }
        }
    }

    fun copyInviteLink(clipboardManager: ClipboardManager) {
        isLoading = true

        clipboardManager.setText(
            androidx.compose.ui.text.AnnotatedString(inviteLink)
        )
        successMessage = "Invitation link copied to clipboard!"

        isLoading = false
    }
}
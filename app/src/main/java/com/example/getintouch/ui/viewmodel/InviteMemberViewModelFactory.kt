package com.example.getintouch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.AuthRepository
import com.example.getintouch.data.repository.OrganizationRepository

class InviteMemberViewModelFactory(
    private val repo: OrganizationRepository,
    private val authPreferences: AuthPreferences,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InviteMemberViewModel::class.java)) {
            return InviteMemberViewModel(repo, authPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.getintouch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.AuthRepository
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository

class SessionViewModelFactory(
    private val authPreferences: AuthPreferences,
    private val repo: AuthRepository,
    private val departmentRepository: DepartmentRepository,
    private val hobbyRepository: HobbyRepository,
    private val inviteToken: String?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel(authPreferences, repo, departmentRepository, hobbyRepository, inviteToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
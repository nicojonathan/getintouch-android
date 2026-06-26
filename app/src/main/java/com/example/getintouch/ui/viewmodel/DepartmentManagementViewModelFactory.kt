package com.example.getintouch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.DepartmentRepository

class DepartmentManagementViewModelFactory(
    private val repo: DepartmentRepository,
    private val authPreferences: AuthPreferences,
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepartmentManagementViewModel::class.java)) {
            return DepartmentManagementViewModel(authPreferences, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
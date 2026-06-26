package com.example.getintouch.data.api

import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.data.repository.NotificationRepository
import com.example.getintouch.data.repository.PersonRepository

data class AppRepositories(
    val hobbyRepository: HobbyRepository,
    val departmentRepository: DepartmentRepository,
    val personRepository: PersonRepository,
    val notificationRepository: NotificationRepository,
    val authPreferences: AuthPreferences
)

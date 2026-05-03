package com.example.getintouch.ui.model

data class PersonUi(
    val id: Int,
    val name: String,
    val department: String,
    val hobbies: List<String>,
    val description: String,
    val instagram: String,
    val linkedin: String,
    val profileUrl: String,
    val role: String,
)
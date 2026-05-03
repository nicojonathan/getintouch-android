package com.example.getintouch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val id: Int,
    val name: String,
    val departmentId: Int,
    val hobbyIds: List<Int>,
    val description: String,
    val instagram: String,
    val linkedin: String,
    val profileUrl: String,
    val email: String,
    val role: String,
)
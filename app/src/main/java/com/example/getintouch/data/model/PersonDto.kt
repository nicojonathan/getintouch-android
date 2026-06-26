package com.example.getintouch.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val id: Int,
    val name: String,
    val department: DepartmentDto = DepartmentDto(id = 0, name = ""),
    val hobbies: List<HobbyDto> = emptyList(),
    val description: String,
    val instagram: String,
    val linkedin: String,
    @SerializedName("profile_url")
    val profileUrl: String,
    val email: String,
    val role: String,
)
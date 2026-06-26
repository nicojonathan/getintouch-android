package com.example.getintouch.ui.model

import com.example.getintouch.data.model.DepartmentDto
import com.example.getintouch.data.model.HobbyDto

data class PersonUi(
    val id: Int,
    val name: String,
    val department: DepartmentDto = DepartmentDto(id = 0, name = ""),
    val hobbies: List<HobbyDto> = emptyList(),
    val description: String,
    val instagram: String,
    val linkedin: String,
    val profileUrl: String,
    val role: String,
)
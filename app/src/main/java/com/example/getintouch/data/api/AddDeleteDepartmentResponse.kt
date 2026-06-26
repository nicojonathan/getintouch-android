package com.example.getintouch.data.api

import com.example.getintouch.data.model.DepartmentDto

data class AddDeleteDepartmentResponse(
    val message: String,
    val department: DepartmentDto
)

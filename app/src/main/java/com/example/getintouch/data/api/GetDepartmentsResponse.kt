package com.example.getintouch.data.api

import com.example.getintouch.data.model.DepartmentDto

data class GetDepartmentsResponse(
    val message: String,
    val departments: List<DepartmentDto>
)

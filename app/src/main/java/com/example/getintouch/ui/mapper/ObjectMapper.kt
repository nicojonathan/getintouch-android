package com.example.getintouch.ui.mapper

import com.example.getintouch.data.model.DepartmentDto
import com.example.getintouch.data.model.HobbyDto
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi

fun departmentUiToDepartmentDto(department: DepartmentUi): DepartmentDto {
    return DepartmentDto(
        id = department.id,
        name = department.name
    )
}

fun hobbyUiToHobbyDto(hobby: HobbyUi): HobbyDto {
    return HobbyDto(
        id = hobby.id,
        name = hobby.name
    )
}

fun hobbyDtoToHobbyUi(hobby: HobbyDto): HobbyUi {
    return HobbyUi(
        id = hobby.id,
        name = hobby.name,
        isSelected = false
    )
}

fun departmentDtoToDepartmentUi(department: DepartmentDto): DepartmentUi {
    return DepartmentUi(
        id = department.id,
        name = department.name,
        isSelected = false
    )
}
package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.model.DepartmentDto
import com.example.getintouch.ui.model.DepartmentUi
import kotlinx.serialization.json.Json

class DepartmentRepository(private val context: Context) {
    private fun loadDepartments(): List<DepartmentDto> {
        val json = context.assets.open("departments.json")
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString<List<DepartmentDto>>(json)
    }

    private val departmentMap: Map<Int, DepartmentDto> by lazy {
        loadDepartments().associateBy { it.id }
    }

    fun getDepartmentName(id: Int): String? {
        return departmentMap[id]?.name
    }

    fun getDepartments(): List<DepartmentUi> {
        return loadDepartments().map {
            DepartmentUi(
                id = it.id,
                name = it.name,
                isSelected = false
            )
        }
    }
}
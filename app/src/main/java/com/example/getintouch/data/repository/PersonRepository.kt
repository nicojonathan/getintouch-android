package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.model.PersonDto
import com.example.getintouch.ui.model.PersonUi
import kotlinx.serialization.json.Json

class PersonRepository(
    private val context: Context,
    private val hobbyRepository: HobbyRepository,
    private val departmentRepository: DepartmentRepository
) {

    fun loadPersons(): List<PersonUi> {
        val json = context.assets.open("persons.json")
            .bufferedReader()
            .use { it.readText() }

        val persons = Json.decodeFromString<List<PersonDto>>(json)

        // You can also load departments + hobbies here and map
        return persons.map {
            PersonUi(
                id = it.id,
                name = it.name,
                department = it.departmentId.let { id -> departmentRepository.getDepartmentName(id) } ?: "Unknown",
                hobbies = it.hobbyIds.mapNotNull { id ->
                    hobbyRepository.getHobbyName(id)
                },
                description = it.description,
                instagram = it.instagram,
                linkedin = it.linkedin,
                profileUrl = it.profileUrl
            )
        }
    }
}
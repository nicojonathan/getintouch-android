package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.model.HobbyDto
import com.example.getintouch.ui.model.HobbyUi
import kotlinx.serialization.json.Json

class HobbyRepository(private val context: Context) {
    private fun loadHobbies(): List<HobbyDto> {
        val json = context.assets.open("hobbies.json")
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString<List<HobbyDto>>(json)
    }

    private val hobbyMap: Map<Int, HobbyDto> by lazy {
        loadHobbies().associateBy { it.id }
    }

    fun getHobbyName(id: Int): String? {
        return hobbyMap[id]?.name
    }

    fun getHobbies(): List<HobbyUi> {
        return loadHobbies().map {
            HobbyUi(
                id = it.id,
                name = it.name,
                isSelected = false
            )
        }
    }
}
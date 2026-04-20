package com.example.getintouch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HobbyDto(
    val id: Int,
    val name: String,
)
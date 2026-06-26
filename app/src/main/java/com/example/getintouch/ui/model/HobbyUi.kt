package com.example.getintouch.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class HobbyUi(
    val id: Int,
    val name: String,
    var isSelected: Boolean
)
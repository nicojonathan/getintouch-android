package com.example.getintouch.data.api

import com.example.getintouch.data.model.HobbyDto

data class GetHobbiesResponse(
    val message: String,
    val hobbies: List<HobbyDto>,
)

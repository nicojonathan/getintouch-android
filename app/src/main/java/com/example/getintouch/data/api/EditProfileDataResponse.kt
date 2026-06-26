package com.example.getintouch.data.api

import com.example.getintouch.data.model.PersonDto

data class EditProfileDataResponse(
    val message: String,
    val user: PersonDto
)

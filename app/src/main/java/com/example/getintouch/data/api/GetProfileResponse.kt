package com.example.getintouch.data.api

import com.example.getintouch.data.model.PersonDto
import com.example.getintouch.ui.model.PersonUi

data class GetProfileResponse (
    val message: String,
    val user: PersonDto,
)
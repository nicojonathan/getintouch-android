package com.example.getintouch.data.api

import com.example.getintouch.data.model.PersonDto

data class SignUpLogInResponse (
    val message: String,
    val user: PersonDto,
    val token: String,
)
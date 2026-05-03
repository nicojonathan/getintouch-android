package com.example.getintouch.data.api

import com.example.getintouch.ui.model.PersonUi

data class SignUpResponse (
    val message: String,
    val user: PersonUi
)
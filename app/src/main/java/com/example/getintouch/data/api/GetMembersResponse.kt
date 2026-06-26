package com.example.getintouch.data.api

import com.example.getintouch.data.model.PersonDto
import com.example.getintouch.ui.model.PersonUi

data class GetMembersResponse(
    val message: String,
    val users: List<PersonDto>
)

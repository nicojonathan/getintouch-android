package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.GetHobbiesResponse
import com.example.getintouch.data.api.GetMembersResponse
import com.example.getintouch.data.model.HobbyDto
import com.example.getintouch.ui.model.HobbyUi
import kotlinx.serialization.json.Json

class HobbyRepository(private val context: Context) {
    fun loadHobbies(
        token: String?
    ): ApiResponse<GetHobbiesResponse> {
        return ApiClient.get<GetHobbiesResponse>(
            "hobbies/all",
            token
        )
    }
}
package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.InviteMemberResponse
import com.example.getintouch.data.api.SignUpLogInResponse

class OrganizationRepository() {
    fun inviteMember(email: String, token: String): ApiResponse<InviteMemberResponse> {
        val json = """
        {
          "email":"$email"
        }
        """.trimIndent()

        return ApiClient.post<InviteMemberResponse>(
            "users/members/invite",
            json,
            token
        )
    }
}
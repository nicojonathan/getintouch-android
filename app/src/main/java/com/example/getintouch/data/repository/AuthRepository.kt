package com.example.getintouch.data.repository

import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.SignUpResponse

class AuthRepository {
    fun adminSignup(
        organizationName: String,
        name: String,
        email: String,
        phone: String,
        password: String
    ): ApiResponse<SignUpResponse> {

        val json = """
        {
          "organization_name":"$organizationName",
          "name":"$name",
          "email":"$email",
          "phone":"$phone",
          "password":"$password"
        }
        """.trimIndent()

        return ApiClient.post<SignUpResponse>(
            "users/admin/signup",
            json
        )
    }
}
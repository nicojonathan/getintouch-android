package com.example.getintouch.data.repository

import android.util.Log
import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.GetProfileResponse
import com.example.getintouch.data.api.SignUpLogInResponse
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthRepository {
    fun adminSignup(
        organizationName: String,
        name: String,
        email: String,
        phone: String,
        password: String
    ): ApiResponse<SignUpLogInResponse> {

        val json = """
        {
          "organization_name":"$organizationName",
          "name":"$name",
          "email":"$email",
          "phone":"$phone",
          "password":"$password"
        }
        """.trimIndent()

        return ApiClient.post<SignUpLogInResponse>(
            "users/admin/signup",
            json
        )
    }

    fun memberSignup(
        name: String,
        password: String,
        department: DepartmentUi?,
        hobbies: List<HobbyUi>,
        inviteToken: String,
        deviceId: String,
        deviceName: String,
        fcmToken: String
    ): ApiResponse<SignUpLogInResponse> {

        val json = """
        {
          "name":"$name",
          "password":"$password",
          "department":${Json.encodeToString(department)},
          "hobbies":${Json.encodeToString(hobbies)},
          "token":"$inviteToken",
          "deviceId":"$deviceId",
          "deviceName":"$deviceName",
          "fcmToken":"$fcmToken"
        }
        """.trimIndent()

        return ApiClient.post<SignUpLogInResponse>(
            "users/members/signup",
            json
        )
    }

    fun logIn(
        email: String,
        password: String,
        deviceId: String,
        deviceName: String,
        fcmToken: String
    ): ApiResponse<SignUpLogInResponse> {

        val json = """
        {
          "email":"$email",
          "password":"$password",
          "deviceId":"$deviceId",
          "deviceName":"$deviceName",
          "fcmToken":"$fcmToken"
        }
        """.trimIndent()

        Log.e("Payload Login: ", "${json}")

        return ApiClient.post<SignUpLogInResponse>(
            "users/login",
            json
        )
    }

    fun getProfile(
        token: String
    ): ApiResponse<GetProfileResponse> {
        return ApiClient.get<GetProfileResponse>(
            "users/me",
            token
        )
    }
}
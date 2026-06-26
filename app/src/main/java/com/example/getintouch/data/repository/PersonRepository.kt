package com.example.getintouch.data.repository

import android.content.Context
import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.EditProfileDataResponse
import com.example.getintouch.data.api.GetMembersResponse
import com.example.getintouch.data.api.MessageOnlyResponse
import com.example.getintouch.data.api.SayHiResponse
import com.example.getintouch.data.api.SignUpLogInResponse
import com.example.getintouch.data.api.UploadFileMultipartResponse
import com.example.getintouch.data.model.PersonDto
import com.example.getintouch.ui.model.PersonUi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PersonRepository(
    private val context: Context,
    private val hobbyRepository: HobbyRepository,
    private val departmentRepository: DepartmentRepository
) {
    fun loadMembers(
        token: String
    ): ApiResponse<GetMembersResponse> {

        return ApiClient.get<GetMembersResponse>(
            "users/members",
            token
        )
    }

    fun sendNotification(
        receiverId: Number,
        token: String
    ): ApiResponse<SayHiResponse> {
        val json = """
        {
          "receiver_id":"${receiverId}"
        }
        """.trimIndent()

        return ApiClient.post<SayHiResponse>(
            "users/members/say-hi",
            json,
            token
        )
    }

    fun editProfileData(
        person: PersonUi,
        token: String
    ): ApiResponse<EditProfileDataResponse> {
        val json = """
        {
          "name":"${person.name}",
          "department_id":"${person.department.id}",
          "hobbies_id":"${person.hobbies.map { it.id }}",
          "description":"${person.description}",
          "instagram":"${person.instagram}",
          "linkedin":"${person.linkedin}"
        }
        """.trimIndent()

        return ApiClient.post<EditProfileDataResponse>(
            "users/me/profile",
            json,
            token
        )
    }

    fun uploadProfilePicture(
        file: File,
        token: String
    ): ApiResponse<UploadFileMultipartResponse> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "profile_picture",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
            .build()

        return ApiClient.multipartPost<UploadFileMultipartResponse>(
            "users/me/profile-picture",
            requestBody,
            token
        )
    }


}
package com.example.getintouch.data.repository

import android.content.Context
import android.util.Log
import com.example.getintouch.data.api.AddDeleteDepartmentResponse
import com.example.getintouch.data.api.ApiClient
import com.example.getintouch.data.api.ApiResponse
import com.example.getintouch.data.api.GetDepartmentsResponse

class DepartmentRepository(private val context: Context) {
    fun loadDepartments(
        token: String?,
        inviteToken: String?
    ): ApiResponse<GetDepartmentsResponse> {
        Log.e("Token: ", "${token}")
        val json = if (inviteToken != null) {
            """
        {
          "token":"$token",
          "inviteToken":"$inviteToken"
        }
        """.trimIndent()
        } else {
            """
        {
          "token":"$token"
        }
        """.trimIndent()
        }

        val endpoint = if (token != null) {
            "departments/all"
        } else {
            "departments/guest/all"
        }

        Log.e("Endpoint DEPT: ", "${endpoint}")

        return ApiClient.post<GetDepartmentsResponse>(
            endpoint,
            json,
            token
        )
    }

    fun addDepartment(
        token: String?,
        departmentName: String
    ): ApiResponse<AddDeleteDepartmentResponse> {
        val json =
            """
        {
          "token":"$token",
          "name":"$departmentName"
        }
        """.trimIndent()

        val endpoint = "departments/add"

        return ApiClient.post<AddDeleteDepartmentResponse>(
            endpoint,
            json,
            token
        )
    }

    fun removeDepartmentById(
        token: String?,
        departmentId: Int
    ): ApiResponse<AddDeleteDepartmentResponse> {
        val json =
            """
        {
          "token":"$token",
          "id":"$departmentId"
        }
        """.trimIndent()

        val endpoint = "departments/delete"

        return ApiClient.post<AddDeleteDepartmentResponse>(
            endpoint,
            json,
            token
        )
    }
}
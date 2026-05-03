package com.example.getintouch.data.api

data class ApiResponse<T> (
    val statusCode: Int,
    val body: T,
)
package com.example.getintouch.utils

fun isSuccess(statusCode: Int): Boolean {
    return statusCode in 200..299
}
package com.example.getintouch.data.api

import java.net.HttpURLConnection
import java.net.URL
import com.example.getintouch.BuildConfig
import com.google.gson.Gson

object ApiClient {
    inline fun <reified T> post(
        endpoint: String,
        jsonBody: String
    ): ApiResponse<T> {
        val url = URL(BuildConfig.BASE_URL + endpoint)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true
        conn.connectTimeout = 5000
        conn.readTimeout = 5000

        conn.outputStream.use {
            it.write(jsonBody.toByteArray())
        }

        val code = conn.responseCode

        val stream =
            if (code in 200..299)
                conn.inputStream
            else
                conn.errorStream

        val response = stream.bufferedReader().readText()

        val parsedResponse =
            Gson().fromJson(response, T::class.java)

        conn.disconnect()

        return ApiResponse<T>(
            code,
            parsedResponse
        )
    }
}
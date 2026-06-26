package com.example.getintouch.data.api

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import com.example.getintouch.BuildConfig
import com.google.gson.Gson
import okhttp3.MultipartBody
import okio.buffer
import okio.sink
import java.io.DataOutputStream
import java.io.File

object ApiClient {
    inline fun <reified T> multipartPost(
        endpoint: String,
        requestBody: MultipartBody,
        token: String? = null
    ): ApiResponse<T> {

        val url = URL(BuildConfig.BASE_URL + endpoint)

        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.doInput = true

        conn.connectTimeout = 20000000
        conn.readTimeout = 20000000

        if (token != null) {
            conn.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )
        }

        // IMPORTANT
        conn.setRequestProperty(
            "Content-Type",
            requestBody.contentType().toString()
        )

        conn.outputStream.use { outputStream ->

            outputStream.sink().buffer().use { sink ->

                requestBody.writeTo(sink)

                sink.flush()
            }
        }

        val code = conn.responseCode

        val stream =
            if (code in 200..299)
                conn.inputStream
            else
                conn.errorStream

        val response =
            stream.bufferedReader().readText()

        val parsedResponse =
            Gson().fromJson(response, T::class.java)

        conn.disconnect()

        return ApiResponse(
            code,
            parsedResponse
        )
    }

    inline fun <reified T> post(
        endpoint: String,
        jsonBody: String,
        token: String? = null
    ): ApiResponse<T> {
        val url = URL(BuildConfig.BASE_URL + endpoint)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")

        if (token != null) {
            conn.setRequestProperty("Authorization", "Bearer $token")
        }

        conn.doOutput = true
        conn.connectTimeout = 20000
        conn.readTimeout = 20000

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

    inline fun <reified T> get(
        endpoint: String,
        token: String? = null
    ): ApiResponse<T> {
        val url = URL(BuildConfig.BASE_URL + endpoint)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-Type", "application/json")

        if (token != null) {
            conn.setRequestProperty("Authorization", "Bearer $token")
        }

        conn.connectTimeout = 20000
        conn.readTimeout = 20000

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

        return ApiResponse(
            code,
            parsedResponse
        )
    }
}
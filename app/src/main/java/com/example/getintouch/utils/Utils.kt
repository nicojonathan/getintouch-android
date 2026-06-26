package com.example.getintouch.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open URI")

    val tempFile = File.createTempFile(
        "upload_",
        ".jpg",
        context.cacheDir
    )

    tempFile.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    inputStream.close()

    return tempFile
}


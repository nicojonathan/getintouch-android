package com.example.getintouch.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import java.util.UUID

class DeviceIdManager(
    private val context: Context
) {

    suspend fun getOrCreateDeviceId(): String {
        val preferences = context.dataStore.data.first()

        val existingDeviceId = preferences[PreferenceKeys.DEVICE_ID]

        if (!existingDeviceId.isNullOrBlank()) {
            return existingDeviceId
        }

        val newDeviceId = UUID.randomUUID().toString()

        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.DEVICE_ID] = newDeviceId
        }

        return newDeviceId
    }
}
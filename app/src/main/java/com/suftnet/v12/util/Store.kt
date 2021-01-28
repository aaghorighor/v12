package com.suftnet.v12.util

import android.content.Context
import kotlinx.serialization.json.JsonConfiguration

class Store(private val context: Context)  {
    companion object {
        const val USER_KEY = "user"
        const val SESSION_ID_KEY = "session_id"
    }

    private val prefUtils by lazy {
        Preference(
                context.getSharedPreferences("justjava_prefs", Context.MODE_PRIVATE),
                JsonConfiguration.appConfig
        )
    }
}
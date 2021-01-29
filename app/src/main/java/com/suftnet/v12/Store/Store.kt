package com.suftnet.v12.Store

import android.content.Context
import com.suftnet.v12.api.model.response.User
import com.suftnet.v12.util.Preference
import com.suftnet.v12.util.appConfig
import kotlinx.serialization.json.JsonConfiguration

class Store(private val context: Context)  {
    companion object {
        const val USER_KEY = "user"
        const val Token_KEY = "token"
    }

    private val store by lazy {
        Preference(
            context.getSharedPreferences("v12_prefs", Context.MODE_PRIVATE),
            JsonConfiguration.appConfig
        )
    }

    val isSignedIn: Boolean
        get() = store.getObject(USER_KEY, User.serializer()) != null

    var token: String
        get() = store.get(Token_KEY)
        set(value) = store.set(Token_KEY, value)

    var user: User?
        get() = store.getObject(USER_KEY, User.serializer())!!
        set(value) = store.setObject(USER_KEY, value, User.serializer())
}
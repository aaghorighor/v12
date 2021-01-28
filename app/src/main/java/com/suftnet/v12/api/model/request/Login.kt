package com.suftnet.v12.api.model.request
import com.google.gson.annotations.SerializedName

data class Login (
        @SerializedName("userName") val userName : String,
        @SerializedName("password") val password : String
)
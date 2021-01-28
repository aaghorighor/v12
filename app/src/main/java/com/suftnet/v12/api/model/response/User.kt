package com.suftnet.v12.api.model.response
import com.google.gson.annotations.SerializedName

data class UserResponse (
        @SerializedName("user") val user : User
)
data class User (
        @SerializedName("id") val id : String,
        @SerializedName("userName") val userName : String,
        @SerializedName("userType") val userType : String,
        @SerializedName("token") val token : String
)
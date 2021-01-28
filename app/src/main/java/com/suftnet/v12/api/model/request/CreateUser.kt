package com.suftnet.v12.api.model.request

import com.google.gson.annotations.SerializedName

data  class CreateUser (
    @SerializedName("email") val email : String,
    @SerializedName("firstName") val firstName : String,
    @SerializedName("lastName") val lastName : String,
    @SerializedName("phoneNumber") val phoneNumber : String,
    @SerializedName("password") val password : String,
    @SerializedName("active") val active : Boolean,
    @SerializedName("description") val description : String,
    @SerializedName("imageUrl") val imageUrl : String
)
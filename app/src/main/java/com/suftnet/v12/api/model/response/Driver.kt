package com.suftnet.v12.api.model.response

import com.google.gson.annotations.SerializedName

data class Driver (

        @SerializedName("id") val id : String,
        @SerializedName("firstName") val firstName : String,
        @SerializedName("lastName") val lastName : String,
        @SerializedName("phoneNumber") val phoneNumber : String,
        @SerializedName("createdBy") val createdBy : String,
        @SerializedName("createdAt") val createdAt : String,
        @SerializedName("active") val active : Boolean,
        @SerializedName("email") val email : String,
        @SerializedName("imageUrl") val imageUrl : String,
        @SerializedName("description") val description : String
)
package com.suftnet.v12.api.model.response

import com.google.gson.annotations.SerializedName

data class Answer (

    @SerializedName("firstName") val firstName : String,
    @SerializedName("lastName") val lastName : String,
    @SerializedName("description") val description : String,
    @SerializedName("createdDt") val createdDt : String,
    @SerializedName("createdBy") val createdBy : String,
    @SerializedName("id") val id : String,
    @SerializedName("questionId") val questionId : String,
    @SerializedName("phoneNumber") val phoneNumber : String,
    @SerializedName("email") val email : String
)
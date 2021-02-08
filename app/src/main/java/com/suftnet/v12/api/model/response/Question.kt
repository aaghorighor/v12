package com.suftnet.v12.api.model.response

import com.google.gson.annotations.SerializedName

data class Question (

    @SerializedName("firstName") val firstName : String,
    @SerializedName("lastName") val lastName : String,
    @SerializedName("phoneNumber") val phoneNumber : Int,
    @SerializedName("email") val email : String,
    @SerializedName("description") val description : String,
    @SerializedName("createdDt") val createdDt : String,
    @SerializedName("createdBy") val createdBy : String,
    @SerializedName("answerCount") val answerCount : Int,
    @SerializedName("id") val id : String
)
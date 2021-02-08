package com.suftnet.v12.api.model.request
import com.google.gson.annotations.SerializedName

data class CreateAnswer (
        @SerializedName("Description") val description : String,
        @SerializedName("QuestionId") val QuestionId : String

)
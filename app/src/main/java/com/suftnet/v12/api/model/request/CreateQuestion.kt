package com.suftnet.v12.api.model.request
import com.google.gson.annotations.SerializedName

data class CreateQuestion (
        @SerializedName("Description") val description : String

)
package com.suftnet.v12.api.model.response

import com.google.gson.annotations.SerializedName

data class Driver (

        @SerializedName("id") var id : String,
        @SerializedName("firstName") var firstName : String,
        @SerializedName("lastName") var lastName : String,
        @SerializedName("phoneNumber") var phoneNumber : String,
        @SerializedName("createdBy") var createdBy : String,
        @SerializedName("createdAt") var createdAt : String,
        @SerializedName("active") var active : Boolean,
        @SerializedName("email") var email : String,
        @SerializedName("imageUrl") var imageUrl : String,
        @SerializedName("description") var description : String,
        @SerializedName("transportType") var transportType : String,
        @SerializedName("journeyTime") var journeyTime : String
)
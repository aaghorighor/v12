package com.suftnet.v12.api.model.request

import com.google.gson.annotations.SerializedName

data class CreateProduce (
        @SerializedName("name") val name : String,
        @SerializedName("description") val description : String,
        @SerializedName("quantity") val quantity : Int,
        @SerializedName("price") val price : Int,
        @SerializedName("active") val active : Boolean,
        @SerializedName("unitId") val unitId : String,
        @SerializedName("availableDate") val availableDate : String,
        @SerializedName("userId") val userId : String,
        @SerializedName("email") val email : String
)

data class EditProduce (
        @SerializedName("id") val id : String,
        @SerializedName("name") val name : String,
        @SerializedName("description") val description : String,
        @SerializedName("quantity") val quantity : Int,
        @SerializedName("price") val price : Int,
        @SerializedName("active") val active : Boolean,
        @SerializedName("unitId") val unitId : String,
        @SerializedName("availableDate") val availableDate : String,
        @SerializedName("userId") val userId : String,
        @SerializedName("email") val email : String
)

data class DeleteProduce (
        @SerializedName("id") val id : String
)
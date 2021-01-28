package com.suftnet.v12.api.model.response

import com.google.gson.annotations.SerializedName

data  class FetchProduce (
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("quantity") val quantity : Int,
    @SerializedName("price") val price : Int,
    @SerializedName("active") val active : Boolean,
    @SerializedName("unitId") val unitId : String,
    @SerializedName("unit") val unit : String,
    @SerializedName("availableDate") val availableDate : String,
    @SerializedName("createdBy") val createdBy : String,
    @SerializedName("createdAt") val createdAt : String
)
package com.suftnet.v12.api.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order (
        @SerializedName("itemName") val itemName : String,
        @SerializedName("availableDate") val availableDate : String,
        @SerializedName("status") val status : String,
        @SerializedName("statusId") val statusId : String,
        @SerializedName("collectionAddress") val collectionAddress : String,
        @SerializedName("deliveryAddress") val deliveryAddress : String,
        @SerializedName("quantity") val quantity : Int,
        @SerializedName("unit") val unit : String,
        @SerializedName("id") val id : String,
        @SerializedName("city") val city : String,
        @SerializedName("state") val state : String,
        @SerializedName("country") val country : String,
        @SerializedName("phoneNumber") val phoneNumber : String,
        @SerializedName("contact") val farmer : String,
        @SerializedName("address") val address : String,
        @SerializedName("buyerId") val buyerId : String,
        @SerializedName("produceId") val produceId : String,
        @SerializedName("amountPaid") val amountPaid : Int,
        @SerializedName("total") val total : Int,
        @SerializedName("balance") val balance : Int,
        @SerializedName("createdBy") val createdBy : String,
        @SerializedName("createdAt") val createdAt : String
): Serializable
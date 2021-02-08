package com.suftnet.v12.api.model.request
import com.google.gson.annotations.SerializedName

data class UpdateOrderStatus (
        @SerializedName("orderId") val orderId : String,
        @SerializedName("statusId") val statusId : String
)
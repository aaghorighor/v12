package com.suftnet.v12.api.model.request

import com.google.gson.annotations.SerializedName

data class CreateDelivery (

    @SerializedName("driverId") val driverId : String,
    @SerializedName("orderId") val orderId : String
)
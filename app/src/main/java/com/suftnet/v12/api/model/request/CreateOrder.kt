package com.suftnet.v12.api.model.request

import com.google.gson.annotations.SerializedName

data  class CreateOrder (
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("country") val country : String,
    @SerializedName("address") val address : String,
    @SerializedName("buyerId") val buyerId : String,
    @SerializedName("produceId") val produceId : String,
    @SerializedName("amountPaid") val paid : Double
)
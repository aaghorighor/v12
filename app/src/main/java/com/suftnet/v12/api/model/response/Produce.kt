package com.suftnet.v12.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data  class Produce (
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("quantity") val quantity : Int,
    @SerializedName("price") val price : Double,
    @SerializedName("active") val active : Boolean,
    @SerializedName("unitId") val unitId : String,
    @SerializedName("unit") val unit : String?,
    @SerializedName("availableDate") val availableDate : String,
    @SerializedName("createdBy") val createdBy : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("firstName") val firstName : String?,
    @SerializedName("lastName") val lastName : String?,
    @SerializedName("phoneNumber") val phoneNumber : String?,
    @SerializedName("email") val email : String?,
    @SerializedName("city") val city : String?,
    @SerializedName("state") val state : String?,
    @SerializedName("country") val country : String?,
    @SerializedName("address") val address : String?
): Parcelable
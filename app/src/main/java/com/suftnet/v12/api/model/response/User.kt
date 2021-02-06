package com.suftnet.v12.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User (
        @SerializedName("id") val id : String,
        @SerializedName("userName") val userName : String,
        @SerializedName("userType") val userType : String,
        @SerializedName("token") val token : String
): Parcelable
package com.suftnet.v12.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import retrofit2.Response
import com.google.gson.JsonParser

object NetWork {

    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {

                    return true
                }
            }
        }
        return false
    }
    @Suppress("DEPRECATION")
    fun errorHandler(response: Response<*>): com.suftnet.v12.model.Error {

        val error = response.errorBody()?.string()
        var message = ""

        message = when(response.code())
        {
            400 -> {
                JsonParser().parse(error)
                        .asJsonObject["message"]
                        .toString()
            }
            401 -> {
                JsonParser().parse(error)
                        .asJsonObject["message"]
                        .toString()
            }
            500 -> {

                JsonParser().parse(error)
                        .asJsonObject["Message"]
                        .toString()
            }
            else -> {
                "Unknown Error ${response.code()}"
            }
        }

        return com.suftnet.v12.model.Error(message, response.code())
    }

}
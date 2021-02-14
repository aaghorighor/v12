package com.suftnet.v12.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.suftnet.v12.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Util {
    fun isValidPhoneNumber(phoneNumber: CharSequence?): Boolean {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        return Patterns.PHONE.matcher(phoneNumber).matches()
    }

    @Suppress("NAME_SHADOWING")
    @Throws(ParseException::class)
    fun parse(formattedDate: String): Date? {
        var formattedDate = formattedDate
        return try {
            SimpleDateFormat("yyyy-MM-dd").parse(formattedDate)
        } catch (ex: IllegalArgumentException) {
            // error happen in Java 6: Unknown pattern character 'X'
            formattedDate = if (formattedDate.endsWith("Z")) formattedDate.replace("Z", "+0000") else formattedDate.replace("([+-]\\d\\d):(\\d\\d)\\s*$".toRegex(), "$1$2")
            val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            df1.parse(formattedDate)
        }
    }

    @Throws(ParseException::class)
    fun iso8601Format(formattedDate: String): Date? {
        @Suppress("NAME_SHADOWING") var formattedDate = formattedDate
        return try {
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            df.parse(formattedDate)
        } catch (ex: IllegalArgumentException) {
            // error happen in Java 6: Unknown pattern character 'X'
            formattedDate = if (formattedDate.endsWith("Z")) formattedDate.replace("Z", "+0000") else formattedDate.replace("([+-]\\d\\d):(\\d\\d)\\s*$".toRegex(), "$1$2")
            val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            df1.parse(formattedDate)
        }
    }


    @Suppress("DEPRECATION")
    fun Activity.lightStatusBar(statusBarColor: Int = -1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (window.decorView.rootView.systemUiVisibility) {
                0 -> window.decorView.rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        window.decorView.rootView.systemUiVisibility =
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR + View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    } else {
                        window.decorView.rootView.systemUiVisibility =
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
            window.statusBarColor = if (statusBarColor == -1) Color.WHITE else statusBarColor
        }
    }


}
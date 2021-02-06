package com.suftnet.v12.util

import android.util.Patterns
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
            SimpleDateFormat("dd/M/yyyy").parse(formattedDate)
        } catch (ex: IllegalArgumentException) {
            // error happen in Java 6: Unknown pattern character 'X'
            formattedDate = if (formattedDate.endsWith("Z")) formattedDate.replace("Z", "+0000") else formattedDate.replace("([+-]\\d\\d):(\\d\\d)\\s*$".toRegex(), "$1$2")
            val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            df1.parse(formattedDate)
        }
    }

    @Throws(ParseException::class)
    fun iso8601Format(formattedDate: String): Date? {
        @Suppress("NAME_SHADOWING") var formattedDate = formattedDate
        return try {
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            df.parse(formattedDate)
        } catch (ex: IllegalArgumentException) {
            // error happen in Java 6: Unknown pattern character 'X'
            formattedDate = if (formattedDate.endsWith("Z")) formattedDate.replace("Z", "+0000") else formattedDate.replace("([+-]\\d\\d):(\\d\\d)\\s*$".toRegex(), "$1$2")
            val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            df1.parse(formattedDate)
        }
    }

}
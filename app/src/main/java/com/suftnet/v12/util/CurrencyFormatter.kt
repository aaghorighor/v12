package com.suftnet.v12.util

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    fun format(number: Double, decimals: Int = 0): String {
        val numberFormat = NumberFormat.getInstance(Locale("en"))
        numberFormat.roundingMode = RoundingMode.HALF_UP
        numberFormat.maximumFractionDigits = decimals
        numberFormat.minimumFractionDigits = decimals
        return numberFormat.format(number)
    }
}
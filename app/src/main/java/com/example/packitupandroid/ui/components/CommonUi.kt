package com.example.packitupandroid.ui.components

import androidx.core.net.ParseException
import java.text.NumberFormat

fun Double.asCurrencyString(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}

fun String.parseCurrencyToDouble(): Double {
    try {
        val parsedValue = NumberFormat.getCurrencyInstance().parse(this)
        return parsedValue?.toDouble() ?: 0.0
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0.0
}

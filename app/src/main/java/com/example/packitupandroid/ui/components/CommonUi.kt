package com.example.packitupandroid.ui.components

import java.text.NumberFormat

fun Double.asCurrencyString(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}

package com.example.packitupandroid.ui.components

import java.text.NumberFormat

fun Double.formatValue(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}

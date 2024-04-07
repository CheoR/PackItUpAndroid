package com.example.packitupandroid.utils

import androidx.core.net.ParseException
import java.text.NumberFormat

sealed class CardType {
    object Default : CardType()
    object Summary : CardType()
    object Collection : CardType()
    object Box : CardType()
    object Item : CardType()
}

sealed class EditMode {
    object NoEdit : EditMode()
    object Edit : EditMode()
}

sealed class EditFields {
    object ImageUri : EditFields()
    object Name : EditFields()
    object Description : EditFields()
    object IsFragile : EditFields()
    object Value : EditFields()
    object Dropdown: EditFields()
}

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

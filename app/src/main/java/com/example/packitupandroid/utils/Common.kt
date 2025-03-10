package com.example.packitupandroid.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.ParseException
import java.text.NumberFormat
import java.util.Locale


/**
 * Converts a Double value to a currency string using the default locale.
 *
 * This extension function formats a `Double` value as a currency string,
 * using the default locale's currency format. For example, if the default
 * locale is US, it would format the number as "$123.45".
 *
 * @return The formatted currency string.
 */
fun Double.asCurrencyString(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}

/**
 * Parses a currency string to a Double value using the default locale.
 *
 * This extension function attempts to parse a currency string into a `Double` value,
 * using the default locale's currency format. If parsing fails, it returns 0.0 and
 * prints the stack trace of the `ParseException`.
 *
 * @return The parsed `Double` value, or 0.0 if parsing fails.
 */
fun String.parseCurrencyToDouble(): Double {
    val nf: NumberFormat = NumberFormat.getInstance();
    nf.maximumFractionDigits = 2;
    nf.currency = java.util.Currency.getInstance(Locale.getDefault());

    try {
        val parsedValue = nf.parse(this.replace("$", "").trim())
//        val parsedValue = NumberFormat.getCurrencyInstance().parse(this)
        return parsedValue?.toDouble() ?: 0.0
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0.0
}

/**
 * Converts an Int value to a Boolean value.
 *
 * This extension function returns `true` if the `Int` value is greater than 0,
 * and `false` otherwise.
 *
 * @return `true` if the `Int` value is greater than 0, `false` otherwise.
 */
fun Int.toBoolean() = this > 0

/**
 * Sealed class representing the different fields that can be edited in the application.
 *
 * This sealed class defines a set of objects, each representing a specific field that
 * can be edited in the application's UI. It is used to provide a type-safe way to
 * represent editable fields.
 */
sealed class EditFields {
    /**
     * Represents the Image URI field.
     */
    object ImageUri : EditFields()

    /**
     * Represents the Name field.
     */
    object Name : EditFields()

    /**
     * Represents the Description field.
     */
    object Description : EditFields()

    /**
     * Represents the IsFragile field.
     */
    object IsFragile : EditFields()

    /**
     * Represents the Value field.
     */
    object Value : EditFields()

    /**
     * Represents a Dropdown field.
     */
    object Dropdown: EditFields()
}

/**
 * Represents the icon to be displayed in an action column.
 *
 * This sealed class defines the possible icons that can be used in an action column.
 *
 * @param icon The [ImageVector] representing the icon.
 */
sealed class ActionColumnIcon(val icon: ImageVector) {
    object RightArrow : ActionColumnIcon(Icons.AutoMirrored.Filled.ArrowForward)
    object ThreeDots : ActionColumnIcon(Icons.Default.MoreVert)
    object None : ActionColumnIcon(Icons.Default.CheckBoxOutlineBlank)
}


/**
 * Represents the options available in a dropdown menu.
 *
 * Each option has a unique identifier (`id`) and a display name (`name`).
 *
 * @property id The unique identifier for this dropdown option. This value is typically used
 *             internally for handling selections and data association.
 * @property name The user-friendly name of this dropdown option that is displayed in the UI.
 */
interface DropdownOptions {
    val id: String
    val name: String
}

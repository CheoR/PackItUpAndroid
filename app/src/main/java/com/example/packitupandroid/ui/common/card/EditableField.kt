package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle


/**
 * A composable function that displays an editable text field.
 *
 * This composable provides a basic text field that can be either editable or read-only.
 * When editable, it displays with a slight background tint initially, which disappears
 * once the user starts interacting with it.
 *
 * @param value The current text value of the field.
 * @param onValueChange A callback that is invoked when the text value changes. It receives the new text value as a parameter.
 * @param isEditable Whether the text field is editable or read-only.
 * @param textStyle The style to apply to the text within the field.
 * @param modifier Modifier to be applied to the text field.
 * @param minLines The minimum number of lines to be displayed in the text field.
 * @param maxLines The maximum number of lines to be displayed in the text field.
 * @param keyboardOptions Software keyboard options that configure keyboard on screen.
 */
@Composable
fun EditableField(
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val backgroundColor = if (isEditable) Color(0xFF5587D9) else Color.Transparent
    var hasInteracted by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            hasInteracted = true
        },
        textStyle = textStyle,
        maxLines = maxLines,
        minLines = minLines,
        enabled = isEditable,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .background(if (hasInteracted) Color.Transparent else backgroundColor)
            .clickable(onClick = { hasInteracted = true }, enabled = isEditable)
    )
}

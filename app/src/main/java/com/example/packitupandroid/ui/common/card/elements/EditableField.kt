package com.example.packitupandroid.ui.common.card.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager


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
    fieldContentDescription: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val hasInteracted = remember { mutableStateOf(false) }
    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val defaultColor = Color.Transparent

    val backgroundColor = when {
        isEditable && !hasInteracted.value -> highlightColor
        else -> defaultColor
    }

    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            hasInteracted.value = true
        },
        textStyle = textStyle,
        maxLines = maxLines,
        minLines = minLines,
        enabled = isEditable,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .background(backgroundColor)
            .semantics { contentDescription = fieldContentDescription }
            .clickable(onClick = { hasInteracted.value = true }, enabled = isEditable),
        )
}

@Preview(showBackground = true)
@Composable
fun EditableFieldPreview() {
    val text = remember { mutableStateOf("Sample Text") }
    val onValueChange: (String) -> Unit = { text.value = it}
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    PackItUpAndroidTheme(themeManager) {
        Surface {
            EditableField(
                value = text.value,
                onValueChange = onValueChange,
                isEditable = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                fieldContentDescription = "Editable Field"
            )
        }
    }
}

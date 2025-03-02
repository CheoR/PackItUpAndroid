package com.example.packitupandroid.ui.common.card.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager


/**
 * A composable that displays a checkbox which can be editable or read-only.
 * When the checkbox is editable and has not been interacted with, it displays a blue background.
 * Once interacted with, the blue background disappears.
 *
 * @param checked The checked state of the checkbox.
 * @param onCheckedChange The callback that is triggered when the checked state changes.
 *        It receives the new checked state as a parameter.
 * @param isEditable Determines whether the checkbox is editable or read-only.
 *        If true, the user can interact with the checkbox and change its state.
 *        If false, the checkbox is read-only and the user cannot interact with it.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun EditableCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier
) {
    val hasInteracted = remember { mutableStateOf(false) }
    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val defaultColor = Color.Transparent

    val backgroundColor = when {
        isEditable && !hasInteracted.value -> highlightColor
        else -> defaultColor
    }

    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(enabled = isEditable) { hasInteracted.value = true }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
                hasInteracted.value = true
            },
            enabled = isEditable,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EditableCheckboxPreview() {
    val checked = remember { mutableStateOf(false) }
    val onCheckedChange = { newValue: Boolean  ->
        checked.value = newValue
    }
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    PackItUpAndroidTheme(themeManager) {
        Surface {
            EditableCheckbox(
                checked = checked.value,
                onCheckedChange = onCheckedChange,
                isEditable = true,
                modifier = Modifier,
            )
        }
    }
}

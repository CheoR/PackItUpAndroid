package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


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
    var hasInteracted by remember { mutableStateOf(false) }
    val backgroundColor = if (isEditable && !hasInteracted) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(enabled = isEditable) { hasInteracted = true }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
                hasInteracted = true
            },
            enabled = isEditable,
        )
    }
}

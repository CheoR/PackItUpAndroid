package com.example.packitupandroid.ui.common.card.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.DropdownOptions


/**
 * A composable dropdown menu that can be either editable or read-only.
 * When editable and not yet interacted with, it displays a blue background to indicate its active state.
 *
 * @param options The list of [DropdownOptions] to display in the dropdown. Can contain null values to represent an empty option.
 * @param selectedOption The currently selected [DropdownOptions]. Can be null if no option is selected.
 * @param onOptionSelected Callback function invoked when an option is selected. Passes the selected [DropdownOptions] or null if an empty/null option is selected.
 * @param isEditable Boolean indicating whether the dropdown is editable (clickable) or read-only.
 * @param modifier Modifier to be applied to the dropdown.
 */
@Composable
fun EditableDropdown(
    options: List<DropdownOptions?>,
    selectedOption: DropdownOptions?,
    onOptionSelected: (DropdownOptions?) -> Unit,
    isEditable: Boolean,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val hasInteracted = remember { mutableStateOf(false) }
    val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val defaultColor = Color.Transparent
    val dropdownMenuBoxContentDescription = stringResource(R.string.dropdown_select_box)

    val backgroundColor = when {
        isEditable && !hasInteracted.value -> highlightColor
        else -> defaultColor
    }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            if (isEditable) expanded.value = it
        },
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(enabled = isEditable) { hasInteracted.value = true }
            .semantics { contentDescription = dropdownMenuBoxContentDescription },
    ) {
        TextField(
            readOnly = true,
            value = selectedOption?.name ?: "Select Options",
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = backgroundColor,
                focusedContainerColor = backgroundColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        if (option != null) {
                            Text(text = option.name)
                        }
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded.value = false
                        hasInteracted.value = true
                    },
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditableDropdownPreview() {
    val selectedOption = remember { mutableStateOf<DropdownOptions?>(null) }
    val onOptionSelected: (DropdownOptions?) -> Unit = { option ->
        selectedOption.value = option
    }
    val options = listOf(
        BoxIdAndName(id="1", "Option 1"),
        BoxIdAndName(id="2", "Option 2"),
        BoxIdAndName(id="3", "Option 3"),
    )
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    PackItUpAndroidTheme(themeManager) {
        Surface {
            EditableDropdown(
                options = options,
                selectedOption = selectedOption.value,
                onOptionSelected = onOptionSelected,
                isEditable = true,
            )
        }
    }
}

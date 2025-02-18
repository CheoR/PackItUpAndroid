package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    var expanded by remember { mutableStateOf(false) }
    var hasInteracted by remember { mutableStateOf(false) }
    val backgroundColor = if (isEditable && !hasInteracted) Color(0xFF5587D9) else Color.Transparent
//    val interactionSource = remember { MutableInteractionSource() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEditable) { hasInteracted = true },
    ) {
//        BasicTextField(
//            readOnly = true,
//            value = selectedOption?.name ?: "Select Options",
//            onValueChange = {},
//            modifier = Modifier.fillMaxWidth(),
//            interactionSource = interactionSource,
//            textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
//        ) { innerTextField ->
//            TextFieldDefaults.DecorationBox(
//                value = selectedOption?.name ?: "Select Options",
//                innerTextField = innerTextField,
//                interactionSource = interactionSource,
//                contentPadding = PaddingValues(0.dp), // to remove padding
//                enabled = true,
//                singleLine = true,
//                colors = TextFieldDefaults.colors(
//                    unfocusedContainerColor = backgroundColor,
//                    focusedContainerColor = backgroundColor,
//                ),
//                visualTransformation = VisualTransformation.None,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            )
//        }
//

        TextField(
            readOnly = true,
            value = selectedOption?.name ?: "Select Options",
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = backgroundColor,
                focusedContainerColor = backgroundColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
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
                        expanded = false
                        hasInteracted = true
                    },
                )
            }
        }
    }
}

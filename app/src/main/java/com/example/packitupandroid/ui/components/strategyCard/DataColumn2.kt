package com.example.packitupandroid.ui.components.strategyCard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.EditMode
import com.example.packitupandroid.utils.asCurrencyString

val modifier = Modifier
    .fillMaxWidth()

@Composable
fun DataColumn2(
    name: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    isFragile: Boolean = false,
    value: Double = 0.0,
    onFieldChange: (EditFields, String) -> Unit = {_, _ -> Unit},
    editMode: EditMode = EditMode.NoEdit,
    currentSelection: String? = null,
    dropdownSelections: List<String?> = emptyList(),
    editableFields: Set<EditFields> = emptySet(),
) {
    fun isEditable(field: EditFields) = editMode == EditMode.Edit && editableFields.contains(field)

    var dropdownExpanded by remember { mutableStateOf(false) }

    BasicTextField(
        value = name,
        onValueChange = { onFieldChange(EditFields.Name, it) },
        textStyle = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        enabled = isEditable(EditFields.Name),
        modifier = modifier
    )
    Box {
        OutlinedTextField(
            value = currentSelection ?: "",
            onValueChange = { onFieldChange(EditFields.Dropdown, it) },
            readOnly = true,
            modifier = modifier,
            trailingIcon = {
                IconButton(
                    onClick = { dropdownExpanded = !dropdownExpanded },
                    enabled = isEditable(EditFields.Dropdown)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Menu"
                    )
                }
            },
            enabled = isEditable(EditFields.Dropdown),
        )
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            dropdownSelections.forEach { selection ->
                DropdownMenuItem(
                    onClick = {
                        onFieldChange(EditFields.Dropdown, selection ?: "") // :( TODO: fix this
                        dropdownExpanded = false
                    },
                    text = { Text(text = selection ?: "") },
                )
            }
        }
    }
    BasicTextField(
        value = description ?: "",
        onValueChange = { onFieldChange(EditFields.Description, it) },
        textStyle = MaterialTheme.typography.bodyMedium,
        minLines = 3,
        maxLines = 3,
        enabled = isEditable(EditFields.Description),
        modifier = modifier
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isFragile,
            onCheckedChange = { onFieldChange(EditFields.IsFragile, it.toString()) },
            enabled = isEditable(EditFields.IsFragile),
            modifier = Modifier
                .semantics {
                    contentDescription = "Fragile Checkbox"
                },
        )
        Text(stringResource(R.string.fragile))
        Spacer(modifier = Modifier.weight(1f))
        BasicTextField(
            value = value.asCurrencyString(),
            onValueChange = { onFieldChange(EditFields.Value, it) },
            textStyle = MaterialTheme.typography.bodySmall,
            enabled = isEditable(EditFields.Value),
            modifier = Modifier.semantics { contentDescription = "Value Field" },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Preview(
    showBackground = true,
    group = "Item",
)
@Composable
fun PreviewDataColumn2CanEditItemFields() {
    var name by remember { mutableStateOf("Item1") }
    var currentSelection by remember { mutableStateOf("Box1") }
    var description by remember { mutableStateOf("Item 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        DataColumn2(
            name = name,
            description = description,
            isFragile = isFragile,
            value = value,
            onFieldChange = onFieldChange,
            editMode = EditMode.Edit,
            editableFields = setOf(
                EditFields.Name,
                EditFields.Description,
                EditFields.IsFragile,
                EditFields.Value,
                EditFields.Dropdown,
            ),
            currentSelection = currentSelection, // TODO: make currentSelection can only be one of dropdown selections
            dropdownSelections = listOf("Box1", "Box2", "Box3"),
        )
    }
}

@Preview(
    showBackground = true,
    group = "Item",
)
@Composable
fun PreviewDataColumn2CannotEditItemFields() {
    //TODO: FINISH THIS LIKE ABOVE
    val onFieldChange : (EditFields, String) -> Unit = {_, _ -> }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        DataColumn2(
            name = "Item1",
            description = "Item 1 description",
            value = 1.97,
            onFieldChange = onFieldChange,
            editMode = EditMode.NoEdit,
            editableFields = setOf(
                EditFields.Name,
                EditFields.Description,
                EditFields.IsFragile,
                EditFields.Value
            ),
            isFragile = false,
            currentSelection = "Box1",
            dropdownSelections = listOf("Box1", "Box2", "Box3"),
        )
    }
}

@Preview(
    showBackground = true,
    group = "Box",
)
@Composable
fun PreviewDataColumn2CanEditBoxFields() {
    val onFieldChange : (EditFields, String) -> Unit = {_, _ -> }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        DataColumn2(
            name = "Box1",
            description = "Box 1 description",
            isFragile = true,
            value = 0.97,
            onFieldChange = onFieldChange,
            editMode = EditMode.Edit,
            editableFields = setOf(
                EditFields.Name,
                EditFields.Description,
                EditFields.Dropdown,
            ),
            currentSelection = "Collection 2",
            dropdownSelections = listOf("Collection 1", "Collection 2", "Collection 3"),
        )
    }
}

@Preview(
    showBackground = true,
    group = "Box",
)
@Composable
fun PreviewDataColumn2CannotEditBoxFields() {
    val onFieldChange : (EditFields, String) -> Unit = {_, _ -> }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        DataColumn2(
            name = "Box 1",
            description = "Box 1 description",
            value = 1.97,
            onFieldChange = onFieldChange,
            editMode = EditMode.NoEdit,
            editableFields = setOf(
                EditFields.Name,
                EditFields.Description,
            ),
            isFragile = false,
            currentSelection = "Collection 1", // TODO: currentSelection can only be one of dropdown selections
            dropdownSelections = listOf("Collection 1", "Collection 2", "Collection 3"),
        )
    }
}

@Preview(
    showBackground = true,
    group = "Collection",
)
@Composable
fun PreviewDataColumn2CanEditCollectionFields() {
    val onFieldChange : (EditFields, String) -> Unit = {_, _ -> }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        DataColumn2(
            name = "Collection1",
            description = "Collection 1 description",
            isFragile = true,
            value = 0.97,
            onFieldChange = onFieldChange,
            editMode = EditMode.Edit,
            editableFields = setOf(
                EditFields.Name,
                EditFields.Description,
            ),
            currentSelection = null,
            dropdownSelections = emptyList()
        )
    }
}

@Preview(
    showBackground = true,
    group = "Collection",
)
@Composable
fun PreviewDataColumn2CannotEditCollectionFields() {
    val onFieldChange : (EditFields, String) -> Unit = {_, _ -> }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        DataColumn2(
            name = "Collection 1",
            description = "Collection 1 description",
            value = 1.97,
            onFieldChange = onFieldChange,
            editMode = EditMode.NoEdit,
            editableFields = setOf(
                EditFields.Name,
                EditFields.Description,
            ),
            isFragile = false,
            currentSelection = null,
            dropdownSelections = emptyList()
        )
    }
}


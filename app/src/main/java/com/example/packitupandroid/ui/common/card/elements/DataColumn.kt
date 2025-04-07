package com.example.packitupandroid.ui.common.card.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.asCurrencyString
import com.example.packitupandroid.utils.parseCurrencyToDouble


/**
 * A composable function that displays and allows editing of data fields related to a card.
 *
 * This function renders a column containing text fields for the card's name and description,
 * a checkbox for the "fragile" status, and a text field for the card's value.
 * The editable state of each field can be controlled.
 *
 * @param onFieldChange A lambda function that is called when any of the fields are modified.
 *                      It receives the `selectedCard` MutableState, the `EditFields` enum representing the modified field,
 *                      and the new value as a `String`.
 * @param selectedCard A MutableState holding the data of the currently selected card.
 *                     The type `D` must be a subclass of `BaseCardData`.
 * @param modifier A Modifier for styling and layout of the DataColumn.
 * @param editableFields A Set of `EditFields` that specifies which fields are editable.
 *                       If a field'
 * @param dropdownOptions An optional [Result] object representing the state of the dropdown options.
 *                        If provided, a dropdown menu will be displayed, allowing the user to select from the options.
 *                        If `null`, no dropdown menu will be displayed. * @param D the type of data that the card will display. It must inherit from BaseCardData
 * */
@Composable
fun <D: BaseCardData>DataColumn(
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    selectedCard: MutableState<D?>,
    modifier: Modifier = Modifier,
    editableFields: Set<EditFields> = emptySet(),
    dropdownOptions: Result<List<DropdownOptions?>>? = null,
) {
    fun isEditable(field: EditFields) = editableFields.contains(field)

    val dropdownOptionsList = if(dropdownOptions != null) {
        when (dropdownOptions) {
            is Result.Success -> dropdownOptions.data
            is Result.Error -> emptyList()
            is Result.Loading -> emptyList()
        }
    } else { emptyList() }

    var selectedBox by remember { mutableStateOf(
        when(selectedCard.value) {
            is Item -> dropdownOptionsList.find { it?.id == (selectedCard.value as Item).boxId }
            is Box -> dropdownOptionsList.find { it?.id == (selectedCard.value as Box).collectionId }
            else -> null
        }
    ) }

    val name = remember { mutableStateOf(selectedCard.value!!.name) }
    val description = remember { mutableStateOf(selectedCard.value!!.description) }
    val value = remember { mutableDoubleStateOf(selectedCard.value!!.value) }
    val isFragile = remember { mutableStateOf(selectedCard.value!!.isFragile) }

    Column(
        modifier = modifier,
    ) {
        EditableField(
            value = name.value,
            onValueChange = {
                name.value = it
                onFieldChange(selectedCard, EditFields.Name, it)
            },
            isEditable = isEditable(EditFields.Name),
            textStyle = MaterialTheme.typography.displayMedium,
            modifier = Modifier.fillMaxWidth(),
            fieldContentDescription = stringResource(R.string.name) + " field",
        )

        if (dropdownOptions != null) {
            EditableDropdown(
                options = dropdownOptionsList,
                selectedOption = selectedBox,
                onOptionSelected = { option ->
                    selectedBox = option
                    onFieldChange(selectedCard, EditFields.Dropdown, option?.id ?: "")
                },
                isEditable = isEditable(EditFields.Dropdown),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        EditableField(
            value = description.value ?: "",
            onValueChange = {
                description.value = it
                onFieldChange(selectedCard, EditFields.Description, it)
            },
            isEditable = isEditable(EditFields.Description),
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            minLines = 3,
            maxLines = 3,
            fieldContentDescription = stringResource(R.string.description) + " field",
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EditableCheckbox(
                checked = isFragile.value,
                onCheckedChange = {
                    isFragile.value = it
                    onFieldChange(selectedCard, EditFields.IsFragile, it.toString())
                },
                isEditable = isEditable(EditFields.IsFragile),
                modifier = Modifier,
                fieldContentDescription = stringResource(R.string.fragile_checkbox),
            )
            Text(stringResource(R.string.fragile))
            Spacer(modifier = Modifier.weight(1f))
            EditableField(
                value = value.doubleValue.asCurrencyString(),
                onValueChange = {
                    value.doubleValue = it.parseCurrencyToDouble()
                    onFieldChange(selectedCard, EditFields.Value, it)
                },
                isEditable = isEditable(EditFields.Value),
                textStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier,
                fieldContentDescription = stringResource(R.string.value),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDataColumnItem() {
    val selectedCard = remember { mutableStateOf<Item?>(Item(
        id = "1",
        name = "Item 1",
        description = "Description 1 ipsum dolor sit amet ipsum5 lorem5 Description 2 ipsum dolor sit amet ipsum5 lorem5   Description 3 ipsum dolor sit amet ipsum5 lorem5   Description 4 ipsum dolor sit amet ipsum5 lorem5   Description 1 ipsum dolor sit amet ipsum5 lorem5   Description 6 ipsum dolor sit amet ipsum5 lorem5     ",
        value = 10.0,
        isFragile = false,
    )) }

    val onFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList<EditFields>()
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> currentBox //  if(editableFields.contains(field))  currentBox.copy(dropdown = value) else currentBox
                EditFields.ImageUri -> if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }
    val dropdownOptions = Result.Success(
        listOf(
            BoxIdAndName(id = "1", name = "Option 1"),
            BoxIdAndName(id = "2", name = "Option 2"),
            BoxIdAndName(id = "3", name = "Option 3"),
        ))
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    PackItUpAndroidTheme(themeManager) {
        Surface {
            DataColumn(
                editableFields = Item.EDIT_FIELDS,
                onFieldChange = onFieldChange,
                selectedCard = selectedCard,
                dropdownOptions = dropdownOptions,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDataColumnBox() {
    val selectedCard = remember { mutableStateOf<Box?>(Box(
        id = "1",
        name = "Box 1",
        description = "Description 1 ipsum dolor sit amet ipsum5 lorem5 Description 2 ipsum dolor sit amet ipsum5 lorem5   Description 3 ipsum dolor sit amet ipsum5 lorem5   Description 4 ipsum dolor sit amet ipsum5 lorem5   Description 1 ipsum dolor sit amet ipsum5 lorem5   Description 6 ipsum dolor sit amet ipsum5 lorem5     ",
        value = 10.0,
        isFragile = true,
    )) }

    val onFieldChange = fun (element: MutableState<Box?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList<EditFields>()
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> currentBox //  if(editableFields.contains(field))  currentBox.copy(dropdown = value) else currentBox
                EditFields.ImageUri -> currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    val dropdownOptions = Result.Success(
    listOf(
        CollectionIdAndName(id = "1", name = "Option 1"),
        CollectionIdAndName(id = "2", name = "Option 2"),
        CollectionIdAndName(id = "3", name = "Option 3"),
    ))
    PackItUpAndroidTheme(themeManager) {
        Surface {
            DataColumn(
                editableFields = Box.EDIT_FIELDS,
                onFieldChange = onFieldChange,
                selectedCard = selectedCard,
                dropdownOptions = dropdownOptions,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDataColumnCollection() {
    val selectedCard = remember { mutableStateOf<Collection?>(Collection(
        id = "1",
        name = "Collection 1",
        description = "Description 1 ipsum dolor sit amet ipsum5 lorem5 Description 2 ipsum dolor sit amet ipsum5 lorem5   Description 3 ipsum dolor sit amet ipsum5 lorem5   Description 4 ipsum dolor sit amet ipsum5 lorem5   Description 1 ipsum dolor sit amet ipsum5 lorem5   Description 6 ipsum dolor sit amet ipsum5 lorem5     ",
        value = 10.0,
    )) }

    val onFieldChange = fun (element: MutableState<Collection?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        element.value?.let { currentCollection ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentCollection.copy(description = value) else currentCollection
                EditFields.Dropdown -> currentCollection //  if(editableFields.contains(field))  currentCollection.copy(dropdown = value) else currentCollection
                EditFields.ImageUri -> currentCollection
                EditFields.IsFragile -> if(editableFields.contains(field))  currentCollection.copy(isFragile = value.toBoolean()) else currentCollection
                EditFields.Name -> if(editableFields.contains(field))  currentCollection.copy(name = value) else currentCollection
                EditFields.Value -> if(editableFields.contains(field))  currentCollection.copy(value = value.parseCurrencyToDouble()) else currentCollection
            }
            element.value = updatedElement
        }
    }
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    PackItUpAndroidTheme(themeManager) {
        Surface {
            DataColumn(
                editableFields = Collection.EDIT_FIELDS,
                onFieldChange = onFieldChange,
                selectedCard = selectedCard,
            )
        }
    }
}


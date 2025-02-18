package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.parseCurrencyToDouble


/**
 * A composable function to display and edit a card's details.
 *
 * This function renders an elevated card with editable fields, allowing the user
 * to modify the card's data. The card's appearance and editable fields can be
 * customized via the provided parameters.
 *
 * @param iconsContent A composable lambda that provides the content for the card's icon area.
 *                     It uses a [ColumnScope] to allow for column-based layout of the icons.
 * @param onFieldChange A callback function triggered when an editable field's value changes.
 *                      It receives the [MutableState] of the card data, the [EditFields] that was modified and the new value.
 * @param selectedCard A [MutableState] holding the currently selected card's data.
 *                     The type of data must implement [BaseCardData].
 *                     This state should be updated to reflect the current state of the card.
 * @param modifier Modifier to be applied to the card. Defaults to an empty modifier.
 * @param editableFields A [Set] of [EditFields] indicating which fields are editable.
 *                       Defaults to an empty set, meaning no fields are editable.
 *
 * @param D The type of [BaseCardData] being edited.
 * @param onUpdate A lambda function that is called when the user wants to update a card's properties.
 *                  It takes a card and updates the card based on the card type's update function
 *
 * @param dropdownOptions An optional [Result] object representing the state of the dropdown options.
 *                        If provided, a dropdown menu will be displayed, allowing the user to select from the options.
 *                        If `null`, no dropdown menu will be displayed. * @param D the type of data that the card will display. It must inherit from BaseCardData
 *
 * @throws IllegalStateException if [selectedCard] value is null.
 *
 * Example Usage:
 *
 * ```
 *  var cardData by remember { mutableStateOf<MyCardData?>(MyCardData */
@Composable
fun <D: BaseCardData>EditCard(
    iconsContent: @Composable ColumnScope.() -> Unit,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    selectedCard: MutableState<D?>,
    modifier: Modifier = Modifier,
    editableFields: Set<EditFields> = emptySet(),
    dropdownOptions: Result<List<DropdownOptions?>>? = null,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.card_height)),
    ) {
        Row {
            Column {
                iconsContent()
            }
            // TODO: combine with [BaseCard] to remove duplication and use flag to
            // display [DataColumn] in edit mode or not
            DataColumn(
                editableFields = editableFields,
                onFieldChange = onFieldChange,
                selectedCard = selectedCard,
                dropdownOptions = dropdownOptions,
                modifier = Modifier.weight(1f),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEditCard() {
    val selectedCard = remember { mutableStateOf<Item?>(
        Item(
            id = "1",
            name = "Item 1",
            description = "Description 1 ipsum dolor sit amet ipsum5 lorem5 Description 2 ipsum dolor sit amet ipsum5 lorem5   Description 3 ipsum dolor sit amet ipsum5 lorem5   Description 4 ipsum dolor sit amet ipsum5 lorem5   Description 1 ipsum dolor sit amet ipsum5 lorem5   Description 6 ipsum dolor sit amet ipsum5 lorem5     ",
            value = 10.0,
            isFragile = false
        )
    ) }

    val onFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList<EditFields>()
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> if(editableFields.contains(field))  currentBox.copy(boxId = value) else currentBox
                EditFields.ImageUri -> if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }

    EditCard(
        editableFields = Item.EDIT_FIELDS,
        onFieldChange = onFieldChange,
        selectedCard = selectedCard,
        iconsContent = {
            IconBadge(
                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                badgeContentDescription = "Default Item Badge",
                badgeCount = 0,
            )
        },
        dropdownOptions = Result.Success(emptyList())
    )
}

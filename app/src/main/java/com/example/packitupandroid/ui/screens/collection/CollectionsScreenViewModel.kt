package com.example.packitupandroid.ui.screens.collection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.screens.BaseViewModel
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.parseCurrencyToDouble
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * ViewModel for the Collections screen.
 *
 * This ViewModel is responsible for managing the data and state of the Collections screen,
 * using the [CollectionsRepository] to access collection data.
 *
 * @param savedStateHandle The SavedStateHandle to preserve state across configuration changes.
 * @param repository The repository used to access item data.
 * @param defaultDispatcher The coroutine dispatcher to use for background tasks (defaults to [Dispatchers.Default]).
 */
class CollectionsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CollectionsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<Collection>(repository) {

    // TODO: refactor into BaseViewModel
    override fun create(count: Int) {
        val data = List(count) { index -> Collection(name = "Collection ${index + 1}") }
        insert(data)
    }

    /**
     * Updates a specific field of an Item based on user input.
     *
     * This function takes a MutableState holding an optional Item, the field to be modified,
     * and the new value for that field. It then creates a copy of the current Item, updates
     * the specified field with the new value, and updates the MutableState with the modified Item.
     * If the `item.value` is null, it does nothing.
     *
     * @param item A MutableState object that holds an Item? (nullable Item). This represents the item
     *             being edited and will be updated in place.
     * @param field An EditFields enum value specifying which field of the Item should be modified.
     * @param value A String representing the new value for the specified field.
     *              The format of the string depends on the `field` type, see below for the details.
     *
     * Fields Handling:
     *  - **EditFields.Description:** `value` directly sets the `description` of the Item.
     *  - **EditFields.Dropdown:** `value` is used as the new value.
     *     **Note**: "TODO: add dropdown" - this comment is left in the code, as it's a feature to implement.
     *  - **EditFields.ImageUri:** `value` directly sets the `imageUri` of the Item.
     *  - **EditFields.IsFragile:** `value` is parsed to a Boolean using `toBoolean()`. Common cases: "true", "false".
     *  - **EditFields.Name:** `value` directly sets the `name` of the Item.
     *  - **EditFields.Value:** `value` is parsed to a Double using `parseCurrencyToDouble()`, representing a numerical value.
     *
     * Example usage:
     * ```kotlin
     * val itemState = remember { mutableStateOf<Item?>(Item(name = "Old Item", description = "Initial", ...)) }
     * onFieldChange(itemState, EditFields.Name, "New Item Name")
     * // itemState.value will now hold an Item with name = "New Item Name"
     *
     * onFieldChange(itemState, EditFields.Value, "$12.99")
     * // itemState.value will now hold an item with the */
    override fun onFieldChange(element: MutableState<Collection?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> currentBox //  if(editableFields.contains(field))  currentBox.copy(dropdown = value) else currentBox
                EditFields.ImageUri -> currentBox // if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }

    /**
     * Generates a Composable column containing icon badges representing box and item counts.
     *
     * This function creates a vertical layout (Column) with two [IconBadge] composables.
     * The first [IconBadge] displays a drawable icon (ic_launcher_foreground) with the box count.
     * The second [IconBadge] displays a vector icon (Label) with the item count.
     *
     * @param element A collection object that provides the box count and item count.
     *        It is assumed that the `element` object has `boxCount` and `itemCount` properties (e.g. `data class Collection(val boxCount: Int, val itemCount: Int)`)
     * @return A Composable lambda function that represents the column of icon badges.
     *          This can be used within a Compose layout to embed the generated column.
     *
     * Example Usage:
     * ```
     * val myCollection = Collection(boxCount = 5, itemCount = 10)
     * val iconsColumn = generateIconsColumn(myCollection)
     *
     * Column {
     *     iconsColumn()
     * }
     * ```
     *
     * The example above generates a column containing two IconBadges based on the data provided by myCollection
     */
    override fun generateIconsColumn(element: Collection): @Composable (ColumnScope.() -> Unit) {
        return {
            Column {
                IconBadge(
                    image = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
                    badgeContentDescription = "${element.boxCount} Boxes",
                    badgeCount = element.boxCount,
                    type = "boxes",
                )
                IconBadge(
                    image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                    badgeContentDescription = "${element.itemCount} Items",
                    badgeCount = element.itemCount,
                    type = "items",
                )
            }
        }
    }
}

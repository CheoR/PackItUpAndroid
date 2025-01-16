package com.example.packitupandroid.ui.screens.collection

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.CollectionsRepository
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

    init {
        initialize()
    }

    // TODO: refactor into BaseViewModel
    override fun create(count: Int) {
        val data = List(count) { index -> Collection(name = "Collection ${index + 1}") }
        insert(data)
    }

    /**
     * Provides mock data for collections.
     *
     * @return A list of mock [CollectionEntity] objects.
     */
    override fun getMockData(): List<Collection> {
        return emptyList<Collection>()
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
        val editableFields = element.value?.editFields ?: emptyList<EditFields>()
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
}

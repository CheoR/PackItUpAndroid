package com.example.packitupandroid.ui.screens.item

import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.ui.screens.BaseViewModel
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.parseCurrencyToDouble
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * ViewModel for the Items screen.
 *
 * This ViewModel is responsible for managing the data and state of the Items screen,
 * using the [ItemsRepository] to access item data.
 *
 * @param savedStateHandle The SavedStateHandle to preserve state across configuration changes.
 * @param repository The repository used to access item data.
 * @param defaultDispatcher The coroutine dispatcher to use for background tasks (defaults to [Dispatchers.Default]).
 */
class ItemsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ItemsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<Item>(repository) {
    private val boxId: String? = savedStateHandle["boxId"]


//    private val _entityCache = MutableStateFlow<Map<String, BaseCardData>>(emptyMap())
//    private val entityCache: StateFlow<Map<String, BaseCardData>> = _entityCache.asStateFlow()
//    private val entityCacheMap = mutableMapOf<String, BaseCardData>()
//    private val entityCache: Map<String, BaseCardData> get() = entityCacheMap

    // note: this approach may lead to IllegalStateException
    // because the ViewModel might not be fully initialized when the coroutine starts
    // TODO: refactor to call initializeUIState() from method in a LaunchedEffect in screen that uses vw
//    init {
//        viewModelScope.launch(defaultDispatcher) {
//            initializeUIState()
//        }
//    }

    /**
     * Creates and inserts a specified number of `Item` objects into the data store.
     *
     * This function generates a list of `Item` objects, each with a unique name based on its index,
     * and then inserts them into the underlying data storage mechanism (likely a database or repository).
     *
     * The `Item` names are formatted as "Item {index + 1}", where the index starts from 0.
     *
     * Example:
     * If `count` is 3, the function will create three items named:
     *   - "Item 1"
     *   - "Item 2"
     *   - "Item 3"
     *
     * Note: This function is currently marked as a TODO for refactoring into a `BaseViewModel`. This suggests
     * that the functionality might be more generally applicable and could be moved to a shared base class
     * in the future.
     *
     * @param count The number of `Item` objects to create and insert. Must be a non-negative integer.
     * @throws IllegalArgumentException if `count` is negative.
     */// TODO: refactor into BaseViewModel
    override fun create(count: Int) {
        val data = List(count) { index -> Item(name = "Item ${index + 1}") }
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
     * @param card A MutableState object that holds an Item? (nullable Item). This represents the item
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
    override fun onFieldChange(element: MutableState<Item?>, field: EditFields, value: String) {
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
}

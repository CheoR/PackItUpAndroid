package com.example.packitupandroid.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.screens.BaseViewModel
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.parseCurrencyToDouble
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


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
    val dropdownOptions: StateFlow<Result<List<BoxIdAndName?>>> =
        repository.listOfBoxIdsAndNames()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading,
            )

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

    init {
        /*
         * TODO: Review `ItemsScreenViewModel`'s `init` block.
         * - **Reminder:** current approach breaks encapsulation by directly observing `BaseViewModel`'s `elements` in `ItemsScreenViewModel`.
         *   This should be fixed for better design and maintainability or keep as is beause I'm already observing elements in *Screens, so heh?
         * - **Reason for Current Approach:**
         *   - To address a race condition that occurred when applying filtering based on `boxId` in the previous implementation.
         *   - The current approach ensures that filtering is applied when `elements` emits `Result.Success`, indicating that the data is available.
         * - **Recommended Solution:**
         *   - Move the filtering logic to `load()` in `BaseViewModel` to avoid the race condition and maintain encapsulation.
         *   - Use a callback mechanism to allow subclasses to define the filtering logic.
         * - **Priority:** High. This issue should be addressed to improve the design and maintainability of the code.
         *      or come back to it when i get the time
         */
        viewModelScope.launch(defaultDispatcher) {
            elements.collect { result ->
                if (boxId != null && result is Result.Success) {
                    filterElements { elements -> elements.filter { it?.boxId == boxId } }
                }
            }
        }
    }

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
     * @throws IllegalArgumentException if `count` is negative
     */// TODO: refactor into BaseViewModel
    override fun create(count: Int) {
        val data = List(count) { index -> Item(name = "Item ${index + 1}", boxId = boxId) }
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
        val editableFields = element.value?.editFields ?: emptyList()
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

    /**
     * Generates the content for the icons column for an Item.
     *
     * This function returns a composable lambda that, when invoked, displays a column containing an IconBadge.
     * The IconBadge is configured with a default label icon and a badge count of 0.
     *
     * @param element The Item object for which to generate the icons. This parameter is not used in the current implementation but is included for potential future use.
     * @return A composable lambda that, when invoked, displays the icons column. The lambda uses a [ColumnScope] to allow for column-based layout of the icons.
     */
    override fun generateIconsColumn(element: Item): @Composable (ColumnScope.() -> Unit) {
        val image = if(element.imageUri != null) ImageContent.BitmapStringImage(element.imageUri) else ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
        return {
            Column {
                IconBadge(
                    image = image,
                    badgeContentDescription = if(element.imageUri != null) stringResource(R.string.image_description, element.name) else stringResource(R.string.default_item_badge),
                    badgeCount = 0,
                    type = "items",
                )
            }
        }
    }
}

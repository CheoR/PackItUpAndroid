package com.example.packitupandroid.ui.screens.box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.screens.BaseViewModel
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.parseCurrencyToDouble
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * ViewModel for the Boxes screen.
 *
 * This ViewModel is responsible for managing the data and state of the Boxes screen,
 * using the [BoxesRepository] to access item data.
 *
 * @param savedStateHandle The SavedStateHandle to preserve state across configuration changes.
 * @param repository The repository used to access box data.
 * @param defaultDispatcher The coroutine dispatcher to use for background tasks (defaults to [Dispatchers.Default]).
 */
class BoxesScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BoxesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<Box>(repository) {
    private val collectionId: String? = savedStateHandle["collectionId"]
    val dropdownOptions: StateFlow<Result<List<CollectionIdAndName?>>> =
        repository.listOfCollectionIdsAndNames()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading,
            )

    init {
        /*
         * TODO: Review `ItemsScreenViewModel`'s `init` block.
         * - **Reminder:** current approach breaks encapsulation by directly observing `BaseViewModel`'s `elements` in `BoxesScreenViewModel`.
         *   This should be fixed for better design and maintainability or keep as is beause I'm already observing elements in *Screens, so heh?
         * - **Reason for Current Approach:**
         *   - To address a race condition that occurred when applying filtering based on `collectionId` in the previous implementation.
         *   - The current approach ensures that filtering is applied when `elements` emits `Result.Success`, indicating that the data is available.
         * - **Recommended Solution:**
         *   - Move the filtering logic to `load()` in `BaseViewModel` to avoid the race condition and maintain encapsulation.
         *   - Use a callback mechanism to allow subclasses to define the filtering logic.
         * - **Priority:** High. This issue should be addressed to improve the design and maintainability of the code.
         *      or come back to it when i get the time
         */
        viewModelScope.launch(defaultDispatcher) {
            elements.collect { result ->
                if (collectionId != null && result is Result.Success) {
                    filterElements { elements -> elements.filter { it?.collectionId == collectionId } }
                }
            }
        }
    }


    // TODO: refactor into BaseViewModel
    override fun create(count: Int) {
        val data = List(count) { index -> Box(name = "Box ${index + 1}", collectionId = collectionId) }
        insert(data)
    }

    /**
     * Updates the properties of a Box object within a MutableState based on the specified field and value.
     *
     * This function is designed to handle changes to individual fields within a Box object. It checks if the
     * specified field is editable (present in the Box's `editFields` list) before applying the update. If the
     * field is not editable, the Box object remains unchanged.
     *
     * @param element A MutableState holding the Box object that needs to be updated. The Box can be null.
     * @param field The EditFields enum value representing the field to be updated within the Box.
     * @param value The new string value for the specified field. This value will be parsed or converted
     *              to the appropriate type before being applied to the Box.
     *
     * @throws NumberFormatException if the value for EditFields.Value cannot be parsed into a Double.
     *
     * @see EditFields
     * @see Box
     * @see MutableState
     */
    override fun onFieldChange(element: MutableState<Box?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        println("field: $field, value: $value editableFields: $editableFields isEditable: ${editableFields.contains(field)}")
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> if(editableFields.contains(field))  currentBox.copy(collectionId = value) else currentBox
                EditFields.ImageUri -> currentBox // if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }

    /**
     * Generates a composable column containing an icon badge.
     *
     * This function creates a [Column] composable that displays an [IconBadge] representing
     * information about the provided [Box] element. The icon used is the "Label" icon
     * from `Icons.AutoMirrored.Filled`, and the badge content displays the number of items
     * contained in the [Box] along with a descriptive text.
     *
     * @param element The [Box] element containing the data to be displayed in the icon badge.
     *                The `itemCount` property of this object is used to determine the badge's content.
     * @return A composable function that, when executed, will render the column containing the icon badge.
     *         The function is defined within a [ColumnScope], allowing for easy composition within
     *         other Column layouts.
     */
    override fun generateIconsColumn(element: Box): @Composable (ColumnScope.() -> Unit) {
        return {
            Column {
                IconBadge(
                    image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                    badgeContentDescription = "${element.itemCount} Items",
                    badgeCount = element.itemCount,
                )
            }
        }
    }
}

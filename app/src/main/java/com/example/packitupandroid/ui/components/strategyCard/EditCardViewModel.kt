package com.example.packitupandroid.ui.components.strategyCard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.R
import com.example.packitupandroid.data.database.entities.toBox
import com.example.packitupandroid.data.database.entities.toCollection
import com.example.packitupandroid.data.database.entities.toItem
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.ui.components.card.ColumnIcon
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditFields
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


sealed class ElementType {
    data object Item : ElementType()
    data object Box : ElementType()
    data object Collection : ElementType()
}

class EditCardViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val boxesRepository: BoxesRepository,
    private val collectionsRepository: CollectionsRepository,
    id: String? = null,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val cardType: CardType = CardType.Default
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditCardUiState("", "", ""))
    val uiState: StateFlow<EditCardUiState> = _uiState.asStateFlow()

    // note: this approach may lead to IllegalStateException
    // because the ViewModel might not be fully initialized when the coroutine starts
    // TODO: refactor to call initializeUIState() from method in a LaunchedEffect in screen that uses vw
    init {
        id?.let { getElement(id) }
    }

    private fun getElement(id: String) {
        viewModelScope.launch(defaultDispatcher) {
            val (element, selections, iconsContent) = when (cardType) {
                CardType.Item -> {
                    val element = itemsRepository.getItem(id)?.toItem()
                    val currentSelection = boxesRepository.getQueryBox(element?.boxId ?: "")?.name
                    val dropdownSelections = boxesRepository.getDropdownSelections()
                        .map { list -> list.map { it.toString() } }
                        .first()
                    val iconsContent = @Composable {
                        IconBadge(
                            // TODO: GET ITEM IMAGE IF NO IMAGE FALLBACK TO DEFAULT LABEL
                            image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                            badgeCount = 0,
                            badgeContentDescription = "Item Image Placeholder"
                        )
                    }
                    Triple(element, Pair(currentSelection, dropdownSelections), iconsContent)
                }
                CardType.Box -> {
                    val element = boxesRepository.getBox(id)?.toBox()
                    val currentSelection = collectionsRepository.getQueryCollection(element?.collectionId ?: "")?.name
                    val dropdownSelections = collectionsRepository.getDropdownSelections()
                        .map { list -> list.map { it.toString() } }
                        .first()
                    val itemCount = boxesRepository.getQueryBox(id)?.item_count
                    val iconsContent = @Composable {
                        IconBadge(
                            image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                            badgeCount = itemCount ?: 0,
                            badgeContentDescription = "Number of items"
                        )
                    }
                    Triple(element, Pair(currentSelection, dropdownSelections), iconsContent)
                }
                CardType.Collection -> {
                    val element = collectionsRepository.getCollection(id)?.toCollection()
                    val queryCollection = collectionsRepository.getQueryCollection(id)
                    val itemCount = queryCollection?.item_count
                    val boxCount = queryCollection?.box_count
                    val iconsContent = @Composable {
                        IconBadge(
                            image = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
                            badgeCount = boxCount ?: 0,
                            badgeContentDescription = "Number of boxes"
                        )
                        IconBadge(
                            image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                            badgeCount = itemCount ?: 0,
                            badgeContentDescription = "Number of items"
                        )
                    }
                    Triple(element, Pair(null, emptyList<String?>()), iconsContent)
                }
                else -> throw IllegalArgumentException("CardType not supported")
            }

            element?.let {
                _uiState.value = EditCardUiState(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    isFragile = it.isFragile,
                    value = it.value,
                    editFields = it.editFields,
                    currentSelection = selections.first,
                    dropdownSelections = selections.second,
                    iconsContent = { iconsContent() },
                )
            }
        }
    }

    fun onFieldChange(field: EditFields, value: String) {
        when(field) {
            EditFields.Name -> _uiState.value = _uiState.value.copy(name = value)
            EditFields.Dropdown -> _uiState.value = _uiState.value.copy(currentSelection = value)
            EditFields.Description -> _uiState.value = _uiState.value.copy(description = value)
            EditFields.IsFragile -> _uiState.value = _uiState.value.copy(isFragile = value.toBoolean())
            EditFields.Value -> _uiState.value = _uiState.value.copy(value = value.toDoubleOrNull() ?: 0.0)
            EditFields.ImageUri -> TODO()
        }
    }

    fun updateElement(element: BaseCardData) {
        viewModelScope.launch(defaultDispatcher) {
            when (cardType) {
                CardType.Item -> itemsRepository.updateItem((element as Item).toEntity())
                CardType.Box -> boxesRepository.updateBox((element as Box).toEntity())
                CardType.Collection -> collectionsRepository.updateCollection((element as Collection).toEntity())
                else -> throw IllegalArgumentException("CardType not supported")
            }
        }
    }
}

data class EditCardUiState(
    val id: String,
    val name: String,
    val description: String? = "",
    val isFragile: Boolean = false,
    val value: Double = 0.0,
    val editFields: Set<EditFields> = emptySet(),
    val currentSelection: String? = null,
    val dropdownSelections: List<String?> = emptyList(),
    val iconsContent: @Composable ColumnIcon.() -> Unit = {},
)

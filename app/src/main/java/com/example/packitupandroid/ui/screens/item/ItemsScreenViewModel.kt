package com.example.packitupandroid.ui.screens.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.database.entities.toItem
import com.example.packitupandroid.data.database.entities.updateWith
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.utils.USE_MOCK_DATA
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val boxId: String? = savedStateHandle["boxId"]
    private val _uiState = MutableStateFlow(ItemsPackItUpUiState())
    val uiState: StateFlow<ItemsPackItUpUiState> = _uiState.asStateFlow()

//    private val _entityCache = MutableStateFlow<Map<String, BaseCardData>>(emptyMap())
//    private val entityCache: StateFlow<Map<String, BaseCardData>> = _entityCache.asStateFlow()
//    private val entityCacheMap = mutableMapOf<String, BaseCardData>()
//    private val entityCache: Map<String, BaseCardData> get() = entityCacheMap

    init {
        viewModelScope.launch(defaultDispatcher) {
            initializeUIState()
        }
    }

    private suspend fun initializeUIState(useMockData: Boolean = USE_MOCK_DATA) {
        if (useMockData) {
            // populate data from file for future tutorial
            itemsRepository.getAllItemsStream().map { itemEntities ->
                itemEntities.map { it.toItem() }
            }.map { items ->
                ItemsPackItUpUiState(
                    elements = items,
                    result = Result.Complete(items),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemsPackItUpUiState()
            ).collect { newState ->
                _uiState.value = newState
                boxId?.let { filterByBoxId(boxId) }
            }
        } else {
            try {
                // TODO - fix user regular db
                itemsRepository.clearAllItems()
                itemsRepository.getAllItemsStream().map { itemEntities ->
                    itemEntities.map { it.toItem() }
                }.map { items ->
                    ItemsPackItUpUiState(
                        elements = items,
                        result = Result.Complete(items),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = ItemsPackItUpUiState()
                ).collect { newState ->
                    _uiState.value = newState
                    boxId?.let { filterByBoxId(boxId) }
                }
            } catch (e: Exception) {
                _uiState.value = ItemsPackItUpUiState(
                    result = Result.Error(e.message ?: "Unknown error")
                )
            }
        }
    }

    fun create(count: Int = 0) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Item(name = "Item ${index + 1}", boxId = boxId).toEntity()
        }

        viewModelScope.launch(defaultDispatcher) {
            saveItem(entities)
        }
    }

    fun update(element: Item) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch(defaultDispatcher) {
            val itemEntity = getItem(element.id)
            if (itemEntity != null) {
                val updatedItemEntity = itemEntity.updateWith(element.toEntity())
                updateItem(updatedItemEntity)
            }
        }
    }

    fun destroy(element: Item) {
        viewModelScope.launch(defaultDispatcher) {
            destroyItem(element.toEntity())
        }
    }

    // TODO: read up on pros/cons of fetching from db vs uiState
    fun getAllItems(): List<Item> {
        var itemList: List<Item> = emptyList()
        viewModelScope.launch(defaultDispatcher) {
            itemList = getItems()
        }
        return itemList
    }

    private suspend fun getItems(): List<Item> {
        return itemsRepository.getAllItemsStream().first().map { it.toItem() }
    }

//    private suspend fun getItem(id: String) : ItemEntity? {
//        return itemsRepository.getItem(id)
//    }
    private fun getItem(id: String) : ItemEntity? {
        return uiState.value.elements.find { it.id == id }?.toEntity()
    }

    private suspend fun saveItem(entities: List<ItemEntity>) {
        if(entities.size == 1) {
            itemsRepository.insertItem(entities.first())
        } else {
            itemsRepository.insertAll(entities)
        }
    }

    private suspend fun updateItem(entity: ItemEntity) {
        itemsRepository.updateItem(entity)
    }

    private suspend fun destroyItem(entity: ItemEntity) {
        itemsRepository.deleteItem(entity)
    }

    private fun filterByBoxId(boxId: String) {
        val filteredElement = uiState.value.elements.filter { it.boxId == boxId }
        _uiState.value = ItemsPackItUpUiState(
            elements = filteredElement,
            result = Result.Complete(filteredElement),
        )
    }

    /**
     * A companion object helps us by having a single instance of an object that is used by everyone
     * without needing to create a new instance of an expensive object. This is an implementation
     * detail, and separating it lets us make changes without impacting other parts of the app's code.
     *
     * The APPLICATION_KEY is part of the ViewModelProvider.AndroidViewModelFactory.Companion object
     * and is used to find the app's MarsPhotosApplication object, which has the container property
     * used to retrieve the repository used for dependency injection.
     */
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemsPackItUpUiState(
    override val elements: List<Item> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState


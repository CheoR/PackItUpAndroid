package com.example.packitupandroid.ui.screens.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.database.entities.toItem
import com.example.packitupandroid.data.database.entities.updateWith
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.ui.screens.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemsScreenViewModel (
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<ItemsPackItUpUiState, Item, ItemEntity>() {
    private val boxId: String? = savedStateHandle["boxId"]

//    private val _entityCache = MutableStateFlow<Map<String, BaseCardData>>(emptyMap())
//    private val entityCache: StateFlow<Map<String, BaseCardData>> = _entityCache.asStateFlow()
//    private val entityCacheMap = mutableMapOf<String, BaseCardData>()
//    private val entityCache: Map<String, BaseCardData> get() = entityCacheMap

    init {
        viewModelScope.launch(defaultDispatcher) {
            initializeUIState()
        }
    }

    override fun initialState(): ItemsPackItUpUiState {
        return  ItemsPackItUpUiState(
            elements = emptyList(),
            result = Result.Loading,
        )
    }

    override suspend fun initializeUIState(useMockData: Boolean) {
        if (useMockData) {
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
                itemsRepository.getAllItemsStream().collect {newState ->
                    val items = newState.map { it.toItem() }
                    _uiState.value = ItemsPackItUpUiState(
                        elements = items,
                        result = Result.Complete(items),
                    )
                }
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

    override fun create(count: Int) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Item(name = "Item ${index + 1}", boxId = boxId).toEntity()
        }

        viewModelScope.launch(defaultDispatcher) {
            createEntity(entities)
        }
    }

    override fun update(element: Item) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch(defaultDispatcher) {
            val itemEntity = getEntity(element.id)
            if (itemEntity != null) {
                val updatedItemEntity = itemEntity.updateWith(element.toEntity())
                updateEntity(updatedItemEntity)
            }
        }
    }

    override fun destroy(element: Item) {
        viewModelScope.launch(defaultDispatcher) {
            destroyEntity(element.toEntity())
        }
    }

    // TODO: read up on pros/cons of fetching from db vs uiState
    override fun getAllElements(): List<Item> {
        var itemList: List<Item> = emptyList()
        viewModelScope.launch(defaultDispatcher) {
            itemList = getElements()
        }
        return itemList
    }

    override suspend fun createEntity(entities: List<ItemEntity>) {
        if(entities.size == 1) {
            itemsRepository.insertItem(entities.first())
        } else {
            itemsRepository.insertAll(entities)
            _uiState.value = ItemsPackItUpUiState(
                elements = uiState.value.elements + entities.map { it.toItem() },
                result = Result.Complete(uiState.value.elements + entities.map { it.toItem() }),
            )
        }
    }

    override suspend fun updateEntity(entity: ItemEntity) {
        itemsRepository.updateItem(entity)
    }

    override suspend fun destroyEntity(entity: ItemEntity) {
        itemsRepository.deleteItem(entity)
    }

    override suspend fun getElements(): List<Item> {
        return itemsRepository.getAllItemsStream().first().map { it.toItem() }
    }

    override suspend fun getEntity(id: String): ItemEntity? {
        return uiState.value.elements.find { it.id == id }?.toEntity()
    }

    private fun filterByBoxId(boxId: String) {
        val filteredElement = uiState.value.elements.filter { it.boxId == boxId }
        _uiState.value = ItemsPackItUpUiState(
            elements = filteredElement,
            result = Result.Complete(filteredElement),
        )
    }
}

data class ItemsPackItUpUiState(
    override val elements: List<Item> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

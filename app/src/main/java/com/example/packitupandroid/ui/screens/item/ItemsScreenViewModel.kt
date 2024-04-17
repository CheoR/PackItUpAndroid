package com.example.packitupandroid.ui.screens.item

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemsScreenViewModel(
    private val itemsRepository: ItemsRepository,
) : ViewModel() {
    private val isUseMockData = true

    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()

//    private val _entityCache = MutableStateFlow<Map<String, BaseCardData>>(emptyMap())
//    private val entityCache: StateFlow<Map<String, BaseCardData>> = _entityCache.asStateFlow()
//    private val entityCacheMap = mutableMapOf<String, BaseCardData>()
//    private val entityCache: Map<String, BaseCardData> get() = entityCacheMap

    init {
        viewModelScope.launch {
            initializeUIState(isUseMockData)
        }
    }

    private fun initializeUIState(useMockData: Boolean = false) {
        if (useMockData) {
            // populate data from file for future tutorial
            viewModelScope.launch {
                itemsRepository.getAllItemsStream().map { itemEntities ->
                    PackItUpUiState(
                        result = Result.Complete(
                            elements = itemEntities.map { it.toItem() },
                        )
                    )
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = PackItUpUiState()
                )
                .collect { newState -> _uiState.value = newState }
            }
        } else {
            // user regular db
            viewModelScope.launch {
                try {
                    // TODO: REPLACE TO USE DIFFERENT DB
                    itemsRepository.clearAllItems()
                    itemsRepository.getAllItemsStream().collect { itemEntities ->
                        _uiState.value = PackItUpUiState(
                            result = Result.Complete(
                                elements = itemEntities.map { it.toItem() },
                            )
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = PackItUpUiState(
                        result = Result.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    fun create(count: Int = 0) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Item(name = "Item ${index + 1}", boxId="af0d2c01-4fe6-46b2-8b7a-3c5429eb9e99").toEntity()
        }

        viewModelScope.launch {
            saveItem(entities)
        }
    }

    fun update(element: Item) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch {
            val itemEntity = getItem(element.id)
            if (itemEntity != null) {
                val updatedItemEntity = itemEntity.updateWith(element.toEntity())
                updateItem(updatedItemEntity)
            }
        }
    }

    fun destroy(element: Item) {
        viewModelScope.launch {
            destroyItem(element.toEntity())
        }
    }

    private suspend fun getItem(id: String) : ItemEntity? {
        return itemsRepository.getItem(id)
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


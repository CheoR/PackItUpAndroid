package com.example.packitupandroid.ui.screens.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.updateWith
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.toCollection
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.utils.USE_MOCK_DATA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class CollectionsScreenViewModel(
    private val collectionsRepository: CollectionsRepository,
) : ViewModel() {
//    private val _uiState = MutableStateFlow(CollectionsScreenUiState())
//    val uiState: StateFlow<CollectionsScreenUiState> = _uiState.asStateFlow()
    private val _uiState = MutableStateFlow(CollectionsScreenUiState())
    val uiState: StateFlow<CollectionsScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeUIState()
        }
    }

    private suspend fun initializeUIState(isUseMockData: Boolean = USE_MOCK_DATA) {
        if (isUseMockData) {
            viewModelScope.launch {
                collectionsRepository.getAllCollectionsStream().map { collectionEntities ->
                    collectionEntities.map { it.toCollection() }
                }.map { collections ->
                    CollectionsScreenUiState(
                        elements = collections,
                        result = Result.Complete(collections),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = CollectionsScreenUiState()
                ).collect { newState -> _uiState.value = newState }
            }
        } else {
            // TODO - fix user regular db
            try {
                // TODO: REPLACE TO USE DIFFERENT DB
                collectionsRepository.clearAllCollections()
                viewModelScope.launch {
                    collectionsRepository.getAllCollectionsStream().map { collectionEntities ->
                        collectionEntities.map { it.toCollection() }
                    }.map { collections ->
                        CollectionsScreenUiState(
                            elements = collections,
                            result = Result.Complete(collections),
                        )
                    }.stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                        initialValue = CollectionsScreenUiState()
                    ).collect { newState -> _uiState.value = newState }
                }
            } catch (e: Exception) {
                _uiState.value = CollectionsScreenUiState(
                    result = Result.Error(e.message ?: "Unknown error")
                )
            }
        }
    }

    fun create(count: Int = 0) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Collection(name = "Collection ${index + 1}").toEntity()
        }

        viewModelScope.launch {
            saveCollection(entities)
        }
    }

    fun update(element: Collection) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch {
            val collectionEntity = getCollection(element.id)
            if (collectionEntity != null) {
                val updatedCollectionEntity = collectionEntity.updateWith(element.toEntity())
                updateCollection(updatedCollectionEntity)
            }
        }
    }

    fun destroy(element: Collection) {
        viewModelScope.launch {
            destroyCollection(element.toEntity())
        }
    }

    fun getAllCollections(): List<Collection> {
        var collectionList: List<Collection> = emptyList()
        viewModelScope.launch {
            collectionList = getCollections()
        }
        return collectionList
    }

    private suspend fun getCollections(): List<Collection> {
        val collectionEntitiesFlow = collectionsRepository.getAllCollectionsStream().map { list -> list.map { it.toCollection() } }
        return collectionEntitiesFlow.toList().flatten()
    }

    suspend fun getCollection(id: String) : CollectionEntity? {
        return collectionsRepository.getCollection(id)
    }

    private suspend fun saveCollection(entities: List<CollectionEntity>) {
        if(entities.size == 1) {
            collectionsRepository.insertCollection(entities.first())
        } else {
            collectionsRepository.insertAll(entities)
        }
    }

    private suspend fun updateCollection(entity: CollectionEntity) {
        collectionsRepository.updateCollection(entity)
    }

    private suspend fun destroyCollection(entity: CollectionEntity) {
        collectionsRepository.deleteCollection(entity)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CollectionsScreenUiState(
    override val elements: List<Collection> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

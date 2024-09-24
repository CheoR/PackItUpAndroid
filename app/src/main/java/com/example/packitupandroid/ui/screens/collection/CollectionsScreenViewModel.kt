package com.example.packitupandroid.ui.screens.collection

import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.updateWith
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.toCollection
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.ui.screens.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class CollectionsScreenViewModel(
    private val collectionsRepository: CollectionsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
): BaseViewModel<CollectionsScreenUiState, Collection, CollectionEntity>() {

    init {
        viewModelScope.launch(defaultDispatcher) {
            initializeUIState()
        }
    }

    override fun initialState(): CollectionsScreenUiState {
        return CollectionsScreenUiState(
            elements = emptyList(),
            result = Result.Loading,
        )
    }

    override suspend fun initializeUIState(useMockData: Boolean) {
        if (useMockData) {
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
        } else {
            // TODO - fix user regular db
            try {
                // TODO: REPLACE TO USE DIFFERENT DB
                collectionsRepository.clearAllCollections()
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
            } catch (e: Exception) {
                _uiState.value = CollectionsScreenUiState(
                    result = Result.Error(e.message ?: "Unknown error")
                )
            }
        }
    }

    override fun create(count: Int) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Collection(name = "Collection ${index + 1}").toEntity()
        }

        viewModelScope.launch(defaultDispatcher) {
            createEntity(entities)
        }
    }

    override fun update(element: Collection) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch(defaultDispatcher) {
            val collectionEntity = getEntity(element.id)
            if (collectionEntity != null) {
                val updatedCollectionEntity = collectionEntity.updateWith(element.toEntity())
                updateEntity(updatedCollectionEntity)
            }
        }
    }

    override fun destroy(element: Collection) {
        viewModelScope.launch(defaultDispatcher) {
            destroyEntity(element.toEntity())
        }
    }

    override fun getAllElements(): List<Collection> {
        var collectionList: List<Collection> = emptyList()
        viewModelScope.launch(defaultDispatcher) {
            collectionList = getElements()
        }
        return collectionList
    }

    override suspend fun createEntity(entities: List<CollectionEntity>) {
        if(entities.size == 1) {
            collectionsRepository.insertCollection(entities.first())
        } else {
            collectionsRepository.insertAll(entities)
        }
    }

    override suspend fun updateEntity(entity: CollectionEntity) {
        collectionsRepository.updateCollection(entity)
    }

    override suspend fun destroyEntity(entity: CollectionEntity) {
        collectionsRepository.deleteCollection(entity)
    }

    override suspend fun getElements(): List<Collection> {
        val collectionEntitiesFlow = collectionsRepository
            .getAllCollectionsStream().map { list -> list.map { it.toCollection() } }
        return collectionEntitiesFlow.toList().flatten()
    }

    override suspend fun getEntity(id: String): CollectionEntity? {
        return collectionsRepository.getCollection(id)
    }

}

data class CollectionsScreenUiState(
    override val elements: List<Collection> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

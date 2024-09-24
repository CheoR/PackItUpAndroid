package com.example.packitupandroid.ui.screens.box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.toBox
import com.example.packitupandroid.data.database.entities.updateWith
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.toBox
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.ui.screens.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BoxesScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val boxesRepository: BoxesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : BaseViewModel<BoxesScreenUiState, Box, BoxEntity>() {
    private val collectionId: String? = savedStateHandle["collectionId"]

    init {
        viewModelScope.launch(defaultDispatcher) {
            initializeUIState()
        }
    }

    override fun initialState(): BoxesScreenUiState {
        return BoxesScreenUiState(
            elements = emptyList(),
            result = Result.Loading,
        )
    }

    override suspend fun initializeUIState(useMockData: Boolean) {
        if (useMockData) {
            boxesRepository.getAllBoxesStream().map { boxEntities ->
                boxEntities.map { it.toBox() }
            }.map { boxes ->
                BoxesScreenUiState(
                    elements = boxes,
                    result = Result.Complete(boxes),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BoxesScreenUiState()
            ).collect {
                    newState -> _uiState.value = newState
                collectionId?.let { filterByCollectionId(collectionId) }
            }
        } else {
            // TODO - fix user regular db
            try {
                // TODO: REPLACE TO USE DIFFERENT DB
                boxesRepository.clearAllBoxes()
                boxesRepository.getAllBoxesStream().collect { newState ->
                    val boxes = newState.map { it.toBox() }
                    _uiState.value = BoxesScreenUiState(
                        elements = boxes,
                        result = Result.Complete(boxes),
                    )
                }
                boxesRepository.getAllBoxesStream().map { boxEntities ->
                    boxEntities.map { it.toBox() }
                }.map { boxes ->
                    BoxesScreenUiState(
                        elements = boxes,
                        result = Result.Complete(boxes),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = BoxesScreenUiState()
                ).collect {
                        newState -> _uiState.value = newState
                    collectionId?.let { filterByCollectionId(collectionId) }
                }
            } catch (e: Exception) {
                _uiState.value = BoxesScreenUiState(
                    result = Result.Error(e.message ?: "Unknown error")
                )
            }
        }
    }

    override fun create(count: Int) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Box(name = "Box ${index + 1}", collectionId = collectionId).toEntity()
        }

        // when using viewModelScope.launch and don't pass a Dispatcher to launch,
        // any coroutines launched from viewModelScope run in the main thread.
        viewModelScope.launch(defaultDispatcher) {
            createEntity(entities)
        }
    }

    override fun update(element: Box) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch(defaultDispatcher) {
            val boxEntity = getEntity(element.id)
            if (boxEntity != null) {
                val updatedBoxEntity = boxEntity.updateWith(element.toEntity())
                updateEntity(updatedBoxEntity)
            }
        }
    }

    override fun destroy(element: Box) {
        viewModelScope.launch(defaultDispatcher) {
            destroyEntity(element.toEntity())
        }
    }

    override fun getAllElements(): List<Box> {
        var boxList: List<Box> = emptyList()
        viewModelScope.launch(defaultDispatcher) {
            boxList = getElements()
        }
        return boxList
    }

    override suspend fun createEntity(entities: List<BoxEntity>) {
        if (entities.size == 1) {
            boxesRepository.insertBox(entities.first())
        } else {
            boxesRepository.insertAll(entities)
            _uiState.value = BoxesScreenUiState(
                elements = uiState.value.elements + entities.map { it.toBox() },
                result = Result.Complete(uiState.value.elements + entities.map { it.toBox() }),
            )
        }
    }

    override suspend fun updateEntity(entity: BoxEntity) {
        boxesRepository.updateBox(entity)
    }

    override suspend fun destroyEntity(entity: BoxEntity) {
        boxesRepository.deleteBox(entity)
    }

    override suspend fun getElements(): List<Box> {
        return boxesRepository.getAllBoxesStream().first().map { it.toBox() }
    }

    override suspend fun getEntity(id: String): BoxEntity? {
        return boxesRepository.getBox(id)
    }

    private fun filterByCollectionId(id: String) {
        val filteredElements = uiState.value.elements.filter { it.collectionId == id }
        _uiState.value = BoxesScreenUiState(
            elements = filteredElements,
            result = Result.Complete(filteredElements)
        )
    }
}

data class BoxesScreenUiState(
    override val elements: List<Box> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

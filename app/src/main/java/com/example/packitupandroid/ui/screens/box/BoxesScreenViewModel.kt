package com.example.packitupandroid.ui.screens.box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.updateWith
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.toBox
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.BoxesRepository
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

class BoxesScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val boxesRepository: BoxesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val collectionId: String? = savedStateHandle["collectionId"]
    private val _uiState = MutableStateFlow(BoxesScreenUiState())
    val uiState: StateFlow<BoxesScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            initializeUIState()
        }
    }

    private suspend fun initializeUIState(isUseMockData: Boolean = USE_MOCK_DATA) {
        if (isUseMockData) {
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

    fun create(count: Int = 0) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Box(name = "Box ${index + 1}", collectionId = collectionId).toEntity()
        }

        // when using viewModelScope.launch and don't pass a Dispatcher to launch,
        // any coroutines launched from viewModelScope run in the main thread.
        viewModelScope.launch(defaultDispatcher) {
            saveBox(entities)
        }
    }

    fun update(element: Box) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        val boxEntity = getBox(element.id)

        if (boxEntity != null) {
            val updatedBoxEntity = boxEntity.updateWith(element.toEntity())
            viewModelScope.launch(defaultDispatcher) {
                updateBox(updatedBoxEntity)
            }
        }
    }

    fun destroy(element: Box) {
        viewModelScope.launch(defaultDispatcher) {
            destroyBox(element.toEntity())
        }
    }

    fun getAllBoxes(): List<Box> {
        var boxList: List<Box> = emptyList()
        viewModelScope.launch(defaultDispatcher) {
            boxList = getBoxes()
        }
        return boxList
    }

    private suspend fun getBoxes(): List<Box> {
        return boxesRepository.getAllBoxesStream().first().map { it.toBox() }
    }

    private fun getBox(id: String): BoxEntity? {
        var box: BoxEntity? = null
        viewModelScope.launch(defaultDispatcher) {
            box = boxesRepository.getBox(id)
        }
        return box
    }

    private suspend fun saveBox(entities: List<BoxEntity>) {
        if (entities.size == 1) {
            boxesRepository.insertBox(entities.first())
        } else {
            boxesRepository.insertAll(entities)
        }
    }

    private suspend fun updateBox(entity: BoxEntity) {
        boxesRepository.updateBox(entity)
    }


    private suspend fun destroyBox(entity: BoxEntity) {
        boxesRepository.deleteBox(entity)
    }

    private fun filterByCollectionId(id: String) {
        val filteredElements = uiState.value.elements.filter { it.collectionId == id }
        _uiState.value = BoxesScreenUiState(
            elements = filteredElements,
            result = Result.Complete(filteredElements)
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class BoxesScreenUiState(
    override val elements: List<Box> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

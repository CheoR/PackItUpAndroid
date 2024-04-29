package com.example.packitupandroid.ui.screens.box

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class BoxesScreenViewModel(
    private val boxesRepository: BoxesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BoxesScreenUiState())
    val uiState: StateFlow<BoxesScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
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
            ).collect { newState -> _uiState.value = newState }
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
                ).collect { newState -> _uiState.value = newState }
            } catch (e: Exception) {
                _uiState.value = BoxesScreenUiState(
                    result = Result.Error(e.message ?: "Unknown error")
                )
            }
        }
    }

    fun create(count: Int = 0) {
        val entities = (0 until count ).mapIndexed { index, _ ->
            Box(name = "Box ${index + 1}").toEntity()
        }

        viewModelScope.launch {
            saveBox(entities)
        }
    }

    fun update(element: Box) {
        // TODO: Fix FOR JUST PASSING STRING INSTEAD OF ENTIRE ELEMENT
        viewModelScope.launch {
            val boxEntity = getBox(element.id)
            if (boxEntity != null) {
                val updatedBoxEntity = boxEntity.updateWith(element.toEntity())
                updateBox(updatedBoxEntity)
            }
        }
    }

    fun destroy(element: Box) {
        viewModelScope.launch {
            destroyBox(element.toEntity())
        }
    }

    fun getAllBoxes(): List<Box> {
        var boxList: List<Box> = emptyList()
        viewModelScope.launch {
            boxList = getBoxes()
        }
        return boxList
    }

    private suspend fun getBoxes(): List<Box> {
        val boxEntitiesFlow =
            boxesRepository.getAllBoxesStream().map { list -> list.map { it.toBox() } }
        return boxEntitiesFlow.toList().flatten()
    }

    suspend fun getBox(id: String) : BoxEntity? {
        return boxesRepository.getBox(id)
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class BoxesScreenUiState(
    override val elements: List<Box> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

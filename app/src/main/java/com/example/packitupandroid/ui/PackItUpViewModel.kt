package com.example.packitupandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.packitupandroid.PackItUpApplication
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Summary
import com.example.packitupandroid.repository.DataRepository
import com.example.packitupandroid.repository.LocalDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PackItUpViewModel(
    private val repository: DataRepository = LocalDataRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (_uiState.value.items.isEmpty()) {
                loadData(repository::loadItems) { items -> _uiState.value.copy(items = items) }
            }
            if (_uiState.value.boxes.isEmpty()) {
                loadData(repository::loadBoxes) { boxes -> _uiState.value.copy(boxes = boxes) }
            }
            if (_uiState.value.collections.isEmpty()) {
                loadData(repository::loadCollections) { collections -> _uiState.value.copy(collections = collections) }
            }
        }
    }

    fun createElement(element: BaseCardData, count: Int?) {
        createEntity(element, count)
    }
    fun updateElement(element: BaseCardData) {
        updateEntity(element)
    }
    fun destroyElement(element: BaseCardData) {
        destroyEntity(element)
    }

    private suspend inline fun <reified T> loadData(
        crossinline loader: suspend () -> List<T>,
        updateState: (List<T>) -> PackItUpUiState
    ) {
        try {
            val data = loader()
            _uiState.value = updateState(data)
        } catch (e: Exception) {
            emptyList<BaseCardData>()
        }
    }

    private inline fun <reified T : BaseCardData> getEntity(id: String): T {
        return uiState.value.items
            .plus(uiState.value.boxes)
            .plus(uiState.value.collections)
            .firstOrNull { it.id == id } as T
    }

    private fun <T: BaseCardData> createEntity(element: T, count: Int? = 0) {
        if (count == null) {
            throw IllegalArgumentException("Cannot create entity with null count")
        }

        if (element is Summary) {
            throw IllegalArgumentException("Element cannot be Summary")
        }

        val newElements =  (0 until count).mapIndexed { index, _ ->
            val newElement = when(element) {
                is Item -> Item(name="Item ${index + 1}")
                is Box -> Box(name="Box ${index + 1}")
                is Collection -> Collection(name="Collection ${index + 1}")
                else -> throw Exception("Could not create element")
            }
            newElement as T
        } as MutableList<T>

        val newState = when (element) {
            is Item -> _uiState.value.copy(
                items = _uiState.value.items + newElements as List<Item>
            )
            is Box -> _uiState.value.copy(
                boxes =  _uiState.value.boxes + newElements as List<Box>
            )
            is Collection -> _uiState.value.copy(
                collections = _uiState.value.collections + newElements as List<Collection>
            )
            else -> throw Exception("Cannot create element")
        }
        _uiState.value = newState
    }

    private inline fun <reified T : BaseCardData> updateEntity(element: T) {
        val elementToUpdate = getEntity<T>(element.id)

        val newState = when(element) {
            is Item -> _uiState.value.copy(
                items = uiState.value.items.map { if (it.id == element.id) element else it }
            )
            is Box -> _uiState.value.copy(
                boxes = uiState.value.boxes.map { if (it.id == element.id) element else it }
            )
            is Collection -> _uiState.value.copy(
                collections = uiState.value.collections.map { if (it.id == element.id) element else it }
            )
            else -> throw IllegalArgumentException("Invalid element type")
        }

        _uiState.value = newState

        if(elementToUpdate.value != element.value || elementToUpdate.isFragile != element.isFragile) {
            notifyUpdateToParent(element)
        }
    }

    private inline fun <reified T : BaseCardData> destroyEntity(element: T) {
        val entityToDelete = getEntity<T>(element.id)
        val relatedEntities = when (entityToDelete) {
            is Box -> entityToDelete.items
            is Collection -> entityToDelete.boxes.flatMap { it.items } + entityToDelete.boxes
            else -> emptyList<T>() // Do nothing with Item or Summary
        }

        relatedEntities.forEach { destroyElement(it) } // destroyEntity gives error cannot be recursive


        val newState = when (entityToDelete) {
            is Item -> _uiState.value.copy(items = _uiState.value.items.filter { it.id != entityToDelete.id })
            is Box -> _uiState.value.copy(boxes = _uiState.value.boxes.filter { it.id != entityToDelete.id })
            is Collection -> _uiState.value.copy(collections = _uiState.value.collections.filter { it.id != entityToDelete.id })
            else -> throw IllegalArgumentException("Invalid element type")
        }
        _uiState.value = newState
        notifyUpdateToParent(entityToDelete, true)

    }

    private fun notifyUpdateToParent(element: BaseCardData, deleteElement: Boolean = false) {
        when(element) {
            is Item -> {
                val box = uiState.value.boxes.find { box -> box.items.any { it.id == element.id } }
                val items = if(deleteElement) {
                    box?.items?.filter { it.id != element.id }
                } else {
                    box?.items?.map {
                        if (it.id == element.id) element else it
                    }
                }
                val updatedBox = box?.copy(items = items as List<Item>)
                updateEntity(updatedBox as Box)
                notifyUpdateToParent(updatedBox)
            }
            is Box -> {
                val collection = uiState.value.collections.find { collection -> collection.boxes.any { it.id == element.id } }
                val boxes = if(deleteElement) {
                    collection?.boxes?.filter { it.id != element.id }
                } else {
                    collection?.boxes?.map {
                        if (it.id == element.id) element else it
                    }
                }
                val updatedCollection = collection?.copy(boxes = boxes as List<Box>)
                updateEntity(updatedCollection as Collection)
            }
            else -> {} // Do nothing with Collection or Summary
        }
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
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PackItUpApplication)
                val localDataRepository = application.container.localDataRepository
                PackItUpViewModel(repository = localDataRepository)
            }
        }
    }
}

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
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Summary
import com.example.packitupandroid.repository.DataRepository
import com.example.packitupandroid.repository.LocalDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class State {
    object Create : State()
    object Update : State()
    object Destroy : State()
}

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
        val elements = createEntity(element, count)
        updateState(State.Create, elements)
    }

    fun updateElement(element: BaseCardData) {
        val elements = updateEntity(element)
        updateState(State.Update, elements)
    }

    fun destroyElement(element: BaseCardData) {
        val elements = destroyEntity(element)
        updateState(State.Destroy, elements)
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

    private fun <T : BaseCardData> getEntity(element: T): T {
        val entity = uiState.value.items
            .plus(uiState.value.boxes)
            .plus(uiState.value.collections)
            .firstOrNull { it.id == element.id } as? T
        return entity ?: throw NoSuchElementException("Entity not found")
    }

    private inline fun <reified T: BaseCardData> createEntity(element: T, count: Int?): MutableList<T> {
        if (count == null) {
            throw IllegalArgumentException("Cannot create entity with null count")
        }

        if (element is Summary) {
            throw IllegalArgumentException("Element cannot be Summary")
        }

        val newElements: MutableList<T> = mutableListOf()
         (0 until count).mapIndexed { index, _ ->
            val newElement = when(element) {
                is Item -> element.copy(name="Item ${index + 1}")
                is Box -> element.copy(name="Box ${index + 1}")
                is Collection -> element.copy(name="Collection ${index + 1}")
                else -> throw Exception("Could not create element")
            } as T
             newElements.add(newElement)
        }
        return newElements
    }

    private fun<T: BaseCardData> getParent(element: T): BaseCardData {
        val parentElement = when(element) {
            is Item -> uiState.value.boxes.find { box -> box.items.any { it.id == element.id } }
            is Box -> uiState.value.collections.find { collection -> collection.boxes.any { it.id == element.id } }
            else -> throw IllegalArgumentException("Could not get parent element")
        }

        return parentElement ?: throw IllegalArgumentException("Could not get parent element")
    }

    private fun <T : BaseCardData> updateEntity(element: T): MutableList<BaseCardData>  {
        val elementsToUpdate: MutableList<BaseCardData> = mutableListOf()
        when (element) {
            is Item -> {
                val elementToUpdate = getEntity(element)
                elementsToUpdate.add(element)
                if (elementToUpdate.value != element.value || elementToUpdate.isFragile != element.isFragile) {
                    val box = getParent(element) as Box
                    val collection = getParent(box) as Collection
                    val updatedBox = box.copy(
                        value = box.items.filterNot { it.id == element.id }
                            .sumOf { it.value } + element.value,
                        isFragile = box.items.filterNot { it.id == element.id }
                            .any { it.isFragile } || element.isFragile,
                    )
                    val updatedCollection = collection.copy(
                        value = collection.boxes.filterNot { it.id == updatedBox.id }
                            .sumOf { it.value } + updatedBox.value,
                        isFragile = collection.boxes.filterNot { it.id == updatedBox.id }
                            .any { it.isFragile } || updatedBox.isFragile,
                    )
                    elementsToUpdate.add(updatedBox)
                    elementsToUpdate.add(updatedCollection)
                }
            }
            is Box -> elementsToUpdate.add(element)
            is Collection -> elementsToUpdate.add(element)
            else -> throw IllegalArgumentException("Cannot copy element")
        }
        return elementsToUpdate
    }
    private fun <T : BaseCardData> destroyEntity(element: T): MutableList<T> {
        val elementsToDestroy: MutableList<BaseCardData> = mutableListOf()
        when (element) {
            is Item -> {
                val box = getParent(element) as Box
                val collection = getParent(box) as Collection
                val items = box.items.filterNot { it.id == element.id }
                val updatedBox = box.copy(
                    items = items,
                    value = items.sumOf { it.value },
                    isFragile = items.any { it.isFragile },
                )
                val boxes = collection.boxes.map { if (it.id == updatedBox.id) updatedBox else it }
                val updatedCollection = collection.copy(
                    boxes = boxes,
                    value = boxes.sumOf { it.value },
                    isFragile = boxes.any { it.isFragile },
                )
                // TODO: redo. don't like state update and returning list of what to destroy
                updateState(State.Update, updateEntity(updatedBox))
                updateState(State.Update, updateEntity(updatedCollection))
                elementsToDestroy.add(element)
            }

            is Box -> {
                val items = element.items
                val collection = getParent(element) as Collection
                val boxes = collection.boxes.filterNot { it.id == element.id }
                val updatedCollection = collection.copy(
                    boxes = boxes,
                    value = boxes.sumOf { it.value },
                    isFragile = boxes.any { it.isFragile }
                )
                updateState(State.Update, updateEntity(updatedCollection))
                elementsToDestroy.addAll(items)
                elementsToDestroy.add(element)
            }
            is Collection -> {
                val boxes = element.boxes
                val items = boxes.flatMap { it.items }
                elementsToDestroy.addAll(items)
                elementsToDestroy.addAll(boxes)
                elementsToDestroy.add(element)
            }
            else -> throw IllegalArgumentException("Cannot copy element")
        }

        return elementsToDestroy as MutableList<T>
    }

    private fun<T : BaseCardData> updateState(state: State, elements: List<T>) {
        val newState : PackItUpUiState = when(state) {
            is State.Create -> {
                when (elements.firstOrNull()) {
                     is Item ->  uiState.value.copy(items = uiState.value.items + elements as List<Item>)
                     is Box -> uiState.value.copy(boxes = uiState.value.boxes + elements as List<Box>)
                     is Collection -> uiState.value.copy(collections = uiState.value.collections + elements as List<Collection>)
                     else -> throw IllegalStateException("Unknown element type")
                 }
            }
            is State.Update -> {
                val items = elements.filter { it is Item }
                val boxes = elements.filter { it is Box }
                val collections = elements.filter { it is Collection }

                val updatedItems = if(items.isNotEmpty()) {
                    uiState.value.items.map { item ->
                        items.find { it.id == item.id } ?: item
                    }
                } else {
                    uiState.value.items
                }

                val updatedBoxes = if(boxes.isNotEmpty()) {
                    uiState.value.boxes.map { box ->
                        boxes.find { it.id == box.id } ?: box
                    }
                } else {
                    uiState.value.boxes
                }

                val updatedCollections = if(collections.isNotEmpty()) {
                    uiState.value.collections.map { collection ->
                        collections.find { it.id == collection.id } ?: collection
                    }
                } else {
                    uiState.value.collections
                }

                uiState.value.copy(
                    items = updatedItems as List<Item>,
                    boxes = updatedBoxes as List<Box>,
                    collections = updatedCollections as List<Collection>,
                )
            }
            is State.Destroy -> {
                val items = elements.filterIsInstance<Item>()
                val boxes = elements.filterIsInstance<Box>()
                val collections = elements.filterIsInstance<Collection>()
                uiState.value.copy(
                    items = uiState.value.items.filterNot { items.contains(it) },
                    boxes = uiState.value.boxes.filterNot { boxes.contains(it) },
                    collections = uiState.value.collections.filterNot { collections.contains(it) },
                )
            }
        }

        _uiState.value = newState
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

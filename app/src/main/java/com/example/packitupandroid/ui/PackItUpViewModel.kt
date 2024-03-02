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

class PackItUpViewModel(
    private val repository: DataRepository = LocalDataRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO: only load items if it is mock data
            // TODO: replace with DB if available
            if(_uiState.value.items.isEmpty()) {
                loadItems()
            }
            if(_uiState.value.boxes.isEmpty()) {
                loadBoxes()
            }
            if(_uiState.value.collections.isEmpty()) {
                loadCollections()
            }
        }
    }
    private fun <T : BaseCardData> getEntity(id: String, entities: List<T>): T? {
        return entities.firstOrNull { it.id == id }
    }

    private fun getItem(id: String): Item? {
        return getEntity(id, uiState.value.items)
    }

    private fun getBox(id: String): Box? {
        return getEntity(id, uiState.value.boxes)
    }

    private fun getCollection(id: String): Collection? {
        return getEntity(id, uiState.value.collections)
    }

    private suspend fun loadItems() {
        try {
            val items = repository.loadItems()
            _uiState.value = _uiState.value.copy(items = items)
        } catch (e: Exception) {
            // Handle error
        }
    }

    private suspend fun loadBoxes() {
        try {
            val boxes = repository.loadBoxes()
            _uiState.value = _uiState.value.copy(boxes = boxes)
        } catch (e: Exception) {
            // Handle error
        }
    }

    private suspend fun loadCollections() {
        try {
            val collections = repository.loadCollections()
            _uiState.value = _uiState.value.copy(collections = collections)
        } catch (e: Exception) {
            // Handle error
        }
    }

    fun createElement(element: BaseCardData, count: Int? = null) {
        when (element) {
            is Item -> createItem(count)
            is Box -> createBox(count)
            is Collection -> createCollection(count)
            is Summary -> throw Exception("Cannot create Summary")
        }
    }

    fun updateElement(element: BaseCardData) {
        when (element) {
            is Item -> updateItem(element)
            is Box -> updateBox(element)
            is Collection -> updateCollection(element)
            is Summary -> throw Exception("Cannot update Summary")
        }
    }

    fun destroyElement(element: BaseCardData) {
        when (element) {
            is Item -> destroyItem(element.id)
            is Box -> destroyBox(element.id)
            is Collection -> destroyCollection(element.id)
            is Summary -> throw Exception("Cannot destroy Summary")
        }
    }

    private fun createItem(count: Int? = 0) {
        val newItems: MutableList<Item> = mutableListOf()
        if(count != null) {
            for (i in 1 .. count) {
                val name = "New Item ${i}"
                val newItem = Item( name = name)
                newItems.add(newItem)
            }
            _uiState.value = _uiState.value.copy(items = uiState.value.items + newItems)
        }
    }

    private fun createBox(count: Int? = 0) {
        val newBoxes: MutableList<Box> = mutableListOf()
        if(count != null) {
            for (i in 1 .. count) {
                val name = "New Box ${i}"
                val newBox = Box(name = name)
                newBoxes.add(newBox)
            }
            _uiState.value = _uiState.value.copy(boxes = uiState.value.boxes + newBoxes)
        }
    }

    private fun createCollection(count: Int? = 0) {
        val newCollections: MutableList<Collection> = mutableListOf()
        if (count != null) {
            for (i in 1..count) {
                val name = "New Collection ${i}"
                val newCollection = Collection(name = name)
                newCollections.add(newCollection)
            }
            _uiState.value =
                _uiState.value.copy(collections = uiState.value.collections + newCollections)
        }
    }

    private fun destroyItem(id: String?) {
        if(id != null) {
            val itemToDelete = getItem(id)
            if(itemToDelete != null) {
                _uiState.value = _uiState.value.copy(items = uiState.value.items.filter { it.id != itemToDelete.id })
                notifyUpdateToParent(itemToDelete, true)
            }
        }
    }

    private fun destroyBox(id: String?) {
        if(id != null) {
            val boxToDelete = getBox(id)
            val items = boxToDelete?.items

            items?.forEach { destroyElement(it) }

            if(boxToDelete != null) {
                _uiState.value = _uiState.value.copy(boxes = uiState.value.boxes.filter { it.id != boxToDelete.id })
                notifyUpdateToParent(boxToDelete, true)
            }
        }
    }

    private fun destroyCollection(id: String?) {
        if(id != null) {
            val collectionToDelete = getCollection(id)
            val boxes = collectionToDelete?.boxes
            val items = collectionToDelete?.boxes?.flatMap { it.items }

            items?.forEach { destroyElement(it) }
            boxes?.forEach { destroyElement(it) }

            if(collectionToDelete != null) {
                _uiState.value = _uiState.value.copy(collections = uiState.value.collections.filter { it.id != collectionToDelete.id })
            }
        }
    }

    private fun updateItem(item: Item) {
        val itemToUpdate = getItem(item.id)
        if(itemToUpdate != null) {
            val updatedItem = item.copy()
            _uiState.value = _uiState.value.copy(items = uiState.value.items.map {
                if (it.id == item.id) updatedItem else it
            })

            if(itemToUpdate.value != item.value || itemToUpdate.isFragile != item.isFragile) {
                notifyUpdateToParent(updatedItem)
            }
        }
    }

    private fun notifyUpdateToParent(element: BaseCardData, deleteElement: Boolean = false) {
        if (element is Item) {
            val box = uiState.value.boxes.find { box -> box.items.any { it.id == element.id } }
            val items = if(deleteElement) {
                box?.items?.filter { it.id != element.id }
            } else {
                box?.items?.map {
                    if (it.id == element.id) element else it
                }
            }
            val updatedBox = box?.copy(items = items as List<Item>)
            updateBox(updatedBox as Box)
            notifyUpdateToParent(updatedBox as Box)
        } else {
            val collection = uiState.value.collections.find { collection -> collection.boxes.any { it.id == element.id } }
            val boxes = if(deleteElement) {
                collection?.boxes?.filter { it.id != element.id }
            } else {
                collection?.boxes?.map {
                    if (it.id == element.id) element else it
                }
            }

            val updatedCollection = collection?.copy(boxes = boxes as List<Box>)
            updateCollection(updatedCollection as Collection)
        }

    }

    private fun updateBox(box: Box){
        val boxToUpdate = getBox(box.id)
        if(boxToUpdate != null) {
            val updatedBox = box.copy()
            _uiState.value = _uiState.value.copy(boxes = uiState.value.boxes.map {
                if (it.id == box.id) updatedBox else it
            })

            if(boxToUpdate.value != box.value || boxToUpdate.isFragile != box.isFragile) {
                notifyUpdateToParent(updatedBox)
            }
        }
    }

    private fun updateCollection(collection: Collection){
        val collectionToUpdate = getCollection(collection.id)
        if(collectionToUpdate != null) {
            val updatedCollection = collection.copy()
            _uiState.value = _uiState.value.copy(collections = uiState.value.collections.map {
                if (it.id == collection.id) updatedCollection else it
            })
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

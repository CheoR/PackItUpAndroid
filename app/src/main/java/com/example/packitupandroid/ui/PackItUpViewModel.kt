package com.example.packitupandroid.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.packitupandroid.PackItUpApplication
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.repository.DataRepository
import com.example.packitupandroid.repository.LocalDataRepository
import com.example.packitupandroid.ui.components.card.BaseCardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class PackItUpViewModel(
    private val repository: DataRepository = LocalDataRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO: only load items if it is mock data
            // TODO: replace with DB if available
            loadItems()
            loadBoxes()
            loadCollections()
        }
    }

    private fun getItem(id: String): Item? {
        return uiState.value.items.firstOrNull { it.id == id }
    }

    private fun getBox(id: String): Box? {
        return uiState.value.boxes.firstOrNull { it.id == id }
    }

    private fun getCollection(id: String): Collection? {
        return uiState.value.collections.firstOrNull { it.id == id }
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


    fun createItem(count: Int? = 0) {
        val newItems: MutableList<Item> = mutableListOf()
        if(count != null) {
            for (i in 1 .. count) {
                val id = UUID.randomUUID().toString()
                val name = "New Item ${i}"
                val newItem = Item(
                    id = id,
                    name = name,
                    value = 1.25,
                )
                newItems.add(newItem)
            }
            _uiState.value = _uiState.value.copy(items = uiState.value.items + newItems)
        }
    }

    fun createBox(count: Int? = 0) {
        val newBoxes: MutableList<Box> = mutableListOf()
        if(count != null) {
            for (i in 1 .. count) {
                val name = "New Box ${i}"
                val newBox = Box(
                    name = name,
                )
                newBoxes.add(newBox)
            }
            _uiState.value = _uiState.value.copy(boxes = uiState.value.boxes + newBoxes)
        }
    }

    fun createCollection(count: Int? = 0) {
        val newCollections: MutableList<Collection> = mutableListOf()
        if (count != null) {
            for (i in 1..count) {
                val name = "New Collection ${i}"
                val newCollection = Collection(
                    name = name,
                )
                newCollections.add(newCollection)
            }
            _uiState.value =
                _uiState.value.copy(collections = uiState.value.collections + newCollections)
        }
    }

    fun deleteItem(id: String?) {
        if(id != null) {
            val itemToDelete = getItem(id)
            if(itemToDelete != null) {
                _uiState.value = _uiState.value.copy(items = uiState.value.items.filter { it.id != itemToDelete.id })
            }
        }
    }

    fun deleteBox(id: String?) {
        if(id != null) {
            val boxToDelete = getBox(id)
            if(boxToDelete != null) {
                _uiState.value = _uiState.value.copy(boxes = uiState.value.boxes.filter { it.id != boxToDelete.id })
            }
        }
    }

    fun deleteCollection(id: String?) {
        if(id != null) {
            val collectionToDelete = getCollection(id)
            if(collectionToDelete != null) {
                _uiState.value = _uiState.value.copy(collections = uiState.value.collections.filter { it.id != collectionToDelete.id })
            }
        }
    }

//
//    fun removeBox(box: Box) {
//        val currentBoxes = repository.boxes.value.toMutableList()
//        currentBoxes.remove(box)
//        repository.boxes.value = currentBoxes
//    }

     fun updateElement(element: BaseCardData) {
        when (element) {
            is BaseCardData.ItemData -> updateItem(element.item)
            is BaseCardData.BoxData -> updateBox(element.box)
            is BaseCardData.CollectionData -> updateCollection(element.collection)
            is BaseCardData.SummaryData -> {}
        }
    }
    fun updateItem(item: Item) {
        Log.i("MOOOOOOOOOOOO", "CALLING UPDATE ITEM")
        val itemToUpdate = getItem(item.id)
        if(itemToUpdate != null) {
            val updatedItem = itemToUpdate.copy(
                name = item.name,
                description = item.description,
                value = item.value,
                imageUri = item.imageUri,
                isFragile = item.isFragile,
            )
//            _uiState.value = _uiState.value.copy(items = uiState.value.items)
            _uiState.value = _uiState.value.copy(items = uiState.value.items.map {
                if (it.id == item.id) updatedItem else it
            })
            Log.i("MOOOOOOOOOOOO", "ITEMS ARE NOW: ${uiState.value.items.map { it.name }}")
        }
    }

//    fun updateBox(box: Box, newName: String, newDescription: String, newItems: List<Item>)  {
//        val currentBoxes = repository.boxes.value.toMutableList()
//        val index = currentBoxes.indexOfFirst { it.id == box.id }
//        if (index != -1) {
//            currentBoxes[index] = box.copy(
//                name = newName,
//                description = newDescription,
//                items = newItems,
//            )
//            repository.boxes.value = currentBoxes
//        }
//    }

    fun resetItemList() {
        viewModelScope.launch {
            Log.i("MOOOOO", "CALLING REST")
            Log.i("MOOO", "number of items: ${uiState.value.items.size}")
            _uiState.update { currentState ->
                currentState.copy(
                    items = emptyList(),
                )
            }
            Log.i("MOOO", "number of items: ${uiState.value.items.size}")
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

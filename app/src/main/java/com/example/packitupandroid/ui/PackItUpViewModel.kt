package com.example.packitupandroid.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.database.entities.toItem
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.data.model.toEntity
import com.example.packitupandroid.data.repository.DataRepository
import com.example.packitupandroid.data.repository.LocalDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class State {
    object Create : State()
    object Update : State()
    object Destroy : State()
}
class PackItUpViewModel(
    private val localDataRepository: DataRepository = LocalDataRepository(),
    private val itemsRepository: ItemsRepository,
) : ViewModel() {
    private val isUseMockData = true
    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()

//    private val _entityCache = MutableStateFlow<Map<String, BaseCardData>>(emptyMap())
//    private val entityCache: StateFlow<Map<String, BaseCardData>> = _entityCache.asStateFlow()
    private val entityCacheMap = mutableMapOf<String, BaseCardData>()
    private val entityCache: Map<String, BaseCardData> get() = entityCacheMap

    init {
        initializeUIState(isUseMockData)
        observeUIState()
    }

    private fun initializeUIState(useMockData: Boolean) {
        Log.i("MOO", "initializeUIState")
        if (useMockData) {
            // populate data from file for future tutorial
            viewModelScope.launch {
                val items: List<Item> = localDataRepository.loadItems()
                val entities = items.map { it.toEntity() }
                saveItem(items)
                itemsRepository.getAllItemsStream().map { items ->
                    _uiState.value.copy(
                        items = items.map{ it.toItem() }
                    )
                }
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                        initialValue = PackItUpUiState()
                    )
                    .collect { newState ->
                        _uiState.value = newState
                    }
            }

        } else {
            // user regular db
            viewModelScope.launch {
                if (_uiState.value.items.isEmpty()) {
                    loadStuff()
                    loadData(localDataRepository::loadItems) { items -> _uiState.value.copy(items = items) }
                }
//            if (_uiState.value.boxes.isEmpty()) {
//                loadData(localDataRepository::loadBoxes) { boxes -> _uiState.value.copy(boxes = boxes) }
//            }
//            if (_uiState.value.collections.isEmpty()) {
//                loadData(localDataRepository::loadCollections) { collections -> _uiState.value.copy(collections = collections) }
//            }
            }
        }
    }

    private fun loadStuff() {
        Log.i("MOO", "loadStuff laoding stuff")
        itemsRepository.getAllItemsStream().map { items ->
            _uiState.value.copy(
                items = items.map{ it.toItem() }
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PackItUpUiState()
            )
    }

    private fun observeUIState() {
        Log.i("MOO", "observeUIState")
        viewModelScope.launch {
            // Observe changes in uiState and update the cache
            //  suspending function that collects latest value of uiState whenever it changes
            uiState.collect { state ->
                val newCache = state.items.associateBy { it.id }
//                    .plus(state.boxes.associateBy { it.id })
//                    .plus(state.collections.associateBy { it.id })
//                _entityCache.emit(newCache)
                entityCacheMap.clear()
                entityCacheMap.putAll(newCache)
            }
        }
    }

    fun createElement(element: BaseCardData, count: Int?) {
        val elements: List<Item> = createEntity(element, count) as List<Item>
        Log.i("MOO", "creating ${elements.size} elements")
        updateState(State.Create, elements)
        viewModelScope.launch {
            saveItem(elements)
            itemsRepository.getAllItemsStream().map { items ->
                _uiState.value.copy(
                    items = items.map{ it.toItem() }
                )
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = PackItUpUiState()
                )
                .collect { newState ->
                    _uiState.value = newState
                }
        }

     }

    fun updateElement(element: BaseCardData) {
        val elements = updateEntity(element)
        updateState(State.Update, elements)
    }

    fun destroyElement(element: BaseCardData) {
        val (elementsToUpdate, elementsToDestroy) = destroyEntity(element)
        updateState(State.Update, elementsToUpdate)
        updateState(State.Destroy, elementsToDestroy)
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
        val entity = entityCache[element.id] as? T
        return entity ?: throw NoSuchElementException("Entity not found")
    }

    private fun <T: BaseCardData> createEntity(element: T, count: Int?): List<T> {
        requireNotNull(count) { "Cannot create entity with null count" }
        require(element !is Summary) { "Element cannot be Summary" }

        return (0 until count).mapIndexed { index, _ ->
            when(element) {
                is Item -> Item(name = "Item ${index + 1}")
                is Box -> Box(name = "Box ${index + 1}")
                is Collection -> Collection(name = "Collection ${index + 1}")
                else -> throw Exception("Could not create element")
            } as T
        }
    }

    private fun <T : BaseCardData> getParent(element: T): BaseCardData {
        val parentElement = when (element) {
            is Item -> getParentBox(element.id)
            is Box -> getParentCollection(element.id)
            else -> null
        }

        return parentElement ?: throw IllegalArgumentException("Could not get parent element")
    }

    private fun getParentBox(itemId: String): BaseCardData? {
        return uiState.value.boxes.find { box -> box.items.any { it.id == itemId } }
    }

    private fun getParentCollection(boxId: String): BaseCardData? {
        return uiState.value.collections.find { collection -> collection.boxes.any { it.id == boxId } }
    }

    private fun <T : BaseCardData> updateEntity(element: T): List<BaseCardData> {
        val elementsToUpdate: MutableList<BaseCardData> = mutableListOf()
        when (element) {
            is Item -> {
                val elementToUpdate = getEntity(element)
                elementsToUpdate.add(element)

                if (elementToUpdate.value != element.value || elementToUpdate.isFragile != element.isFragile) {
                    val box = getParent(element) as Box
                    val collection = getParent(box) as Collection
                    val (newBoxValue, newBoxIsFragile) = updateValueAndIsFragile(box.items, element)

                    val updatedBox = box.copy(
                        value = newBoxValue,
                        isFragile = newBoxIsFragile
                    )
                    val (newCollectionValue, newCollectionIsFragile) = updateValueAndIsFragile(collection.boxes, updatedBox)

                    val updatedCollection = collection.copy(
                        value = newCollectionValue,
                        isFragile = newCollectionIsFragile,
                    )
                    elementsToUpdate.add(updatedBox)
                    elementsToUpdate.add(updatedCollection)
                }
            }
            is Box -> elementsToUpdate.add(element)
            is Collection -> elementsToUpdate.add(element)
            else -> throw IllegalArgumentException("Cannot copy element")
        }
        return elementsToUpdate.toList()
    }

    private fun <T: BaseCardData>updateValueAndIsFragile(elements: List<T>, element: T): Pair<Double, Boolean> {
        val newValue = elements.filterNot { it.id == element.id }.sumOf { it.value } + element.value
        val newIsFragile = elements.filterNot { it.id == element.id }.any { it.isFragile } || element.isFragile
        return Pair(newValue, newIsFragile)
    }

//    private fun <T : BaseCardData> destroyEntity(element: T): Pair<List<BaseCardData>, List<BaseCardData>> {
private fun destroyEntity(element: BaseCardData): Pair<List<BaseCardData>, List<BaseCardData>> {
        val elementsToUpdate: MutableList<BaseCardData> = mutableListOf()
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
                elementsToUpdate.add(updatedBox)
                elementsToUpdate.add(updatedCollection)
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
                elementsToUpdate.add(updatedCollection)
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

        return Pair(elementsToUpdate.toList(), elementsToDestroy.toList())
    }

    private fun <T : BaseCardData> replaceElements(oldElements: List<T>, newElements: List<T>): List<T> {
        return oldElements.map { oldElement ->
            newElements.find { it.id == oldElement.id } ?: oldElement
        }
    }

    private fun <T : BaseCardData> updateState(state: State, elements: List<T>) {
        viewModelScope.launch {
            val items = elements.filterIsInstance<Item>() // elements.filter { it is Item }
            val boxes = elements.filterIsInstance<Box>() //  { it is Box }
            val collections = elements.filterIsInstance<Collection>()

            val newState: PackItUpUiState = when (state) {
                is State.Create -> {
                    when (elements.firstOrNull()) {
                        is Item -> uiState.value.copy(items = uiState.value.items + elements as List<Item>)
                        is Box -> uiState.value.copy(boxes = uiState.value.boxes + elements as List<Box>)
                        is Collection -> uiState.value.copy(collections = uiState.value.collections + elements as List<Collection>)
                        else -> throw IllegalStateException("Unknown element type")
                    }
                }

                is State.Update -> {
                    val updatedItems = replaceElements(uiState.value.items, items)
                    val updatedBoxes = replaceElements(uiState.value.boxes, boxes)
                    val updatedCollections = replaceElements(uiState.value.collections, collections)

                    uiState.value.copy(
                        items = updatedItems,
                        boxes = updatedBoxes,
                        collections = updatedCollections,
                    )
                }

                is State.Destroy -> {
                    uiState.value.copy(
                        items = uiState.value.items.filterNot { items.contains(it) },
                        boxes = uiState.value.boxes.filterNot { boxes.contains(it) },
                        collections = uiState.value.collections.filterNot { collections.contains(it) },
                    )
                }
            }

            _uiState.value = newState
        }
    }

    fun getAllItems() {
        itemsRepository.getAllItemsStream()
    }
    suspend fun saveItem(items: List<Item>) {
        val entities: List<ItemEntity> = items.map { item -> item.toEntity() }

        if(entities.size == 1) {
            itemsRepository.insertItem(entities[0])
        } else {
            itemsRepository.insertAll(entities)
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
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


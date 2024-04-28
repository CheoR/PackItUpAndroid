package com.example.packitupandroid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.data.database.entities.toBox
import com.example.packitupandroid.data.database.entities.toCollection
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.screens.summary.SummaryScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.example.packitupandroid.Result

//class ScreenViewModel(
//    private val itemViewModel: ItemsScreenViewModel,
//    private val boxViewModel: BoxesScreenViewModel,
//    private val collectionViewModel: CollectionsScreenViewModel,
//    private val summaryScreenViewModel: SummaryScreenViewModel,
//) : ViewModel() {
//    private val isUseMockData = true
//
////    private val _uiState = MutableStateFlow(PackItUpUiState())
////    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()
//    private val _uiState = MutableStateFlow(ScreenUiState(elements=emptyList()))
//    val uiState: StateFlow<ScreenUiState> = _uiState.asStateFlow()
//    init {
////        viewModelScope.launch {
//            initializeUIState(isUseMockData)
////        }
//    }
//
////    private suspend fun initializeUIState(isUseMockData: Boolean = false) {
//private fun initializeUIState(isUseMockData: Boolean = false) {
//        // TODO: fix - use combine and stateIn to combine
//        if (isUseMockData) {
//            combine(
//                itemViewModel.uiState,
//                boxViewModel.uiState,
//                collectionViewModel.uiState,
//            ) { itemUiState, boxUiState, collectionUiState ->
//                ScreenUiState(
//                    elements = emptyList(),
//                    items = itemUiState.elements,
//                    boxes = boxUiState.elements,
//                    collections = collectionUiState.elements,
//                )
//            }.onEach { combinedUiState ->
//                _uiState.value = combinedUiState
//            }.launchIn((viewModelScope))
//
//        } else {
//            // TODO: REPLACE TO USE DIFFERENT DB
//            combine(
//                itemViewModel.uiState,
//                boxViewModel.uiState,
//                collectionViewModel.uiState,
//            ) { itemUiState, boxUiState, collectionUiState ->
//                ScreenUiState(
//                    elements = emptyList(),
//                    items = itemUiState.elements,
//                    boxes = boxUiState.elements,
//                    collections = collectionUiState.elements,
//                )
//            }.onEach { combinedUiState ->
//                _uiState.value = combinedUiState
//            }.launchIn((viewModelScope))
//        }
//    }
//
//    private suspend fun initializeUIState(isUseMockData: Boolean = false) {
//        // TODO: fix - use combine and stateIn to combine
//        if (isUseMockData) {
//            viewModelScope.launch {
//                collectionViewModel.getAllCollections()
//                summaryScreenViewModel.uiState.collect { summaryUiState ->
//                    _uiState.value = summaryUiState
//                }
//            }
//        } else {
//            // TODO: REPLACE TO USE DIFFERENT DB
//            viewModelScope.launch {
//                try {
//                    summaryScreenViewModel.uiState.collect { summaryUiState ->
//                        _uiState.value = summaryUiState
//                    }
//                } catch (e: Exception) {
//                    _uiState.value = PackItUpUiState(
//                        result = Result.Error(e.message ?: "Unknown error")
//                    )
//                }
//            }
//        }
//    }
//
//    fun loadCurrentScreenData(selectedScreen: String) {
////        viewModelScope.launch {
//            when(selectedScreen) {
//                PackItUpRoute.ITEMS -> {
//                    _uiState.value = ScreenUiState(
//                        elements = uiState.value.items,
//                        items = uiState.value.items,
//                        boxes = uiState.value.boxes,
//                        collections = uiState.value.collections,
//                        result = Result.Complete(elements = uiState.value.items),
//                        currentScreen = PackItUpRoute.ITEMS,
//                    )
//                }
//                PackItUpRoute.BOXES -> {
//                    _uiState.value = ScreenUiState(
//                        elements = uiState.value.boxes,
//                        items = uiState.value.items,
//                        boxes = uiState.value.boxes,
//                        collections = uiState.value.collections,
//                        result = Result.Complete(elements = uiState.value.boxes),
//                        currentScreen = PackItUpRoute.BOXES,
//                    )
//                }
//                PackItUpRoute.COLLECTIONS -> {
//                    _uiState.value = ScreenUiState(
//                        elements = uiState.value.collections,
//                        items = uiState.value.items,
//                        boxes = uiState.value.boxes,
//                        collections = uiState.value.collections,
//                        result = Result.Complete(elements = uiState.value.collections),
//                        currentScreen = PackItUpRoute.COLLECTIONS,
//                    )
//                }
//                PackItUpRoute.SUMMARY -> {
//                    _uiState.value = ScreenUiState(
//                        elements = emptyList(),
//                        items = uiState.value.items,
//                        boxes = uiState.value.boxes,
//                        collections = uiState.value.collections,
//                        result = Result.Complete(elements = emptyList()),
//                        currentScreen = PackItUpRoute.SUMMARY,
//                    )
//                }
////            }
//        }
//    }
//
//    fun setCurrentScreen(selectedScreen: String) {
//        if (uiState.value.currentScreen != selectedScreen) {
//            _uiState.value = uiState.value.copy(
//                currentScreen = selectedScreen
//            )
//        }
//    }
//
////    private suspend fun getParent(element: BaseCardData): BaseCardData? {
// suspend fun getParent(element: BaseCardData): BaseCardData? {
//        return when(element) {
//            is Item -> {
//                val boxId = element.boxId
//                var box: Box? = null
//                if(boxId != null) {
//                    // TODO: FIX - review
//                    box = boxViewModel.getBox(boxId)!!.toBox()
//                }
//                box
//            }
//            is Box -> {
//                val collectionId = element.collectionId
//                var collection: Collection? = null
//                if(collectionId != null) {
//                    collection = collectionViewModel.getCollection(collectionId)!!.toCollection()
//                }
//                collection
//            }
//            else -> null // throw IllegalStateException("Unsupported type")
//        }
//    }
//
////    private fun getDropdownSelections(element: BaseCardData): List<BaseCardData> {
////        return when(element) {
////            is Item -> boxViewModel.getAllBoxes()
////            is Box -> collectionViewModel.getAllCollections()
////            else -> emptyList() // throw IllegalStateException("Unsupported type")
////        }
////    }
////
////    fun getDropdownOptions(element: BaseCardData): List<BaseCardData> {
////        var selections: List<BaseCardData> = emptyList()
////        val scope = CoroutineScope(Dispatchers.Default)
////        scope.launch {
////            selections = getDropdownSelections(element)
////        }
////        return selections
////    }
//
//    fun getDropdownOptions(element: BaseCardData): List<BaseCardData> {
//        return when(element) {
//            is Item -> boxViewModel.getAllBoxes()
//            is Box -> collectionViewModel.getAllCollections()
//            else -> emptyList() // throw IllegalStateException("Unsupported type")
//        }
//    }
//
//    fun getParentContainer(element: BaseCardData): BaseCardData? {
//        var parent: BaseCardData? = null
//        val scope = CoroutineScope(Dispatchers.Default)
//        scope.launch {
//            parent = getParent(element)
//        }
//        return parent
//    }
//
//    fun create(count: Int, element: BaseCardData) {
//        when(element) {
//            is Item -> itemViewModel.create(count)
//            is Box -> boxViewModel.create(count)
//            is Collection -> collectionViewModel.create(count)
//            else -> throw IllegalStateException("Unsupported type")
//        }
//    }
//
//    fun update(element: BaseCardData) {
//        when(element) {
//            is Item -> itemViewModel.update(element)
//            is Box -> boxViewModel.update(element)
//            is Collection -> collectionViewModel.update(element)
//            else -> throw IllegalStateException("Unsupported type")
//        }
//    }
//
//    fun destroy(element: BaseCardData) {
//        when(element) {
//            is Item -> itemViewModel.destroy(element)
//            is Box -> boxViewModel.destroy(element)
//            is Collection -> collectionViewModel.destroy(element)
//            else -> throw IllegalStateException("Unsupported type")
//        }
//    }
//}

package com.example.packitupandroid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.screens.summary.SummaryScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreenViewModel(
    private val itemViewModel: ItemsScreenViewModel,
    private val boxViewModel: BoxesScreenViewModel,
    private val collectionViewModel: CollectionsScreenViewModel,
    private val summaryScreenViewModel: SummaryScreenViewModel,
) : ViewModel() {
    private val isUseMockData = true

    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            initializeUIState(isUseMockData)
        }
    }

    private suspend fun initializeUIState(isUseMockData: Boolean = false) {
        if (isUseMockData) {
            viewModelScope.launch {
                summaryScreenViewModel.uiState.collect { summaryUiState ->
                    _uiState.value = summaryUiState
                }
            }
        } else {
            // TODO: REPLACE TO USE DIFFERENT DB
            viewModelScope.launch {
                try {
                    summaryScreenViewModel.uiState.collect { summaryUiState ->
                        _uiState.value = summaryUiState
                    }
                } catch (e: Exception) {
                    _uiState.value = PackItUpUiState(
                        result = Result.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    fun loadCurrentScreenData(selectedScreen: String) {
        viewModelScope.launch {
            when(selectedScreen) {
                PackItUpRoute.ITEMS -> {
                    itemViewModel.uiState.collect { itemUiState ->
                        _uiState.value = itemUiState
                    }
                }
                PackItUpRoute.BOXES -> {
                    boxViewModel.uiState.collect { boxUiState ->
                        _uiState.value = boxUiState
                    }
                }
                PackItUpRoute.COLLECTIONS -> {
                    collectionViewModel.uiState.collect { boxUiState ->
                        _uiState.value = boxUiState
                    }
                }
                PackItUpRoute.SUMMARY -> {
                    summaryScreenViewModel.uiState.collect { summaryUiState ->
                        _uiState.value = summaryUiState
                    }
                }
            }
        }
    }
    fun setCurrentScreen(selectedScreen: String) {
        if (uiState.value.currentScreen != selectedScreen) {
            _uiState.value = uiState.value.copy(
                currentScreen = selectedScreen
            )
        }
    }
}

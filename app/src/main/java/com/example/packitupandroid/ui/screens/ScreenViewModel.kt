package com.example.packitupandroid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreenViewModel(
    private val itemViewModel: ItemsScreenViewModel,
    private val boxViewModel: BoxesScreenViewModel,
    private val collectionViewModel: CollectionsScreenViewModel,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PackItUpUiState())
    val uiState: StateFlow<PackItUpUiState> = _uiState.asStateFlow()

    // load Summary data here in init

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

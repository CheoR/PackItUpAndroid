package com.example.packitupandroid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.utils.toBoolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreenViewModel(
    private val summaryRepository: SummaryRepository,
) : ViewModel() {
    private val _navGraphState = MutableStateFlow(NavGraphState())
    val navGraphState: StateFlow<NavGraphState> = _navGraphState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeNavGraphState()
        }
    }

    private suspend fun initializeNavGraphState() {
        summaryRepository.getAllSummaryStream().collect { summary ->
            _navGraphState.value = navGraphState.value.copy(
                canNavigateToBoxesScreen = summary.collection_count.toBoolean(),
                canNavigateToItemsScreen = summary.box_count.toBoolean(),
            )
        }
    }
}

data class NavGraphState(
    val canNavigateToBoxesScreen: Boolean = false,
    val canNavigateToItemsScreen: Boolean = false,
)

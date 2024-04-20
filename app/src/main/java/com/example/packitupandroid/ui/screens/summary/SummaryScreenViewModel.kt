package com.example.packitupandroid.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.toSummary
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SummaryScreenViewModel(
    private val summaryRepository: SummaryRepository,
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
                summaryRepository.getAllSummaryStream().collect { queryResult ->
                    _uiState.value = PackItUpUiState(
                        result = Result.Complete(
                            summary = queryResult.toSummary("")
                        ),
                        currentScreen = PackItUpRoute.SUMMARY,
                    )
                }
//                    .stateIn(
//                        scope = viewModelScope,
//                        started = SharingStarted.WhileSubscribed(CollectionsScreenViewModel.TIMEOUT_MILLIS),
//                        initialValue = PackItUpUiState()
//                    )
//                    .collect { newState -> _uiState.value = newState }
            }
        } else {
            viewModelScope.launch {
                try {
                    // TODO: REPLACE TO USE DIFFERENT DB
                    summaryRepository.getAllSummaryStream().collect { queryResult ->
                        _uiState.value = PackItUpUiState(
                            result = Result.Complete(
                                summary = queryResult.toSummary("")
                            ),
                            currentScreen = PackItUpRoute.SUMMARY,
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = PackItUpUiState(
                        result = Result.Error(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

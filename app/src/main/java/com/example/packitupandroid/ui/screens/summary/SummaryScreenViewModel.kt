package com.example.packitupandroid.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.toSummary
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.utils.USE_MOCK_DATA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SummaryScreenViewModel(
    private val summaryRepository: SummaryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SummaryScreenUiState())
    val uiState: StateFlow<SummaryScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeUIState()
        }
    }

    private suspend fun initializeUIState(isUseMockData: Boolean = USE_MOCK_DATA) {
        if (isUseMockData) {
            viewModelScope.launch {
                summaryRepository.getAllSummaryStream().map { queryResult ->
                    SummaryScreenUiState(
                        elements = emptyList(),
                        result = Result.Complete(
                            summary = queryResult.toSummary("")
                        ),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = SummaryScreenUiState()
                ).collect { newState -> _uiState.value = newState }
            }
        } else {
            viewModelScope.launch {
                try {
                    // TODO: REPLACE TO USE DIFFERENT DB
                    summaryRepository.clearAllSummary()
                    summaryRepository.getAllSummaryStream().collect { queryResult ->
                        _uiState.value = SummaryScreenUiState(
                            result = Result.Complete(
                                summary = queryResult.toSummary("")
                            ),
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = SummaryScreenUiState(
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

data class SummaryScreenUiState(
    override val elements: List<BaseCardData> = emptyList(),
    override val result: Result = Result.Loading,
): PackItUpUiState

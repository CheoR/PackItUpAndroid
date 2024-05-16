package com.example.packitupandroid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.utils.toBoolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ScreenViewModel(
    private val boxesRepository: BoxesRepository,
    private val collectionsRepository: CollectionsRepository,
    private val summaryRepository: SummaryRepository,
) : ViewModel() {
    private val _navGraphState = MutableStateFlow(NavGraphState())
    val navGraphState: StateFlow<NavGraphState> = _navGraphState.asStateFlow()
    private var boxesOptions: List<QueryDropdownOptions> = emptyList()
    private var collectionsOptions: List<QueryDropdownOptions> = emptyList()

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
                showBoxesScreenSnackBar = !summary.collection_count.toBoolean(),
                showItemsScreenSnackBar = !summary.box_count.toBoolean(),
            )
        }
    }

    fun toggleScreenSnackbar(route: String) {
        when(route) {
            PackItUpRoute.BOXES -> _navGraphState.value = navGraphState.value.copy(
                showBoxesScreenSnackBar = !navGraphState.value.showBoxesScreenSnackBar,
            )
            PackItUpRoute.ITEMS -> _navGraphState.value = navGraphState.value.copy(
                showItemsScreenSnackBar = !navGraphState.value.showItemsScreenSnackBar,
            )
        }
    }

    fun getCollectionDropdownOptions(): List<QueryDropdownOptions> {
        viewModelScope.launch {
            collectionsOptions = getCollectionsDropdownOptions()
        }
        return collectionsOptions
    }

    fun getBoxDropdownOptions(): List<QueryDropdownOptions> {
        viewModelScope.launch {
            boxesOptions = getBoxesDropdownOptions()
        }
        return boxesOptions
    }

    private suspend fun getBoxesDropdownOptions(): List<QueryDropdownOptions> {
        return boxesRepository.getDropdownSelections().first()
    }

    private suspend fun getCollectionsDropdownOptions(): List<QueryDropdownOptions> {
        return collectionsRepository.getDropdownSelections().first()
    }
}

data class NavGraphState(
    val canNavigateToBoxesScreen: Boolean = false,
    val canNavigateToItemsScreen: Boolean = false,
    val showBoxesScreenSnackBar: Boolean = true,
    val showItemsScreenSnackBar: Boolean = true,
)

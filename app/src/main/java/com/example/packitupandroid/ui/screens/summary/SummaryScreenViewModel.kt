package com.example.packitupandroid.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.USE_MOCK_DATA
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel for the Summary screen.
 *
 * This ViewModel is responsible for managing the data and state of the Items screen,
 * using the [SummaryRepository] to access item data.
 *
 * @param repository The repository used to access item data.
 * @param defaultDispatcher The coroutine dispatcher to use for background tasks (defaults to [Dispatchers.Default]).
 */
class SummaryScreenViewModel (
    private val repository: SummaryRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    // TODO: REMOVE, no need for a viewModel on a screen that has no user interaction and only displays data
    private val _elements = MutableStateFlow<Result<Summary?>>(Result.Loading)

    /**
     * A [StateFlow] that emits the current state of the summary of data.
     *
     * The state is wrapped in a [Result] object, which can be [Result.Loading], [Result.Success], or [Result.Error].
     */
    val elements: StateFlow<Result<Summary?>> = _elements.asStateFlow()

    init {
        initialize()
    }

    /**
     * Initializes the ViewModel, loading mock data if [USE_MOCK_DATA] is true and the database is empty.
     *
     * This function is called in the `init` block.
     */
    protected fun initialize() {
        viewModelScope.launch(defaultDispatcher) {
            load()
        }
    }

    /**
     * Loads all data from the repository and updates the [_elements] state.
     *
     * This function is called in the `init` block and after data modifications.
     */
    private fun load() {
        viewModelScope.launch(defaultDispatcher) {
            repository.observe().collect { result ->
                _elements.value = result
            }
        }
    }
}

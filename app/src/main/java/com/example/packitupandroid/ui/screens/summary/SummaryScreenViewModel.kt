package com.example.packitupandroid.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.utils.Result
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
     * Initializes the data observation process.
     *
     * This function starts a coroutine within the ViewModel's scope to observe changes
     * from the repository. When new data is emitted by the repository, it updates the
     * `_elements` LiveData/StateFlow/MutableStateFlow (depending on the actual implementation of _elements).
     *
     * The coroutine is launched on the `defaultDispatcher` to avoid blocking the main thread
     * during potentially long-running operations.
     *
     * The `repository.observe()` method is expected to return a Flow that emits data updates.
     * The `collect` method then listens to this Flow and triggers the update of `_elements`.
     *
     * Note: This function should typically be called once, preferably during the initialization
     * of the ViewModel.
     *
     * @param viewModelScope The coroutine scope associated with the ViewModel. This is used to launch
     *        the observation coroutine and will automatically be cancelled when the ViewModel is cleared.
     * @param repository The data repository that provides the data to be observed. It is expected to
     *        have an `observe()` method that returns a Flow.
     * @param defaultDispatcher The coroutine dispatcher to be used for observing data from the repository
     *        (e.g., Dispatchers.Default, Dispatchers.IO). This is passed in as a parameter
     *        to allow for easier testing and flexibility.
     * @param _elements The MutableStateFlow/LiveData/MutableLiveData  that will hold the data coming from the
     *        repository. It will be updated each time the repository emits a new result.
     */
    private fun initialize() {
        viewModelScope.launch(defaultDispatcher) {
            repository.observe().collect { result ->
                _elements.value = result
            }
        }
    }
}

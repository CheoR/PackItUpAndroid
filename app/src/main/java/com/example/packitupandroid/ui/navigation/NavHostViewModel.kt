package com.example.packitupandroid.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.repository.SummaryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.toBoolean


/**
 * Manages the state of the navigation host within the application.
 *
 * This ViewModel controls whether the app can navigate to different screens (Boxes, Items),
 * and whether specific snack bars should be shown on those screens. It relies on data from the
 * [SummaryRepository] to determine these states.
 *
 * @property summaryRepository The repository that provides summary data about collections and boxes.
 * @property defaultDispatcher The CoroutineDispatcher used for background operations.
 * @constructor Creates a NavHostViewModel with a specified [SummaryRepository].
 */
class NavHostViewModel(
    private val summaryRepository: SummaryRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val _navHoseState = MutableStateFlow(NavHostState())
    val navHoseState: StateFlow<NavHostState> = _navHoseState.asStateFlow()

    init {
        viewModelScope.launch {
            initialize()
        }
    }

    /**
     * Initializes the navigation host state based on the summary data received from the repository.
     *
     * This function observes the [summaryRepository] and updates the [_navHoseState] based on the
     * results. It handles different [Result] states: [Result.Error], [Result.Loading], and [Result.Success].
     *
     * When a [Result.Success] is received, it updates the [_navHoseState] with flags determining
     * whether navigation to the Boxes and Items screens is allowed and whether snack bars should be
     * shown. The logic for these flags is based on the presence of collections and boxes within the
     * received summary data.
     *
     * If [summaryRepository] returns error or loading state , the nav host state will remain unchanged
     *
     */
    private suspend fun initialize() {
        summaryRepository.observe().collect { result ->
            when(result) {
                is Result.Error -> NavHostState()
                is Result.Loading -> NavHostState()
                is Result.Success -> {
                    result.data?.let { summary ->
                        _navHoseState.value = _navHoseState.value.copy(
                            canNavigateToBoxesScreen = summary.collectionCount.toBoolean(),
                            canNavigateToItemsScreen = summary.boxCount.toBoolean(),
                            showBoxesScreenSnackBar = !summary.collectionCount.toBoolean(),
                            showItemsScreenSnackBar = !summary.boxCount.toBoolean(),
                        )
                    }
                }}
        }
    }

    /**
     * Toggles the visibility of a snackbar on a specific screen based on the provided PackItUpRoute.
     *
     * This function takes a [route] string, which represents a screen in the application (e.g., "BOXES" or "ITEMS").
     * Based on the route, it toggles the corresponding boolean flag in the `navHoseState` to show or hide a snackbar.
     *
     * @param route The route (screen identifier) for which to toggle the snackbar visibility.
     *              Must be one of the predefined routes like [PackItUpRoute.BOXES] or [PackItUpRoute.ITEMS].
     *
     * @throws IllegalArgumentException if an invalid route is provided.
     */
    fun toggleScreenSnackbar(route: String) {
        when(route) {
            Route.BOXES -> _navHoseState.value = navHoseState.value.copy(
                showBoxesScreenSnackBar = !navHoseState.value.showBoxesScreenSnackBar
            )
            Route.ITEMS -> _navHoseState.value = navHoseState.value.copy(
                showItemsScreenSnackBar = !navHoseState.value.showItemsScreenSnackBar
            )
        }
    }

    /**
     * Sets the title in the navigation state.
     *
     * This function updates the title in the `_navHoseState` based on the provided parameters.
     * It offers three modes of operation:
     *
     * 1. **Setting a specific title:** If a `title` string is provided, it will be used directly as the new title.
     * 2. **Adding a box title by ID:** If `addBox` is `true` and an `id` is provided, it will call `getCollectionName(id)` to retrieve and set the title for the added box.
     * 3. **Setting a box title by ID:** If neither `title` is set, nor `addBox` is `true` but an `id` is provided, then it will call `getBoxName(id)` to retrieve and set the box title.
     *
     * The `_navHoseState` is assumed to be a mutable state holder, which will be updated by copying the current state
     * and replacing the title.
     *
     * @param title The new title to be set. If null, other conditions are evaluated.
     * @param addBox A boolean flag indicating whether to retrieve a collection name. If true and `id` is provided, `getCollectionName(id)` will be called.
     * @param id The ID used to retrieve the box or collection name. Required if `addBox` is true or `title` is null and `addBox` is false.
     *
     * @see getCollectionName
     * @see getBoxName
     */
    fun setTitle (title: String? = null, addBox: Boolean = false, id: String? = null) {
        if(title != null) {
            _navHoseState.value = navHoseState.value.copy(
                title = title
            )
        } else if(addBox) {
            if (id != null) {
                getCollectionName(id)
            }
        } else {
            if (id != null) {
                getBoxName(id)
            }
        }
    }

    /**
     * Retrieves the name of a collection based on its ID and updates the navigation hose state.
     *
     * This function fetches the collection name associated with the provided [id] from the
     * [summaryRepository]. If the operation is successful and a name is retrieved, it updates
     * the `title` property of the `navHoseState`.
     *
     * The operation is performed asynchronously within the `viewModelScope` using the specified
     * [defaultDispatcher] (or `Dispatchers.Default` if not provided). This ensures that the network
     * request does not block the main thread.
     *
     * @param id The unique identifier of the collection.
     * @param defaultDispatcher The CoroutineDispatcher to use for the background operation.
     * Defaults to [Dispatchers.Default] if not provided.
     *
     * @see summaryRepository.getCollectionName
     * @see navHoseState
     * @see Result
     *
     * Example Usage:
     * ```kotlin
     *  getCollectionName("collection123")
     * ```
     */
    private fun getCollectionName(id: String) {
        viewModelScope.launch(defaultDispatcher) {
            val result = summaryRepository.getCollectionName(id)
            if(result is Result.Success) {
                result.data?.let {
                    _navHoseState.value = navHoseState.value.copy(
                        title = it
                    )
                }
            }
        }
    }

    /**
     * Retrieves the name of a box based on its ID and updates the navigation hose state.
     *
     * This function fetches the box name associated with the provided [id] from the
     * [summaryRepository]. If the operation is successful and a name is retrieved, it updates
     * the `title` property of the `navHoseState`.
     *
     * The operation is performed asynchronously within the `viewModelScope` using the specified
     * [defaultDispatcher] (or `Dispatchers.Default` if not provided). This ensures that the network
     * request does not block the main thread.
     *
     * @param id The unique identifier of the box.
     * @param defaultDispatcher The CoroutineDispatcher to use for the background operation.
     * Defaults to [Dispatchers.Default] if not provided.
     *
     * @see summaryRepository.getBoxName
     * @see navHoseState
     * @see Result
     *
     * Example Usage:
     * ```kotlin
     *  getBoxName(id)
     * ```
     */
    private fun getBoxName(id: String) {
        viewModelScope.launch(defaultDispatcher) {
            val result = summaryRepository.getBoxName(id)
            if(result is Result.Success) {
                result.data?.let {
                    _navHoseState.value = navHoseState.value.copy(
                        title = it
                    )
                }
            }
        }
    }

    /**
     * Retrieves the name of a item based on its ID and updates the navigation hose state.
     *
     * This function fetches the item name associated with the provided [id] from the
     * [summaryRepository]. If the operation is successful and a name is retrieved, it updates
     * the `title` property of the `navHoseState`.
     *
     * The operation is performed asynchronously within the `viewModelScope` using the specified
     * [defaultDispatcher] (or `Dispatchers.Default` if not provided). This ensures that the network
     * request does not block the main thread.
     *
     * @param id The unique identifier of the item.
     * @param defaultDispatcher The CoroutineDispatcher to use for the background operation.
     * Defaults to [Dispatchers.Default] if not provided.
     *
     * @see summaryRepository.getItemName
     * @see navHoseState
     * @see Result
     *
     * Example Usage:
     * ```kotlin
     *  getItemName(id)
     * ```
     */
    private fun getItemName(id: String) {
        viewModelScope.launch(defaultDispatcher) {
            val result = summaryRepository.getItemName(id)
            if(result is Result.Success) {
                result.data?.let {
                    _navHoseState.value = navHoseState.value.copy(
                        title = it
                    )
                }
            }
        }
    }
}


/**
 * Represents the state of the navigation host, encapsulating flags that determine navigation
 * capabilities and the visibility of snack bars for specific screens.
 *
 * @property title The title of the current screen.
 * @property canNavigateToBoxesScreen Indicates whether navigation to the "Boxes" screen is currently allowed.
 * @property canNavigateToItemsScreen Indicates whether navigation to the "Items" screen is currently allowed.
 * @property showBoxesScreenSnackBar Indicates whether a snack bar should be displayed on the "Boxes" screen.
 * @property showItemsScreenSnackBar Indicates whether a snack bar should be displayed on the "Items" screen.
 */
data class NavHostState(
    val canNavigateToBoxesScreen: Boolean = false,
    val canNavigateToItemsScreen: Boolean = false,
    val showBoxesScreenSnackBar: Boolean = true,
    val showItemsScreenSnackBar: Boolean = true,
    val title: String = "PackItUp"
)

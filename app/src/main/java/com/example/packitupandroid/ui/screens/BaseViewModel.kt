package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.repository.BaseRepository
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * Abstract base class for ViewModels that handle data operations for entities of type [T].
 *
 * This class provides common functionality for loading, inserting, updating, and deleting entities,
 * using a [BaseRepository] for data access. It also manages the state of the data using [StateFlow].
 *
 * @param T The type of entity this ViewModel handles, which must inherit from [BaseEntity].
 * @param repository The repository used to access data for the entity type [T].
 * @param defaultDispatcher The coroutine dispatcher to use for background tasks (defaults to [Dispatchers.Default]).
 */
abstract class BaseViewModel<D: BaseCardData>(
    private val repository: BaseRepository<D>,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _elements = MutableStateFlow<Result<List<D?>>>(Result.Loading)

    /**
     * A [StateFlow] that emits the current state of the list of entities.
     *
     * The state is wrapped in a [Result] object, which can be [Result.Loading], [Result.Success], or [Result.Error].
     */
    val elements: StateFlow<Result<List<D?>>> = _elements.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            initialize()
        }
    }

//fun loadData(){
//    viewModelScope.launch(defaultDispatcher) {
//        initialize()
//    }
//}


    /**
     * Initializes the data stream from the repository and updates the _elements LiveData.
     *
     * This function observes all data from the repository using `repository.observeAll()`.
     * It then processes the received `Result` and updates the `_elements` LiveData accordingly.
     *
     * The function handles three types of `Result`:
     * - `Result.Success`: Maps the received data (list) to the correct format.
     *    If the list is empty , it will not map it.
     * - `Result.Error`: Propagates the error to `_elements`.
     * - `Result.Loading`: Indicates that data is being loaded and updates `_elements` with a loading state.
     *
     * The operation is performed in the `defaultDispatcher` to avoid blocking the main thread.
     *
     * **Note:**
     * - `_elements` is a MutableLiveData that holds the current state of the data.
     * - `repository` is an instance of a repository class that provides access to the data.
     * - `defaultDispatcher` is a CoroutineDispatcher used for background tasks.
     * - `viewModelScope` is a coroutine scope tied to the lifecycle of the ViewModel.
     * - `mapNotNull` function is used for filtering null values of list after mapping.
     * - The function doesn't return a value, it updates `_elements` internally.
     * - The TODO comment indicates a future improvement to return null if data is empty instead of an empty list.
     *
     * @see Result
     * @see repository.observeAll()
     * @see _elements
     * @see viewModelScope
     * @see CoroutineDispatcher
     */
    private suspend fun initialize() {
        repository.observeAll().collect { result ->
            // TODO: update mapToDataClass to return null if empty
            // and do the when expresssion in the mapping
            _elements.value = when (result) {
                is Result.Success -> Result.Success(result.data.mapNotNull { it })
                is Result.Error -> Result.Error(result.exception)
                is Result.Loading -> Result.Loading
            }
        }
    }

    /**
     * Filters the elements in the underlying data list based on the provided callback function.
     *
     * This function retrieves the current list of elements stored in the `_elements` LiveData.
     * If the current value of `_elements` is a `Result.Success`, it extracts the data list from it.
     * Otherwise, it uses an empty list as the starting point.
     *
     * It then applies the provided `callback` function to this data list. The `callback` is responsible
     * for filtering, transforming, or otherwise modifying the list of `D?` elements.
     *
     * Finally, it updates the `_elements` LiveData with a new `Result.Success` object containing the
     * modified list returned by the `callback`.
     *
     * @param callback A lambda function that takes a `List<D?>` as input and returns a modified `List<D?>`.
     *                 This function is responsible for the actual filtering or transformation logic.
     *                 It receives the current data list (or an empty list if no data is available).
     *                 It should return the filtered/modified list.
     *
     * Example Usage:
     *
     * ```kotlin
     * filterElements { list ->
     *     list.filterNotNull().filter { it.someProperty > 5 } // Filters out nulls and keeps elements where someProperty > 5
     * }
     *
     * filterElements { list ->
     *   list.map { it?.copy(name = "Updated ${it.name}") } // Updates the name property of non-null elements
     * }
     * ```
     *
     * Note:
     * - If `_elements` is not `Result.Success`, the callback will be called with an empty list.
     * - This function operates on a copy of the data list. The original list is not modified directly.
     * - The type `D` can be any type. `D?` means that the list can contain nullable elements.
     * - The function ensures the `_elements` is always in `Result.Success` state after execution.
     */
    protected fun filterElements(callback: (List<D?>) -> List<D?>) {
        val currentData = when(_elements.value) {
            is Result.Success -> (_elements.value as Result.Success<List<D?>>).data
            else -> emptyList()
        }
        val result = Result.Success(callback(currentData))
        _elements.value = result
    }

    /**
     * Generates a composable function representing a column of icons.
     *
     * This function is an abstract method that should be implemented by concrete classes.
     * It is responsible for defining the content of a vertical column, typically containing
     * a series of icons. The implementation will determine the specific icons and their
     * arrangement within the column based on the provided data element.
     *
     * @param element The data element (of type D) that will be used to determine the icons
     *                to be displayed and their properties. This element provides the necessary
     *                information to customize the icons in the column.
     * @return A composable lambda function ([@Composable] ColumnScope.() -> Unit) that defines
     *         the UI content of the column. The lambda is executed within a ColumnScope, allowing
     *         the use of Column composable functions like `Arrangement` and `Alignment` modifiers.
     *         The returned composable function will generate the column layout containing icons.
     *
     * @see androidx.compose.foundation.layout.Column
     * @see androidx.compose.foundation.layout.ColumnScope
     * @see androidx.compose.runtime.Composable
     *
     * Example usage (hypothetical concrete implementation):
     * ```kotlin
     * data class MyData(val hasStar: Boolean, val hasHeart: Boolean)
     *
     * class MyIconGenerator : IconGenerator<MyData> {
     *     override fun generateIconsColumn(element: MyData): @Composable ColumnScope.() -> Unit = {
     *         if (element.itemCount) {
     *             Icon(imageVector = Icons.Filled.Star, contentDescription = "${element.itemCount} Item")
     *         }
     *         if (element.boxCount) {
     *             Icon(imageVector = Icons.Filled.Star, contentDescription = "${element.itemCount} Item")
     *
     *             Icon(imageVector = Icons.Filled.Favorite, contentDescription = "${element.boxCount} Boxes")
     *         }
     *     }
     * }
     * ```
     */
    abstract fun generateIconsColumn(element: D) : @Composable ColumnScope.() -> Unit

    /**
     * Abstract function to create a specified number of entities.
     *
     * @param count The number of entities to create.
     */
    abstract fun create(count: Int)

    /**
     * Abstract function to handle changes in a specific field of a card.
     *
     * This function is intended to be implemented by concrete classes that manage the
     * editing of cards. It's responsible for updating the state of a card based on
     * changes to one of its editable fields.
     *
     * @param card A mutable state object holding the current card data (of type D).
     *             This state will be updated when a field's value changes. It can be null.
     * @param field The specific field within the card that has been modified.
     *              This is represented by the [EditFields] enum (or a similar
     *              mechanism).
     * @param value The new string value that has been entered for the specified field.
     *              This value should be validated and potentially transformed
     *              before being applied to the card state.
     *
     * @param D The data type representing the Card, can be any object that hold the editfields.
     *
     * Example of potential implementation logic:
     * ```kotlin
     *  override fun onFieldChange(card: MutableState<MyCard?>, field: EditFields, value: String) {
     *     when (field) {
     *         EditFields.NAME -> {
     *             // Validate the name (e.g., length constraints)
     *             if (value.length <= 50) {
     *               card.value = card.value?.copy(name = value)
     *             } else {
     *                 // handle error, notify the user, etc.
     *             }
     *         }
     *         EditFields.DESCRIPTION -> {
     *             card.value = card.value?.copy(description = value)
     *         }
     *         // ... handle other fields
     *        else -> {
     *          //handle error, notify the user, etc.
     *        }
     *     }
     * }
     * ```
     */
    abstract fun onFieldChange(element: MutableState<D?>, field: EditFields, value: String)

    /**
     * Retrieves a specific entity by its ID and returns it as a [StateFlow].
     *
     * @param id The ID of the entity to retrieve.
     * @return A [StateFlow] that emits the current state of the entity, wrapped in a [Result] object.
     */
    fun get(id: String): StateFlow<Result<D?>> {
        val elementFlow = MutableStateFlow<Result<D?>>(Result.Loading)
        viewModelScope.launch(defaultDispatcher) {
            repository.observe(id).collect { result ->
                elementFlow.value = when (result) {
                    is Result.Success -> Result.Success(result.data)
                    is Result.Error -> Result.Error(result.exception)
                    is Result.Loading -> Result.Loading
                }
            }
        }
        return elementFlow.asStateFlow()
    }

    /**
     * Inserts a new entity into the repository.
     *
     * @param data The entity to insert.
     */
    fun insert(data: List<D>) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                repository.insert(data)
            } catch (e: Exception) {
                _elements.value = Result.Error(e)
            }
        }
    }

    /**
     * Updates an existing entity in the repository.
     *
     * @param element The entity to update.
     */
    fun update(data: D) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                repository.update(data)
            } catch (e: Exception) {
                _elements.value = Result.Error(e)
            }
        }
    }

    /**
     * Deletes an entity from the repository.
     *
     * @param element The entity to delete.
     */
    fun delete(data: List<String>) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                repository.delete(data)
            } catch (e: Exception) {
                _elements.value = Result.Error(e)
            }
        }
    }
}

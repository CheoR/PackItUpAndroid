package com.example.packitupandroid.ui.screens

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.repository.BaseRepository
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.USE_MOCK_DATA
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.firstOrNull


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
        initialize()
    }

    /**
     * Initializes the ViewModel, loading mock data if [USE_MOCK_DATA] is true and the database is empty.
     *
     * This function is called in the `init` block.
     */
    protected fun initialize() {
        viewModelScope.launch(defaultDispatcher) {
            if (USE_MOCK_DATA) {
                val currentItemsResult = repository.observeAll().firstOrNull()
                if (currentItemsResult is Result.Success && currentItemsResult.data.isEmpty()) {
                    repository.clear()
                    try {
                        repository.insert(getMockData())
                    } catch (e: Exception) {
                        _elements.value = Result.Error(e)
                    }
                }
                load()
            }
            load()
        }
    }

    /**
     * Abstract function to create a specified number of entities.
     *
     * @param count The number of entities to create.
     */
    abstract fun create(count: Int)

    /**
     * Abstract function to provide mock data for the entity type [T].
     *
     * @return A list of mock entities.
     */
    abstract fun getMockData(): List<D>

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
     * Loads all entities from the repository and updates the [_elements] state.
     *
     * This function is called in the `init` block and after data modifications.
     */
    private fun load() {
        viewModelScope.launch(defaultDispatcher) {
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
    }

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
                load() // Reload data after insertion
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
                load() // Reload data after update
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
                load() // Reload data after deletion
            } catch (e: Exception) {
                _elements.value = Result.Error(e)
            }
        }
    }

    /**
     * A companion object helps us by having a single instance of an object that is used by everyone
     * without needing to create a new instance of an expensive object. This is an implementation
     * detail, and separating it lets us make changes without impacting other parts of the app's code.
     *
     * The APPLICATION_KEY is part of the ViewModelProvider.AndroidViewModelFactory.Companion object
     * and is used to find the app's MarsPhotosApplication object, which has the container property
     * used to retrieve the repository used for dependency injection.
     */
    companion object {
        @JvmStatic
        protected val TIMEOUT_MILLIS = 5_000L
    }
}

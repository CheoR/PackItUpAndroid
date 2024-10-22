package com.example.packitupandroid.ui.screens

import androidx.lifecycle.ViewModel
import com.example.packitupandroid.utils.USE_MOCK_DATA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<UiState, Data, Entity> : ViewModel() {
    protected val _uiState by lazy { MutableStateFlow(initialState()) }
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    abstract fun initialState(): UiState
    abstract fun create(count: Int = 0)
    abstract fun update(element: Data)
    abstract fun destroy(element: Data)
    abstract fun getAllElements(): List<Data>
    abstract fun getElementById(id: String): Data?

    protected abstract suspend fun getEntity(id: String): Entity?
    protected abstract suspend fun initializeUIState(useMockData: Boolean = USE_MOCK_DATA)
    protected abstract suspend fun createEntity(entities: List<Entity>)
    protected abstract suspend fun updateEntity(entity: Entity)
    protected abstract suspend fun destroyEntity(entity: Entity)
    protected abstract suspend fun getElements(): List<Data>

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

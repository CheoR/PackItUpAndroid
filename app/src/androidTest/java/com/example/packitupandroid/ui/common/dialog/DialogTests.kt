package com.example.packitupandroid.ui.common.dialog

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.common.layout.Screen
import com.example.packitupandroid.ui.common.layout.generateIconsColumn
import com.example.packitupandroid.ui.common.layout.onFieldChange
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DialogTests {
        @get:Rule
        val composeTestRule = createComposeRule()

        @OptIn(ExperimentalCoroutinesApi::class)
        @get:Rule
        val coroutineRule = MainCoroutineRule()

        private lateinit var viewModel: ItemsScreenViewModel
        private lateinit var snackbarHostState: SnackbarHostState
        private lateinit var coroutineScope: CoroutineScope
        lateinit var baseCardContentDescription: String
        lateinit var swipeToOpenMenuContentDescription: String
        lateinit var deleteButtonContentDescription: String
        lateinit var cameraButtonContentDescription: String
        lateinit var editButtonContentDescription: String
        lateinit var editCardContentDescription: String
        lateinit var confirmContentDescription: String
        lateinit var cancelContentDescription: String

        lateinit var dialogTitle: String

        val testDataSource = TestDataSource()
        val items = testDataSource.items
        val placeholder = "items"
        val title = "Peggy PUg"

        private val _elements = MutableStateFlow<Result<List<Item?>>>(
            Result.Loading)

        val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun initializeViewModel() {
            viewModel = ItemsScreenViewModel(
                savedStateHandle = SavedStateHandle(),
                repository = MockItemsRepository2(),
                defaultDispatcher = coroutineRule.testDispatcher,
            )
        }

        private fun initializeSnackbar() {
            snackbarHostState = SnackbarHostState()
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun initializeCoroutineScope() {
            coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
        }

        private fun unregisterComposeEspressoIdlingResource() {
            IdlingRegistry.getInstance().resources
                .filter { it.name == "Compose-Espresso link" }
                .forEach { IdlingRegistry.getInstance().unregister(it) }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Before
        fun setup() {
            unregisterComposeEspressoIdlingResource()

            initializeSnackbar()
            initializeCoroutineScope()
            initializeViewModel()
        }

        @Test
        fun screen_clickConfirm_confirmClicked() {
            val testLazyListState = LazyListState()
            var confirmClicked = false

            composeTestRule.setContent {
                val context = LocalContext.current
                val themeManager = rememberThemeManager(context)
                val _elements = MutableStateFlow<Result<List<Item?>>>(
                    Result.Success(items.take(1)))
                val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
                val result by elements.collectAsState()

                baseCardContentDescription = stringResource(R.string.base_card)
                swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
                deleteButtonContentDescription = stringResource(R.string.delete)
                editButtonContentDescription = stringResource(R.string.edit)
                editCardContentDescription = stringResource(R.string.edit_dialog_title, title)
                confirmContentDescription = stringResource(R.string.button_confirm)
                cancelContentDescription = stringResource(R.string.button_cancel)
                dialogTitle = stringResource(R.string.delete_dialog_title, title)

                PackItUpAndroidTheme(themeManager) {
                    Screen<Item>(
                        result = result,
                        key = { it?.id ?: "" },
                        generateIconsColumn = generateIconsColumn,
                        onDelete = { id ->
                            val items = (result as Result.Success).data
                            val result = items.filterNot { it?.id == id.first() }
                            _elements.value = Result.Success(result)

                            confirmClicked = true
                        },
                        onCreate = {},
                        onFieldChange = onFieldChange,
                        onUpdate = {},
                        emptyListPlaceholder = placeholder,
                        snackbarHostState = snackbarHostState,
                        coroutineScope = coroutineScope,
                        lazyListState = testLazyListState,
                    )
                }
            }

            composeTestRule.waitUntil(timeoutMillis = 3500) {
                composeTestRule
                    .onAllNodesWithContentDescription(baseCardContentDescription)
                    .fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule
                .onNodeWithText(title)
                .assertExists()

            composeTestRule
                .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
                .performTouchInput {
                    swipeLeft()
                }

            val deleteButton = composeTestRule
                .onNodeWithContentDescription(deleteButtonContentDescription)

            deleteButton
                .assertExists()

            deleteButton
                .performClick()

            composeTestRule.waitUntil(timeoutMillis = 3500) {
                val count = composeTestRule
                    .onAllNodesWithText(dialogTitle)
                    .fetchSemanticsNodes()
                    .size

                1 == count
            }

            composeTestRule
                .onNodeWithContentDescription(confirmContentDescription)
                .performClick()

            composeTestRule
                .waitUntil(timeoutMillis = 45000) {
                    composeTestRule
                        .onAllNodesWithText(title)
                        .fetchSemanticsNodes()
                        .isEmpty()
                }

            assert(confirmClicked)
        }

    @Test
    fun screen_clickCancel_cancelClicked() {
        val testLazyListState = LazyListState()
        var cancelClicked = false

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(
                Result.Success(items.take(1)))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()

            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            editCardContentDescription = stringResource(R.string.edit_dialog_title, title)
            confirmContentDescription = stringResource(R.string.button_confirm)
            cancelContentDescription = stringResource(R.string.button_cancel)
            dialogTitle = stringResource(R.string.delete_dialog_title, title)

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { id ->
                        val items = (result as Result.Success).data
                        val result = items.filterNot { it?.id == id.first() }
                        _elements.value = Result.Success(result)

                        cancelClicked = true
                    },
                    onCreate = {},
                    onFieldChange = onFieldChange,
                    onUpdate = {},
                    emptyListPlaceholder = placeholder,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    lazyListState = testLazyListState,
                )
            }
        }

        composeTestRule.waitUntil(timeoutMillis = 3500) {
            composeTestRule
                .onAllNodesWithContentDescription(baseCardContentDescription)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText(title)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .performTouchInput {
                swipeLeft()
            }

        val deleteButton = composeTestRule
            .onNodeWithContentDescription(deleteButtonContentDescription)

        deleteButton
            .assertExists()

        deleteButton
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 3500) {
            val count = composeTestRule
                .onAllNodesWithText(dialogTitle)
                .fetchSemanticsNodes()
                .size

            1 == count
        }

        composeTestRule
            .onNodeWithContentDescription(confirmContentDescription)
            .performClick()

        composeTestRule
            .waitUntil(timeoutMillis = 45000) {
                composeTestRule
                    .onAllNodesWithText(title)
                    .fetchSemanticsNodes()
                    .isEmpty()
            }

        assert(cancelClicked)
    }
}

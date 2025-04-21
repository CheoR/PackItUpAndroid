package com.example.packitupandroid.ui.common.layout

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.printToString
import com.example.packitupandroid.source.local.LocalDataSource
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.incrementCountByFive
import com.example.packitupandroid.utils.incrementCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle


class ScreenUiItemTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ItemsScreenViewModel
    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var coroutineScope: CoroutineScope

    val testDataSource = TestDataSource()
    val items = testDataSource.items

    private val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Loading)

    val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = ItemsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockItemsRepository2(),
            defaultDispatcher = coroutineRule.testDispatcher,
        )
//        viewModel.create(initialValue)
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

    private fun create(count: Int): List<Item> {
        val data = List(count) { index -> Item(name = "Item ${index + 1}", boxId = null) }
        return data
    }

//    private fun setupComposeSetContent() {
//        composeTestRule.setContent {
//            val context = LocalContext.current
//            val themeManager = rememberThemeManager(context)
//            val result by viewModel.elements.collectAsState()

//            val result: Result<List<Item?>> = Result.Success(listOf(
//                Item(
//                    id = "1",
//                    name = "moocow",
//                    description = "oink",
//                    value = 123.45,
//                    isFragile = true,
//                    boxId = "2",
//                )
//            ))
//var value = elements.value
//            PackItUpAndroidTheme(themeManager) {
//                Screen<Item>(
//                    result = value, // Result.Success(elements.asList()),
//                    key = { it?.id ?: "" },
//                    generateIconsColumn = generateIconsColumn,
//                    onDelete = {},
//                    onCreate = {
//                        println("\n\n\tonCreate called, crating $it items\n\n")
////                        viewModel.create(it)
//                        _elements.value = Result.Success(emptyList())
//                    }, // viewModel::create,
//                    onFieldChange = onFieldChange,
//                    onUpdate = {},
//                    emptyListPlaceholder = "items",
//                    snackbarHostState = snackbarHostState,
//                    coroutineScope = coroutineScope,
//                )
//            }
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
//        coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
        unregisterComposeEspressoIdlingResource()
//        snackbarHostState = SnackbarHostState()

        initializeSnackbar()
        initializeCoroutineScope()
        initializeViewModel()
//        setupComposeSetContent()

//        assertNotNull(viewModel)
//        assertNotNull(coroutineScope)
    }

    @Test
    fun screen_init_noItemsFound() {
        composeTestRule
            .onNodeWithText("no items found")
            .assertExists()
    }

    @Test
    fun screen_createFiveItems_fiveItemsFound() {


        TODO: UPDATE THIS TO ALL TESTS
        BASIC IDEA WORKS, JUST NEEDS TO DO TESTS NOW
        IT CREATES A CARD INT TIMES
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Success(emptyList()))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = {},
                    onCreate = {
                        println("\n\n\tonCreate called, crating $it items\n\n")
                        _elements.value = Result.Success(create(it))
                    },
                    onFieldChange = onFieldChange,
                    onUpdate = {},
                    emptyListPlaceholder = "items",
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                )
            }
        }

        composeTestRule
            .incrementCounter(1)

        composeTestRule
            .onNodeWithContentDescription("add")
            .performClick()

        composeTestRule
            .waitUntil(timeoutMillis = 25000) {
                composeTestRule
                    .onAllNodesWithText("Peggy PUg")
                    .fetchSemanticsNodes().size == 1
            }
    }
}

class ScreenUiBoxTests {
}

class ScreenUiCollectionTests {
}

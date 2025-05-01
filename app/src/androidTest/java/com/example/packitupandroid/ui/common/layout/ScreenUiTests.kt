package com.example.packitupandroid.ui.common.layout

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeLeft
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.source.local.TestDataSource
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
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.incrementCountByFive
import com.example.packitupandroid.utils.incrementCounter
import com.example.packitupandroid.utils.parseCurrencyToDouble
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ScreenUiItemTests {
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
    lateinit var spinner_content_description: String
    lateinit var nameFieldContentDescription: String
    lateinit var cameraPreviewContentDescription: String
    lateinit var defaultIconContentDescription: String

    lateinit var dialogTitle: String

    val testDataSource = TestDataSource()
    val items = testDataSource.items
    val placeholder = "items"
    val title = items.first().name
    val COUNT = 5

    private val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Loading)

    val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
//    private val _elements = MutableStateFlow<Result<List<Item?>>>(
//        Result.Loading)
//
//    val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()

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
    private fun onFieldChange (element: MutableState<Item?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        element.value?.let { currentItem ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentItem.copy(description = value) else currentItem
                EditFields.Dropdown -> if(editableFields.contains(field))  currentItem.copy(boxId = value) else currentItem
                EditFields.ImageUri -> if(editableFields.contains(field))  currentItem.copy(imageUri = value) else currentItem
                EditFields.IsFragile -> if(editableFields.contains(field))  currentItem.copy(isFragile = value.toBoolean()) else currentItem
                EditFields.Name -> if(editableFields.contains(field))  currentItem.copy(name = value) else currentItem
                EditFields.Value -> if(editableFields.contains(field))  currentItem.copy(value = value.parseCurrencyToDouble()) else currentItem
            }

            println("\n\n\t\tUPDATED ELEMENTS: $updatedElement")
            Log.d("UPDATED ELEMENTS", "$updatedElement")
            element.value = updatedElement
        }
    }

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            editCardContentDescription = stringResource(R.string.edit_dialog_title, title)
            confirmContentDescription = stringResource(R.string.button_confirm)
            cancelContentDescription = stringResource(R.string.button_cancel)
            dialogTitle = stringResource(R.string.delete_dialog_title, title)
            spinner_content_description = stringResource(R.string.spinner_content_description)

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            val _elements = MutableStateFlow<Result<List<Item?>>>(
                Result.Success(items.take(1)))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
//            val result by viewModel.elements.collectAsState()

//
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
            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result, // value, // Result.Success(elements.asList()),
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { ids ->
                        val items = (result as Result.Success).data
                        val result = items.filterNot { it?.id == ids.first() }
                        _elements.value = Result.Success(result)
                    },
                    onCreate = {
                        println("\n\n\tonCreate called, crating $it items\n\n")
//                        viewModel.create(it)
                        val newItems = create(it)

                        _elements.value = Result.Success((result as Result.Success).data + newItems)
                    }, // viewModel::create,
                    onFieldChange = onFieldChange,
                    onUpdate = {},
                    emptyListPlaceholder = "items",
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                )
            }
        }
    }

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
    fun screen_initLoading_loadingAnimationDisplayed() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            spinner_content_description = stringResource(R.string.spinner_content_description)

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = Result.Loading,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = {},
                    onCreate = {},
                    onFieldChange = onFieldChange,
                    onUpdate = {},
                    emptyListPlaceholder = placeholder,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                )
            }
        }
        composeTestRule
            .onNodeWithContentDescription(spinner_content_description)
            .assertExists()
    }

    @Test
    fun screen_init_noItemsFound() {
    fun screen_initSuccessEmptyList_noItemsFound() {
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
                    onCreate = {},
                    onFieldChange = onFieldChange,
                    onUpdate = {},
                    emptyListPlaceholder = placeholder,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                )
            }
        }
        composeTestRule
            .onNodeWithText("no items found")
            .assertExists()
    }

    @Test
    fun screen_initSuccessNonEmptyList_correctNumberOfItemsFound() {
        val testLazyListState = LazyListState()

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Success(items))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = {},
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

        composeTestRule
            .waitUntil(timeoutMillis = 25000) {
//                val nodes = composeTestRule
//                    .onAllNodesWithContentDescription(baseCardContentDescription, substring = true)
                // TODO: get all nodes, not jsut visible in viewport, try without using scrollTo

                val totalItemCount = testLazyListState.layoutInfo.totalItemsCount

                totalItemCount == items.size
            }
    }

    @Test
    fun screen_createFiveItems_fiveItemsFound() {
        val testLazyListState = LazyListState()


        TODO: UPDATE THIS TO ALL TESTS
        BASIC IDEA WORKS, JUST NEEDS TO DO TESTS NOW
        IT CREATES A CARD INT TIMES
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Success(emptyList()))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)

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
                    emptyListPlaceholder = placeholder,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    lazyListState = testLazyListState,
                )
            }
        }

        composeTestRule
            .incrementCounter(1)
            .incrementCounter(COUNT)

        composeTestRule
            .onNodeWithContentDescription("add")
            .performClick()

        composeTestRule
            .waitUntil(timeoutMillis = 25000) {
            .waitUntil(timeoutMillis = 35000) {
                COUNT == testLazyListState.layoutInfo.totalItemsCount
            }

        assertEquals(COUNT, testLazyListState.layoutInfo.totalItemsCount)
    }

    @Test
    fun screen_swipeToDisplayOptions_optionsDisplayed() {
        val testLazyListState = LazyListState()
        val numberToTake = 1

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Success(items.take(numberToTake)))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            cameraButtonContentDescription = stringResource(R.string.camera)
            editButtonContentDescription = stringResource(R.string.edit)

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { id ->
                        val items = (result as Result.Success).data
                        val result = items.filterNot { it?.id == id.first() }
                        _elements.value = Result.Success(result)
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
            .onNodeWithText(items.first().name)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .performTouchInput {
                swipeLeft()
            }

        composeTestRule
            .onNodeWithContentDescription(deleteButtonContentDescription)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(cameraButtonContentDescription)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)
            .assertExists()
    }

    @Test
    fun screen_deleteItem_oneItemsDeleted() {
        val testLazyListState = LazyListState()
        val numberToTake = 1

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Success(items.take(numberToTake)))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            dialogTitle = stringResource(R.string.delete_dialog_title, title)
            confirmContentDescription = stringResource(R.string.button_confirm)
            editCardContentDescription = stringResource(R.string.edit_dialog_title, title)

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { id ->
                        val items = (result as Result.Success).data
                        val result = items.filterNot { it?.id == id.first() }
                        _elements.value = Result.Success(result)
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
            .onNodeWithText(items.first().name)
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

        composeTestRule
            .onNodeWithText(items.first().name)
            .assertDoesNotExist()
    }

    @Test
    fun screen_editItem_itemUpdated() {
        val testLazyListState = LazyListState()
        val numberToTake = 1
        val newName = "Moo Moo Oink Oink"
        val nameBefore = items.first().name

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Success(items.take(numberToTake)))
            val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
            val result by elements.collectAsStateWithLifecycle()

            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            dialogTitle = stringResource(R.string.edit_dialog_title, title)
            confirmContentDescription = stringResource(R.string.button_confirm)
            nameFieldContentDescription = stringResource(R.string.name) + " field"
            editButtonContentDescription = stringResource(R.string.edit)
            editCardContentDescription = stringResource(R.string.edit_dialog_title, title)

            // TODO: test every field? or just one since it uses the same
            // update method?
            // TODO: have a separate test for updating icon/camera

            PackItUpAndroidTheme(themeManager) {
                Screen<Item>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = {},
                    onCreate = {},
                    onFieldChange = onFieldChange,
                    onUpdate = { updatedItem ->
                        val currentItems = (_elements.value as Result.Success).data.toMutableList()
                        val index = currentItems.indexOfFirst { it?.id == updatedItem.id }
                        if (index != -1) {
                            currentItems[index] = updatedItem
                            _elements.value = Result.Success(currentItems.toList())
                        }
                    },
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
            .onNodeWithText(items.first().name)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .performTouchInput {
                swipeLeft()
            }

        val editButton = composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)

        editButton
            .assertExists()

        editButton
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 4500) {
            val count = composeTestRule
                .onAllNodesWithText(dialogTitle)
                .fetchSemanticsNodes()
                .size

            1 == count
        }

        composeTestRule
            .onNode(
                hasContentDescription(nameFieldContentDescription)
                    .and(isEditable())
            )
            .assertExists()
            .performTextReplacement(newName)

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

        composeTestRule
            .onNodeWithText(newName)
            .assertExists()

        composeTestRule
            .onNodeWithText(nameBefore)
            .assertDoesNotExist()
    }

    @Test
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

package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.repository.MockCollectionsRepository2
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.incrementCounter
import com.example.packitupandroid.utils.parseCurrencyToDouble
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ScreenUiCollectionTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CollectionsScreenViewModel
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
    lateinit var addButtonContentDescription: String

    lateinit var dialogTitle: String

    val testDataSource = TestDataSource()
    val items = testDataSource.items
    val boxes = testDataSource.boxes
    val collections = testDataSource.collections
    val placeholder = "collections"
    val title = collections.first().name
    val COUNT = 5

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = CollectionsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockCollectionsRepository2(),
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

    private fun create(count: Int): List<Collection> {
        val data = List(count) { index -> Collection(name = "Collection ${index + 1}") }
        return data
    }

    private val onFieldChange = fun (element: MutableState<Collection?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> currentBox //  if(editableFields.contains(field))  currentBox.copy(dropdown = value) else currentBox
                EditFields.ImageUri -> currentBox // if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }

    private val generateIconsColumn = fun(element: Collection): @Composable (ColumnScope.() -> Unit) {
        return {
            Column {
                IconBadge(
                    image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                    badgeContentDescription = "${element.itemCount} Items",
                    badgeCount = element.itemCount,
                    type = "items",
                )
            }
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

            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(collections.take(1))
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()

            PackItUpAndroidTheme(themeManager) {
                Screen<Collection>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { ids ->
                        val boxes = (result as Result.Success).data
                        val result = boxes.filterNot { it?.id == ids.first() }
                        _elements.value = Result.Success(result)
                    },
                    onCreate = {
                        println("\n\n\tonCreate called, crating $it boxes\n\n")
                        val newCollection = create(it)

                        _elements.value =
                            Result.Success((result as Result.Success).data + newCollection)
                    },
                    onFieldChange = onFieldChange,
                    onUpdate = {},
                    emptyListPlaceholder = "boxes",
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        unregisterComposeEspressoIdlingResource()

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
                Screen<Collection>(
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
    fun screen_initSuccessEmptyList_noCollectionFound() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(emptyList())
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()

            PackItUpAndroidTheme(themeManager) {
                Screen<Collection>(
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
            .onNodeWithText("no collections found")
            .assertExists()
    }

    @Test
    fun screen_initSuccessNonEmptyList_correctNumberOfCollectionFound() {
        val testLazyListState = LazyListState()

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(collections)
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)

            PackItUpAndroidTheme(themeManager) {
                Screen<Collection>(
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

                val totalCollectionCount = testLazyListState.layoutInfo.totalItemsCount

                totalCollectionCount == collections.size
            }
    }

    @Test
    fun screen_createFiveCollection_fiveCollectionFound() {
        val testLazyListState = LazyListState()

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(emptyList())
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)

            PackItUpAndroidTheme(themeManager) {
                Screen<Collection>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = {},
                    onCreate = {
                        _elements.value = Result.Success(create(it))
                    },
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
            .incrementCounter(COUNT)

        composeTestRule
            .onNodeWithContentDescription("add")
            .performClick()

        composeTestRule
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
            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(collections.take(numberToTake))
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            addButtonContentDescription = stringResource(R.string.add)
            editButtonContentDescription = stringResource(R.string.edit)

            PackItUpAndroidTheme(themeManager) {
                Screen<Collection>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { id ->
                        val collections = (result as Result.Success).data
                        val result = collections.filterNot { it?.id == id.first() }
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
            .onNodeWithText(collections.first().name)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .performTouchInput {
                swipeLeft()
            }

        composeTestRule
            .onNodeWithContentDescription(deleteButtonContentDescription)
            .assertExists()

        composeTestRule.onNode(
            hasContentDescription("add")
                .and(!hasText("add"))
        ).assertExists()

        composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)
            .assertExists()
    }

    @Test
    fun screen_deleteCollection_oneCollectionDeleted() {
        val testLazyListState = LazyListState()
        val numberToTake = 1

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(collections.take(numberToTake))
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
            val result by elements.collectAsState()
            baseCardContentDescription = stringResource(R.string.base_card)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            deleteButtonContentDescription = stringResource(R.string.delete)
            dialogTitle = stringResource(R.string.delete_dialog_title, title)
            confirmContentDescription = stringResource(R.string.button_confirm)
            editCardContentDescription = stringResource(R.string.edit_dialog_title, title)

            PackItUpAndroidTheme(themeManager) {
                Screen<Collection>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = { id ->
                        val collections = (result as Result.Success).data
                        val result = collections.filterNot { it?.id == id.first() }
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
            .onNodeWithText(collections.first().name)
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
            .onNodeWithText(collections.first().name)
            .assertDoesNotExist()
    }

    @Test
    fun screen_editCollection_CollectionUpdated() {
        val testLazyListState = LazyListState()
        val numberToTake = 1
        val newName = "Moo Moo Oink Oink"
        val nameBefore = collections.first().name

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            val _elements = MutableStateFlow<Result<List<Collection?>>>(
                Result.Success(collections.take(numberToTake))
            )
            val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()
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
                Screen<Collection>(
                    result = result,
                    key = { it?.id ?: "" },
                    generateIconsColumn = generateIconsColumn,
                    onDelete = {},
                    onCreate = {},
                    onFieldChange = onFieldChange,
                    onUpdate = { updatedCollection ->
                        val currentCollection =
                            (_elements.value as Result.Success).data.toMutableList()
                        val index =
                            currentCollection.indexOfFirst { it?.id == updatedCollection.id }
                        if (index != -1) {
                            currentCollection[index] = updatedCollection
                            _elements.value = Result.Success(currentCollection.toList())
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
            .onNodeWithText(collections.first().name)
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
}

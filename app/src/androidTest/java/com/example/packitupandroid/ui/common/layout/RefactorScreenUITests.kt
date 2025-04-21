package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.parseCurrencyToDouble
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import androidx.compose.runtime.getValue

typealias ParameterizedCollection = Collection<Array<Any>>

//typealias RefactorParam//typealias RefactorParameterizedCollection = Collection<Array<Any>>
////
////sealed class RefGroup {
////    object Loading : RefGroup()
////    object Success : RefGroup()
////    object Error : RefGroup()
////}
////
////private val refactorOnFieldChangenFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {
////    val editableFields = element.value?.editFields ?: emptyList()
////    element.value?.let { currentBox ->
////        val updatedElement = when(field) {
////            EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
////            EditFields.Dropdown -> if(editableFields.contains(field))  currentBox.copy(boxId = value) else currentBox
////            EditFields.ImageUri -> if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
////            EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
////            EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
////            EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
////        }
////        element.value = updatedElement
////    }
////}
////
////private val refactorGenerateIconsColumn = fun (element: Item): @Composable (ColumnScope.() -> Unit) {
////    val image = if (element.imageUri?.startsWith("/") == true) {
////        ImageContent.FileImage(element.imageUri)
////    } else {
////        ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
////    }
////
////    return {
////        Column {
////            IconBadge(
////                image = image,
////                badgeContentDescription = if(element.imageUri != null) stringResource(R.string.image_description, element.name) else stringResource(R.string.default_item_badge),
////                badgeCount = 0,
////                type = "items",
////            )
////        }
////    }
////}
////
////@OptIn(ExperimentalCoroutinesApi::class)
////@RunWith(Parameterized::class)
////class ScreenUITests(
////    private val group: RefGroup,
////    private val result: Result<List<Item?>>,
////) {
////
////    @get:Rule
////    val composeTestRule = createComposeRule() // createAndroidComposeRule<ComponentActivity>()
////
////    @OptIn(ExperimentalCoroutinesApi::class)
////    @get:Rule
////    val coroutineRule = MainCoroutineRule()
////
////    private lateinit var viewModel: ItemsScreenViewModel
////    private lateinit var snackbarHostState: SnackbarHostState
////    private lateinit var coroutineScope: CoroutineScope
////
////    val testData = TestDataSource()
////    val items = testData.items
////
////    @OptIn(ExperimentalCoroutinesApi::class)
////    private fun initializeViewModel() {
////        viewModel = ItemsScreenViewModel(
////            savedStateHandle = SavedStateHandle(),
////            repository = MockItemsRepository2(),
////            defaultDispatcher = coroutineRule.testDispatcher,
////        )
////        viewModel.create(4)
////    }
////
////    private fun initializeSnackbar() {
////        snackbarHostState = SnackbarHostState()
////    }
////
////    @OptIn(ExperimentalCoroutinesApi::class)
////    private fun initializeCoroutineScope() {
////        coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
////    }
////
////    private fun unregisterComposeEspressoIdlingResource() {
////        IdlingRegistry.getInstance().resources
////            .filter { it.name == "Compose-Espresso link" }
////            .forEach { IdlingRegistry.getInstance().unregister(it) }
////    }
////
////    private fun setupComposeSetContent() {
////        composeTestRule.setContent {
////            val context = LocalContext.current
////            val themeManager = rememberThemeManager(context)
////
////            val result by viewModel.elements.collectAsState()
////
////            PackItUpAndroidTheme(themeManager) {
////                Screen<Item>(
////                    result = result,
////                    key = { it?.id ?: "" },
////                    generateIconsColumn = refactorGenerateIconsColumn,
////                    onDelete = {},
////                    onCreate = {},
////                    onFieldChange = refactorOnFieldChangenFieldChange,
////                    onUpdate = {},
////                    emptyListPlaceholder = "items",
////                    snackbarHostState = snackbarHostState,
////                    coroutineScope = coroutineScope,
////                )
////            }
////        }
////    }
////
////    @OptIn(ExperimentalCoroutinesApi::class)
////    @Before
////    fun setup() {
//////        coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
////        unregisterComposeEspressoIdlingResource()
//////        snackbarHostState = SnackbarHostState()
////
////        initializeSnackbar()
////        initializeCoroutineScope()
////        initializeViewModel()
////        setupComposeSetContent()
////
//////        assertNotNull(viewModel)
//////        assertNotNull(coroutineScope)
////    }
////
////
//////    @After
//////    fun tearDown() {
//////        IdlingRegistry.getInstance().unregister(composeTestRule)
//////    }
////
////
////
////    companion object {
////        @JvmStatic
////        @Parameters(name = "{index}: group={0}, result={1}")
////        fun data(): RefactorParameterizedCollection {
////            val testData = TestDataSource()
////            val items = testData.items
////
////            return listOf(
////                arrayOf(RefGroup.Loading, Result.Loading),
////                arrayOf(RefGroup.Success, result, // Result.Success(result)),
////                arrayOf(RefGroup.Error, Result.Error(Exception("Test Error")))
////            )
////        }
////    }
////
////    @Test
////    fun screen_init_loadingSpinnerExists() {
////        if(group == RefGroup.Loading) {
////            composeTestRule
////                .onNodeWithContentDescription("loading content")
////                .assertExists()
////        }
////    }
////
////    @Test
////    fun screen_init_loadingSpinnerIsDisplayed() {
////        if (group == RefGroup.Loading) {
////            composeTestRule
////                .onNodeWithContentDescription("loading content")
////                .assertIsDisplayed()
////        }
////    }
////
////    @Test
////    fun screen_init_successEmptyListTextExists() {
////        if (group == RefGroup.Success) {
////            composeTestRule
////                .onNodeWithContentDescription("list is empty")
////                .assertExists()
////        }
////    }
////
////    @Test
////    fun screen_init_successEmptyListTextIsDisplayed() {
////        if (group == RefGroup.Success) {
////            composeTestRule
////                .onNodeWithContentDescription("list is empty")
////                .assertIsDisplayed()
////        }
////    }
////
////    @Test
////    fun testNumberOfCardsVisible_whenSuccessGroup() {
////        if (group == RefGroup.Success && result is Result.Success) {
////            val items = result.data
////
////            // Assert that the number of visible cards matches the number of items
////            items.let {
////                composeTestRule
////                    .onAllNodesWithContentDescription("base card", substring = true, ignoreCase = true) // Replace with the actual content description for your cards
////                    .assertCountEquals(it.size)
////            }
////        }
////    }eterizedCollection = Collection<Array<Any>>
//
//sealed class RefGroup {
//    object Loading : RefGroup()
//    object Success : RefGroup()
//    object Error : RefGroup()
//}
//
//private val refactorOnFieldChangenFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {
//    val editableFields = element.value?.editFields ?: emptyList()
//    element.value?.let { currentBox ->
//        val updatedElement = when(field) {
//            EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
//            EditFields.Dropdown -> if(editableFields.contains(field))  currentBox.copy(boxId = value) else currentBox
//            EditFields.ImageUri -> if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
//            EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
//            EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
//            EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
//        }
//        element.value = updatedElement
//    }
//}
//
//private val refactorGenerateIconsColumn = fun (element: Item): @Composable (ColumnScope.() -> Unit) {
//    val image = if (element.imageUri?.startsWith("/") == true) {
//        ImageContent.FileImage(element.imageUri)
//    } else {
//        ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
//    }
//
//    return {
//        Column {
//            IconBadge(
//                image = image,
//                badgeContentDescription = if(element.imageUri != null) stringResource(R.string.image_description, element.name) else stringResource(R.string.default_item_badge),
//                badgeCount = 0,
//                type = "items",
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(Parameterized::class)
//class ScreenUITests(
//    private val group: RefGroup,
//    private val result: Result<List<Item?>>,
//) {
//
//    @get:Rule
//    val composeTestRule = createComposeRule() // createAndroidComposeRule<ComponentActivity>()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val coroutineRule = MainCoroutineRule()
//
//    private lateinit var viewModel: ItemsScreenViewModel
//    private lateinit var snackbarHostState: SnackbarHostState
//    private lateinit var coroutineScope: CoroutineScope
//
//    val testData = TestDataSource()
//    val items = testData.items
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun initializeViewModel() {
//        viewModel = ItemsScreenViewModel(
//            savedStateHandle = SavedStateHandle(),
//            repository = MockItemsRepository2(),
//            defaultDispatcher = coroutineRule.testDispatcher,
//        )
//        viewModel.create(4)
//    }
//
//    private fun initializeSnackbar() {
//        snackbarHostState = SnackbarHostState()
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun initializeCoroutineScope() {
//        coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
//    }
//
//    private fun unregisterComposeEspressoIdlingResource() {
//        IdlingRegistry.getInstance().resources
//            .filter { it.name == "Compose-Espresso link" }
//            .forEach { IdlingRegistry.getInstance().unregister(it) }
//    }
//
//    private fun setupComposeSetContent() {
//        composeTestRule.setContent {
//            val context = LocalContext.current
//            val themeManager = rememberThemeManager(context)
//
//            val result by viewModel.elements.collectAsState()
//
//            PackItUpAndroidTheme(themeManager) {
//                Screen<Item>(
//                    result = result,
//                    key = { it?.id ?: "" },
//                    generateIconsColumn = refactorGenerateIconsColumn,
//                    onDelete = {},
//                    onCreate = {},
//                    onFieldChange = refactorOnFieldChangenFieldChange,
//                    onUpdate = {},
//                    emptyListPlaceholder = "items",
//                    snackbarHostState = snackbarHostState,
//                    coroutineScope = coroutineScope,
//                )
//            }
//        }
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Before
//    fun setup() {
////        coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
//        unregisterComposeEspressoIdlingResource()
////        snackbarHostState = SnackbarHostState()
//
//        initializeSnackbar()
//        initializeCoroutineScope()
//        initializeViewModel()
//        setupComposeSetContent()
//
////        assertNotNull(viewModel)
////        assertNotNull(coroutineScope)
//    }
//
//
////    @After
////    fun tearDown() {
////        IdlingRegistry.getInstance().unregister(composeTestRule)
////    }
//
//
//
//    companion object {
//        @JvmStatic
//        @Parameters(name = "{index}: group={0}, result={1}")
//        fun data(): RefactorParameterizedCollection {
//            val testData = TestDataSource()
//            val items = testData.items
//
//            return listOf(
//                arrayOf(RefGroup.Loading, Result.Loading),
//                arrayOf(RefGroup.Success, result, // Result.Success(result)),
//                arrayOf(RefGroup.Error, Result.Error(Exception("Test Error")))
//            )
//        }
//    }
//
//    @Test
//    fun screen_init_loadingSpinnerExists() {
//        if(group == RefGroup.Loading) {
//            composeTestRule
//                .onNodeWithContentDescription("loading content")
//                .assertExists()
//        }
//    }
//
//    @Test
//    fun screen_init_loadingSpinnerIsDisplayed() {
//        if (group == RefGroup.Loading) {
//            composeTestRule
//                .onNodeWithContentDescription("loading content")
//                .assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun screen_init_successEmptyListTextExists() {
//        if (group == RefGroup.Success) {
//            composeTestRule
//                .onNodeWithContentDescription("list is empty")
//                .assertExists()
//        }
//    }
//
//    @Test
//    fun screen_init_successEmptyListTextIsDisplayed() {
//        if (group == RefGroup.Success) {
//            composeTestRule
//                .onNodeWithContentDescription("list is empty")
//                .assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun testNumberOfCardsVisible_whenSuccessGroup() {
//        if (group == RefGroup.Success && result is Result.Success) {
//            val items = result.data
//
//            // Assert that the number of visible cards matches the number of items
//            items.let {
//                composeTestRule
//                    .onAllNodesWithContentDescription("base card", substring = true, ignoreCase = true) // Replace with the actual content description for your cards
//                    .assertCountEquals(it.size)
//            }
//        }
//    }

// LOADING
// EMPTY
// SUCCES
// ERROR // TODO

// SEARCH FUNCITONALITY

// STATE MANAGEMENT

// ERROR HANDLING // TODO

// CARD INTERACTIONS

// SNACKBAR MESSAGES

// BOTTOM SHEET BEHAVIOR

//  MODAL BOTTOM SHEET BEHAVIOR

// ACCESSABLITY

// RESPONSIVENESS // TODO
//}

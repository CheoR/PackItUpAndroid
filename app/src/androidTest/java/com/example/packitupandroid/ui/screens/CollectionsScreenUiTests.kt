package com.example.packitupandroid.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.repository.MockCollectionsRepository2
import com.example.packitupandroid.ui.screens.collection.CollectionsScreen
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.initialValue
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


//class CollectionsScreenUiTests {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val coroutineRule = MainCoroutineRule()
//
//    private lateinit var viewModel: CollectionsScreenViewModel
//    private lateinit var snackbarHostState: SnackbarHostState
//    private lateinit var coroutineScope: CoroutineScope
//
//    private fun unregisterComposeEspressoIdlingResource() {
//        IdlingRegistry.getInstance().resources
//            .filter { it.name == "Compose-Espresso link" }
//            .forEach { IdlingRegistry.getInstance().unregister(it) }
//    }
//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun initializeViewModel() {
//        viewModel = CollectionsScreenViewModel(
//            savedStateHandle = SavedStateHandle(),
//            repository = MockCollectionsRepository2(),
//            defaultDispatcher = coroutineRule.testDispatcher,
//        )
//        viewModel.create(initialValue)
//    }
//
//    private fun setupComposeSetContent() {
//        composeTestRule.setContent {
//            val context = LocalContext.current
//            val themeManager = rememberThemeManager(context)
//            PackItUpAndroidTheme(themeManager) {
//                CollectionsScreen(
//                    viewModel = viewModel,
//                    coroutineScope = coroutineScope,
//                    snackbarHostState = snackbarHostState,
//                    addElements = { _ -> Unit }, // TODO: create test for this
//                )
//            }
//        }
//    }
//
//    @Before
//    fun setup() {
//        unregisterComposeEspressoIdlingResource()
//        initializeViewModel()
//        setupComposeSetContent()
//    }
//
//
//    @Test
//    fun collectionsScreen_initialValues() {
//        TestCase.assertEquals(1, 1)
//
////        composeTestRule
////            .onNodeWithContentDescription("Counter Value")
////            .assertTextEquals(initialValue.toString())
////        assertChildrenCount("BaseCard", initialValue)
//    }
//}
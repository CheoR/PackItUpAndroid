package com.example.packitupandroid.ui.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeUp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.repository.MockSummaryRepository2
import com.example.packitupandroid.ui.screens.summary.SummaryScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.ContentType
import com.example.packitupandroid.utils.NavigationType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MyNavHostTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    lateinit var navController: TestNavHostController
    private lateinit var viewModel: NavHostViewModel
    private lateinit var summaryViewModel: SummaryScreenViewModel

    private fun unregisterComposeEspressoIdlingResource() {
        IdlingRegistry.getInstance().resources
            .filter { it.name == "Compose-Espresso link" }
            .forEach { IdlingRegistry.getInstance().unregister(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = NavHostViewModel(
            summaryRepository = MockSummaryRepository2(),
            defaultDispatcher = testDispatcher,
        )

        summaryViewModel = SummaryScreenViewModel(
            repository = MockSummaryRepository2(),
            defaultDispatcher = testDispatcher,
        )
    }

    @Before
    fun setup() {
        unregisterComposeEspressoIdlingResource()
        initializeViewModel()

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            PackItUpAndroidTheme(themeManager) {
                AppNavHost(
                    navController = navController,
                    selectedDestination = Route.SUMMARY,
                    contentType = ContentType.LIST_ONLY,
                    navigationType = NavigationType.BOTTOM_NAVIGATION,
                    navigateToTopLevelDestination = { destination ->
                        navController.navigate(destination.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    viewModel = viewModel,
                )
            }

//            SummaryScreen(
//                viewModel = summaryViewModel,
//                navigateToTopLevelDestination = { destination ->
//                    navController.navigate(destination.route) {
//                        // Pop up to the start destination of the graph to
//                        // avoid building up a large stack of destinations
//                        // on the back stack as users select items
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reselecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                    }
//                }
//            )
        }
    }

    @Test
    fun navgraph_init_startDestinationIsSummary() {
        assertEquals(navController.currentDestination?.route, Route.SUMMARY)
    }

    @Test
    fun navgraph_swipeUp_bottomRowNavigationDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Drag handle")
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Summary")
                .and(isSelected())
        )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Collections")
                .and(isNotSelected())
        )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Boxes")
                .and(isNotSelected())
        )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Items")
                .and(isNotSelected())
        )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNode(
            hasText("Settings")
                .and(isNotSelected())
        )
    }

    @Test
    fun navgraph_sideNavigationToCollectionsScreen_navigateToCollectionsScreen() {

        composeTestRule.onRoot().printToLog("MOO")

        composeTestRule
            .onNodeWithContentDescription("navigate to collections", substring = true)
            .performClick()

        composeTestRule.waitForIdle()

        assertEquals(Route.COLLECTIONS, navController.currentDestination?.route)

        composeTestRule.onRoot().printToLog("MOO")
    }

    // TODO: why does the exact same test with the exact same setup pass when navigating
    // to collections but fail when side navigating to boxes or items.
    // when navigating to collections, counts are correct, when navigating to boxes or items, counts
    // are zero and side navigation icons are not displayed
    @Test
    fun navgraph_sideNavigationToBoxesScreen_navigateToBoxesScreen() {
        composeTestRule
            .onNodeWithContentDescription("navigate to boxes", substring = true)
            .performClick()

        composeTestRule.waitForIdle()

        assertEquals(Route.BOXES, navController.currentDestination?.route)
    }

    @Test
    fun navgraph_sideNavigationToItemsScreen_navigateToItemsScreen() {
        composeTestRule
            .onNodeWithContentDescription("navigate to items", substring = true)
            .performClick()

        composeTestRule.waitForIdle()

        assertEquals(Route.ITEMS, navController.currentDestination?.route)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navgraph_bottomNavigateToCollectionsScreen_navigateToCollectionsScreen() {

        composeTestRule
            .onNodeWithContentDescription("Drag handle")
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Collections")
                .and(isNotSelected())
        )
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .waitUntilExactlyOneExists(
                hasContentDescription("collections list", substring = true),
                timeoutMillis = 9_000L,
            )
        composeTestRule.waitForIdle()

        // TESTS PASSS because node heirachy is udated,  BUT NOTHING IS DISPLAYED
        assertEquals(Route.COLLECTIONS, navController.currentDestination?.route)
        composeTestRule.onNodeWithContentDescription("collections list", substring = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navgraph_bottomNavigateToBoxesScreen_navigateToBoxesScreen() {

        composeTestRule
            .onNodeWithContentDescription("Drag handle")
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Boxes")
                .and(isNotSelected())
        )
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .waitUntilExactlyOneExists(
                hasContentDescription("boxes list", substring = true),
                timeoutMillis = 9_000L,
            )
        composeTestRule.waitForIdle()

        assertEquals(Route.BOXES, navController.currentDestination?.route)
        composeTestRule.onNodeWithContentDescription("boxes list", substring = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navgraph_bottomNavigateToItemsScreen_navigateToItemsScreen() {

        composeTestRule
            .onNodeWithContentDescription("Drag handle")
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Items")
                .and(isNotSelected())
        )
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .waitUntilExactlyOneExists(
                hasContentDescription("items list", substring = true),
                timeoutMillis = 9_000L,
            )
        composeTestRule.waitForIdle()

        assertEquals(Route.ITEMS, navController.currentDestination?.route)
        composeTestRule.onNodeWithContentDescription("items list", substring = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navgraph_bottomNavigateToSettingsScreen_navigateToSettingsScreen() {
        composeTestRule
            .onNodeWithContentDescription("Drag handle")
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Settings")
                .and(isNotSelected())
        )
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .waitUntilExactlyOneExists(
                hasText("settings", substring = true, ignoreCase = true),
                timeoutMillis = 9_000L,
            )
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("settings", ignoreCase = true)
            .assertExists()
            .assertIsDisplayed()

        assertEquals(Route.SETTINGS, navController.currentDestination?.route)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navgraph_navigateToScreenAndBack_navigateToPreviousScreen() {

        composeTestRule
            .onNodeWithContentDescription("Drag handle")
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.waitForIdle()

        composeTestRule.onNode(
            hasText("Collections")
                .and(isNotSelected())
        )
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .waitUntilExactlyOneExists(
                hasContentDescription("collections list", substring = true),
                timeoutMillis = 9_000L,
            )
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithContentDescription("back", substring = true)
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .waitUntilExactlyOneExists(
                hasContentDescription("Drag handle", substring = true, ignoreCase = true),
                timeoutMillis = 9_000L,
            )

        assertEquals(Route.SUMMARY, navController.currentDestination?.route)

    }
    // TEST
    //   display snackbar
    //   when no collections, boxes, items
    //     can't navigate to boxes, items because side naivgation icons are not displayed, and bottom naivgations are disabled
    // change state
    //   update collections count so boxes side naviggation is displayed and bottom navigation is enabled
    //   update boxes count so items side navigation is displayed and bottom navigation is enabled
}

//class MyNavHostTests {
////    @Test
////    fun navgraph_snackbarDisplaysMessage() {
////        composeTestRule.setContent {
////            SnackbarHost(hostState = snackbarHostState)
////        }
////
////        // Show a snackbar message
////        composeTestRule.runOnUiThread {
////            snackbarHostState.showSnackbar("Test Message")
////        }
////
////        // Verify snackbar is displayed with the correct message
////        composeTestRule.onNodeWithText("Test Message").assertIsDisplayed()
////    }
//
////    @Test
////    fun navgraph_viewModelStateIsUpdatedCorrectly() {
////        // Trigger a ViewModel function to update state
////        viewModel.someFunction()
////
////        // Assert that the ViewModel's state is updated as expected
////        assertEquals(viewModel.state.value, "Updated State")
////    }
//
//    @Test
//    fun navgraph_navigateToAddBoxesScreen_displaysAddBoxesScreen() {
//        // Navigate to Add Boxes screen with a collectionId
//        val collectionId = "123"
//        composeTestRule.runOnUiThread {
//            navController.navigate("${Route.ADD_BOXES}/$collectionId")
//        }
//
//        // Verify that the current destination is Add Boxes
//        composeTestRule.waitForIdle()
//        assertEquals(navController.currentDestination?.route, "${Route.ADD_BOXES}/$collectionId")
//    }
//
//    @Test
//    fun navgraph_navigateToAddItemsScreen_displaysAddItemsScreen() {
//        // Navigate to Add Items screen with a boxId
//        val boxId = "456"
//        composeTestRule.runOnUiThread {
//            navController.navigate("${Route.ADD_ITEMS}/$boxId")
//        }
//
//        // Verify that the current destination is Add Items
//        composeTestRule.waitForIdle()
//        assertEquals(navController.currentDestination?.route, "${Route.ADD_ITEMS}/$boxId")
//    }
//}

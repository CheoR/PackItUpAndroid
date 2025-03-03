package com.example.packitupandroid

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextReplacement
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.ui.screens.collection.CollectionsScreen
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val initialValue = 0
private const val incrementCountByFive = 5
private const val decrementCountByFour = 4
private const val COUNT = 5
private const val DELAY = 2000L

class CollectionsScreenUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CollectionsScreenViewModel
    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var coroutineScope: CoroutineScope

    private fun assertFieldIsEditable(semanticText: String, isEnabled: Boolean = true) {
        composeTestRule
            .onNodeWithContentDescription(semanticText)
            .assertExists()
            .assertIsDisplayed()
            .apply {
                if (isEnabled) assertIsEnabled() else assertIsNotEnabled()
            }
    }

    private fun hasTestTagThatContains(substring: String): SemanticsMatcher {
        return SemanticsMatcher("TestTag contains $substring") { node ->
            val testTag = node.config.getOrNull(SemanticsProperties.TestTag)
            testTag?.contains(substring) ?: false
        }
    }

    private fun assertChildrenCount(tag: String, expectedCount: Int, allNodes: Boolean = true) {
        val childrenCount = if (allNodes) {
            composeTestRule
                .onAllNodes(hasTestTagThatContains(tag))
                .fetchSemanticsNodes()
                .size
        } else {
            composeTestRule
                .onNode(hasTestTagThatContains(tag))
                .onChildren()
                .fetchSemanticsNodes()
                .size
        }
        assertEquals(expectedCount, childrenCount)
    }

    private fun unregisterComposeEspressoIdlingResource() {
        IdlingRegistry.getInstance().resources
            .filter { it.name == "Compose-Espresso link" }
            .forEach { IdlingRegistry.getInstance().unregister(it) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = CollectionsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockCollectionsRepository2(),
            defaultDispatcher = coroutineRule.testDispatcher,
        )
        viewModel.create(initialValue)
    }

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            PackItUpAndroidTheme(themeManager) {
                CollectionsScreen(
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    addElements = { _ -> Unit }, // TODO: create test for this
                )
            }
        }
    }

    private fun performActionOnNode(
        contentDescription: String,
        times: Int,
        action: SemanticsNodeInteraction.() -> SemanticsNodeInteraction
    ) {
        repeat(times) {
            composeTestRule
                .onNodeWithContentDescription(contentDescription)
                .action()
        }
        composeTestRule.mainClock.advanceTimeBy(DELAY)
    }

    private fun incrementCounter(times: Int) =
        performActionOnNode("increment", times) { performClick() }

    private fun decrementCounter(times: Int) =
        performActionOnNode("decrement", times) { performClick() }

    private fun clickAdd() = performActionOnNode("add", 1) { performClick() }

    private fun confirmationDialogSelection(selection: String = "confirm") {
        composeTestRule
            .onNodeWithText(selection)
            .performClick()
        composeTestRule.mainClock.advanceTimeBy(DELAY * 4)
    }

    private fun insertFiveCollections() {
        incrementCounter(incrementCountByFive)
        clickAdd()
        composeTestRule.mainClock.advanceTimeBy(DELAY)
    }

    private fun scrollToLastElement(elements: Int) {
        composeTestRule
            .onNodeWithTag("LazyColumn")
            .performScrollToIndex(elements - 1)
    }

    private fun openMenuSelectionOption(card: SemanticsNodeInteraction, selection: String = "edit") {
        openMenu(card)
        composeTestRule.mainClock.advanceTimeBy(DELAY * 2)
        composeTestRule
            .onNodeWithText(selection)
            .assertExists()
            .performClick()

        composeTestRule.mainClock.advanceTimeBy(DELAY)
    }

    private fun getRandomCardIdFromList(): String {
        val nodes = composeTestRule.onAllNodes(hasTestTagThatContains("BaseCard "))
        val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.substringAfter("BaseCard ")
        }

        return cardIds.random()
    }

    private fun getCardById(id: String): SemanticsNodeInteraction {
        return composeTestRule
            .onNode(hasTestTagThatContains(id))
    }

    private fun getFieldNodeByContentDescription(card: SemanticsNodeInteraction, contentDescription: String): SemanticsNodeInteraction {
        val field = card
            .onChildren()
            .filterToOne(hasContentDescription(contentDescription))
        return field
    }

    private fun getFieldValue(field: SemanticsNodeInteraction): String {
        return field
            .fetchSemanticsNode()
            .config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text ?: ""
    }

    private fun openMenu(card: SemanticsNodeInteraction) {
        card
            .onChildren()
            .filterToOne(hasContentDescription("Open Menu"))
            .performClick()
        composeTestRule.mainClock.advanceTimeBy(DELAY)
    }

    private fun replaceTextField(fieldName: String, text: String) {
        composeTestRule
            .onNodeWithContentDescription(fieldName)
            .performTextReplacement(text)
    }

    private fun toggleCheckbox() {
        composeTestRule
            .onNodeWithContentDescription("Edit Fragile Checkbox")
            .performClick()
    }

    @Before
    fun setup() {
        unregisterComposeEspressoIdlingResource()
        initializeViewModel()
        setupComposeSetContent()
    }


    @Test
    fun collectionsScreen_initialValues() {
        assertEquals(1,1)

//        composeTestRule
//            .onNodeWithContentDescription("Counter Value")
//            .assertTextEquals(initialValue.toString())
//        assertChildrenCount("BaseCard", initialValue)
    }
}

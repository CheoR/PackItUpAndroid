package com.example.packitupandroid.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextReplacement
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.asCurrencyString
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val initialValue = 0
private const val incrementCountByFive = 5
private const val decrementCountByFour = 4

private fun getBoxList(): List<BoxIdAndName> {
    return listOf(
        BoxIdAndName("1", "Box 1"),
        BoxIdAndName("2", "Box 2"),
        BoxIdAndName("3", "Box 3"),
        BoxIdAndName("4", "Box 4")
    )
}

class ItemsScreenUiTests {

    @get:Rule
    val composeTestRule = createComposeRule() // createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ItemsScreenViewModel
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
//        composeTestRule.unregisterIdlingResource(idlingResource).
//        IdlingRegistry.getInstance().resources.forEach {
//            if (it.name == "Compose-Espresso link") {
//                IdlingRegistry.getInstance().unregister(it)
//            }
//        }
//        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
//        IdlingRegistry.getInstance().register(composeTestRule.idlingResource)
//        composeTestRule.registerIdlingResource(idlingResource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = ItemsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockItemsRepository2(),
            defaultDispatcher = coroutineRule.testDispatcher,
        )
        viewModel.create(initialValue)
    }

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                ItemsScreen(
                    viewModel = viewModel,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
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
        composeTestRule.mainClock.advanceTimeBy(DELAY)
    }

    private fun insertFiveItems() {
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

//    @After
//    fun tearDown() {
//        IdlingRegistry.getInstance().unregister(composeTestRule.idlingResource)
//    }

    @Test
    fun testItemsScreen_initialValues() {
        composeTestRule
            .onNodeWithText("add")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        composeTestRule
            .onNodeWithContentDescription("Counter Value")
            .assertTextEquals(initialValue.toString())

        assertChildrenCount("BaseCard", initialValue)
    }

    @Test
    fun testItemsScreen_incrementByFive_fiveItemsInserted() {
        incrementCounter(incrementCountByFive)
        clickAdd()
        scrollToLastElement(incrementCountByFive)

        composeTestRule
            .onNodeWithText("Item $incrementCountByFive")
            .assertExists()
    }

    @Test
    fun testItemsScreen_incrementByTwentyFive_twentyFiveItemsInserted() {
        val incrementValueByTwentyFive = incrementCountByFive * 5

        incrementCounter(incrementValueByTwentyFive)
        clickAdd()
        scrollToLastElement(incrementValueByTwentyFive)

        composeTestRule
            .onNodeWithText("Item $incrementValueByTwentyFive")
            .assertExists()
    }

    @Test
    fun testItemsScreen_incrementByFive_decrementByFour_oneItemInserted() {
        val result = incrementCountByFive - decrementCountByFour

        incrementCounter(incrementCountByFive)
        decrementCounter(decrementCountByFour)
        clickAdd()
        scrollToLastElement(result)

        assertChildrenCount("BaseCard", result)
    }

    @Test
    fun testItemsScreen_incrementByTen_decrementByEight_twoItemInserted() {
        val result =
            incrementCountByFive + incrementCountByFive - decrementCountByFour - decrementCountByFour

        incrementCounter(incrementCountByFive + incrementCountByFive)
        decrementCounter(decrementCountByFour + decrementCountByFour)
        clickAdd()
        scrollToLastElement(result)

        assertChildrenCount("BaseCard", result)
    }

    @Test
    fun testItemsScreen_decrementBelowZero_zeroItemsInserted() {
        decrementCounter(incrementCountByFive)
        composeTestRule
            .onNodeWithText("add")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        clickAdd()

        assertChildrenCount("BaseCard", initialValue)
    }

    @Test
    fun testItemsScreen_deleteOneItem_fourItemsRemain() {
        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"delete")
        confirmationDialogSelection("confirm")
        scrollToLastElement(incrementCountByFive - 1)

        assertChildrenCount("BaseCard", incrementCountByFive - 1)
    }

    @Test
    fun testItemsScreen_cancelDeleteOneItem_fiveItemsRemain() {
        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"delete")
        confirmationDialogSelection("cancel")
        scrollToLastElement(incrementCountByFive)

        assertChildrenCount("LazyColumn", incrementCountByFive, false)
    }

    @Test
    fun testItemsScreen_cancelDeleteOneItem_tapOffScreen_FiveItemsRemain() {
        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"delete")

        composeTestRule
            .onNodeWithContentDescription("Delete Card")
            .assertExists()

        // because could not get goBack() to work
        composeTestRule
            .onNodeWithTag("LazyColumn")
            .performClick()

        scrollToLastElement(incrementCountByFive)

        assertChildrenCount("LazyColumn", incrementCountByFive, false)
    }

    @Test
    fun testItemsScreen_editOneItem_hasEditableFields() {
        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"edit")

        assertFieldIsEditable("Edit Name Field")
        assertFieldIsEditable("Edit Dropdown Selection Value")
        assertFieldIsEditable("Edit Description Field")
        assertFieldIsEditable("Edit Fragile Checkbox")
        assertFieldIsEditable("Edit Value Field")
    }

    @Test
    fun testItemsScreen_editOneItem_editPersist() {
        val newName = "MOO COW"
        val newDescription = "OINK OINK"
        val newValue = 123.45

        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)
        val nameField = getFieldNodeByContentDescription(card, "Name Field")
        val descriptionField = getFieldNodeByContentDescription(card, "Description Field")
        val oldNameFieldValue = getFieldValue(nameField)

        openMenuSelectionOption(card,"edit")
        replaceTextField("Edit Name Field", newName)
        replaceTextField("Edit Description Field", newDescription)
        replaceTextField("Edit Value Field", newValue.asCurrencyString())
        toggleCheckbox()
        confirmationDialogSelection()

        val subComposables = card.onChildren()

        subComposables.apply {
            filterToOne(hasText((newName)))
                .assertExists()
            filterToOne(hasText((newDescription)))
                .assertExists()
            filterToOne(hasText(newValue.asCurrencyString()))
                .assertExists()
            filterToOne(hasContentDescription("Fragile Checkbox"))
                .assertIsOn()
        }

        composeTestRule
            .onNodeWithText(oldNameFieldValue)
            .assertDoesNotExist()
    }

    @Test
    fun testItemsScreen_captureImageOneItem_imagePersist() {
        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)
        val nameField = getFieldNodeByContentDescription(card, "Name Field")
        val descriptionField = getFieldNodeByContentDescription(card, "Description Field")
        val oldNameFieldValue = getFieldValue(nameField)

        openMenuSelectionOption(card,"camera")
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(DELAY * 10)

        confirmationDialogSelection()

        val subComposables = card.onChildren()

        // TODO: wait for image preview to load before taking image
    }

    @Test
    fun testItemsScreen_updateBoxDropdown_boxPersist() {
        insertFiveItems()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"edit")
        val editCard = composeTestRule.onNodeWithContentDescription("Edit Card")

        editCard
            .onChildren()
            .onFirst()
            .onChildren()
            .filterToOne(hasContentDescription("Icon Selector"))
            .performClick()

        composeTestRule.mainClock.advanceTimeBy(DELAY * 20)

        composeTestRule.onNodeWithContentDescription("Edit Dropdown Menu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Box 2")
            .performClick()
        confirmationDialogSelection()

        composeTestRule
            .onNodeWithText("Box 2")
            .assertExists()
    }

    companion object {
        private const val COUNT = 5
        private const val DELAY = 2000L
    }
}

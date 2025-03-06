package com.example.packitupandroid.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.repository.MockBoxesRepository2
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.ui.screens.box.BoxesScreen
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val initialValue = 0
private const val incrementCountByFive = 5
private const val decrementCountByFour = 4

private fun getBoxList(): List<CollectionIdAndName> {
    return listOf(
        CollectionIdAndName("1", "Collection 1"),
        CollectionIdAndName("2", "Collection 2"),
        CollectionIdAndName("3", "Collection 3"),
        CollectionIdAndName("4", "Collection 4")
    )
}

class BoxesScreenUiTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BoxesScreenViewModel
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
        TestCase.assertEquals(expectedCount, childrenCount)
    }

    private fun unregisterComposeEspressoIdlingResource() {
        IdlingRegistry.getInstance().resources
            .filter { it.name == "Compose-Espresso link" }
            .forEach { IdlingRegistry.getInstance().unregister(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = BoxesScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockBoxesRepository2(),
            defaultDispatcher = coroutineRule.testDispatcher,
        )
        viewModel.create(initialValue)
    }

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                BoxesScreen(
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

    private fun insertFiveBoxes() {
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
    fun boxesScreen_initialValues() {

        composeTestRule
            .onNodeWithContentDescription("Counter Value")
            .assertTextEquals(initialValue.toString())

        assertChildrenCount("BaseCard", initialValue)
    }

    @Test
    fun testBoxesScreen_incrementByFive_fiveBoxesInserted() {
        incrementCounter(incrementCountByFive)
        clickAdd()
        scrollToLastElement(incrementCountByFive)

        composeTestRule
            .onNodeWithText("Box $incrementCountByFive")
            .assertExists()
    }

    @Test
    fun testBoxesScreen_incrementByTwentyFive_twentyFiveBoxesInserted() {
        val incrementValueByTwentyFive = incrementCountByFive * 5

        incrementCounter(incrementValueByTwentyFive)
        clickAdd()
        scrollToLastElement(incrementValueByTwentyFive)

        composeTestRule
            .onNodeWithText("Box $incrementValueByTwentyFive")
            .assertExists()
    }

    @Test
    fun testBoxesScreen_incrementByFive_decrementByFour_oneBoxInserted() {
        val result = incrementCountByFive - decrementCountByFour

        incrementCounter(incrementCountByFive)
        decrementCounter(decrementCountByFour)
        clickAdd()
        scrollToLastElement(result)

        assertChildrenCount("BaseCard", result)
    }

    @Test
    fun testBoxesScreen_incrementByTen_decrementByEight_twoBoxInserted() {
        val result =
            incrementCountByFive + incrementCountByFive - decrementCountByFour - decrementCountByFour

        incrementCounter(incrementCountByFive + incrementCountByFive)
        decrementCounter(decrementCountByFour + decrementCountByFour)
        clickAdd()
        scrollToLastElement(result)

        assertChildrenCount("BaseCard", result)
    }

    @Test
    fun testBoxesScreen_decrementBelowZero_zeroBoxesInserted() {
        decrementCounter(incrementCountByFive)
        composeTestRule
            .onNodeWithText("add")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        clickAdd()

        assertChildrenCount("BaseCard", initialValue)
    }

    @Test
    fun testBoxesScreen_deleteOneBox_fourBoxesRemain() {
        insertFiveBoxes()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"delete")
        confirmationDialogSelection("confirm")

        scrollToLastElement(incrementCountByFive - 1)

        assertChildrenCount("BaseCard", incrementCountByFive - 1)
    }

    @Test
    fun testBoxesScreen_cancelDeleteOneItem_fiveBoxesRemain() {
        insertFiveBoxes()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"delete")
        confirmationDialogSelection("cancel")
        scrollToLastElement(incrementCountByFive)

        assertChildrenCount("LazyColumn", incrementCountByFive, false)
    }

    @Test
    fun testBoxesScreen_cancelDeleteOneBox_tapOffScreen_FiveBoxesRemain() {
        insertFiveBoxes()

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
    fun testBoxesScreen_editOneBox_hasEditableFields() {
        insertFiveBoxes()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)

        openMenuSelectionOption(card,"edit")

        assertFieldIsEditable("Edit Name Field")
        assertFieldIsEditable("Edit Dropdown Selection Value")
        assertFieldIsEditable("Edit Description Field")
        assertFieldIsEditable("Edit Fragile Checkbox", false)
        assertFieldIsEditable("Edit Value Field", false)
    }

    @Test
    fun testBoxesScreen_editOneItem_editPersist() {
        val newName = "MOO COW"
        val newDescription = "OINK OINK"

        insertFiveBoxes()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)
        val nameField = getFieldNodeByContentDescription(card, "Name Field")
        val descriptionField = getFieldNodeByContentDescription(card, "Description Field")
        val oldNameFieldValue = getFieldValue(nameField)

        openMenuSelectionOption(card,"edit")
        replaceTextField("Edit Name Field", newName)
        replaceTextField("Edit Description Field", newDescription)
//        replaceTextField("Edit Value Field", newValue.asCurrencyString())
//        toggleCheckbox()
        confirmationDialogSelection()

        val subComposables = card.onChildren()

        subComposables.apply {
            filterToOne(hasText((newName)))
                .assertExists()
            filterToOne(hasText((newDescription)))
                .assertExists()
//            filterToOne(hasText(newValue.asCurrencyString()))
//                .assertExists()
//            filterToOne(hasContentDescription("Fragile Checkbox"))
//                .assertIsOn()
        }

        composeTestRule
            .onNodeWithText(oldNameFieldValue)
            .assertDoesNotExist()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun boxesScreen_updateItemValue_boxValueUpdated() {

        val itemsViewModel = ItemsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockItemsRepository2(),
            defaultDispatcher = coroutineRule.testDispatcher,
        )

        insertFiveBoxes()

        val id = getRandomCardIdFromList()
        val card = getCardById(id)
//        val value =
//        card.printToLog("BOXES SCREEN")
//
//        val children = card.onChildren()
//        val text = children.filterToOne(hasContentDescription("Badge1 count"))
//        println("value is ${text}")
//        text.printToLog("BOXES SCREEN")
//        children.printToLog("BOXES SCREEN")


//        println("children: ${children.}")
        val badge1CountNode = getFieldNodeByContentDescription(card, "Badge1 count")
        val badge1CountValue = getFieldValue(badge1CountNode)

        badge1CountNode.printToLog("badge1CountNode: ")
        println("badge1CountValue: $badge1CountValue.")

        itemsViewModel.create(COUNT)
//        val result = itemsViewModel.elements.value
//        val items = Result.Success(result).data // Result(itemsViewModel.elements.value) as List<Item?>

//        val result: Result<Item?>>? = itemsViewModel.elements.value
//        val items: List<Item?>? = (result as? Result.Success)?.data

        val items = when(val result = itemsViewModel.elements.value) {
            is Result.Success -> result.data
            else -> emptyList()
        }


        for (item in items) {
            item?.copy(
                boxId = id,
            )?.let {
                itemsViewModel.update(
                    it
                )
            }
        }
        composeTestRule.mainClock.advanceTimeBy(DELAY)

//        viewModel.refreshUIState()
//
//        composeTestRule.mainClock.advanceTimeBy(DELAY)

        openMenuSelectionOption(card,"edit")
        replaceTextField("Edit Name Field", "Add stuff")
        confirmationDialogSelection()
        composeTestRule.mainClock.advanceTimeBy(DELAY)

        println("items: $items")

        val badge1CountNode2 = getFieldNodeByContentDescription(card, "Badge1 count")
        val badge1CountValue2 = getFieldValue(badge1CountNode)

        badge1CountNode.printToLog("badge1CountNode2: ")
        println("badge1CountValue: $badge1CountValue2.")

        TestCase.assertEquals(initialValue + COUNT, badge1CountValue2)

//        val count = card
////            .fetchSemanticsNode()
////            .config
//            .onChildren()
//            .filterToOne(hasContentDescription("Badge1 Count"))

//        val countValue = getFieldValue(count)
//        println("countValue is $count")
//        count.printToLog("BOXES SCREEN")
//        count.printToLog("count")
    }

    companion object {
        private const val COUNT = 5
        private const val DELAY = 2000L
    }
}
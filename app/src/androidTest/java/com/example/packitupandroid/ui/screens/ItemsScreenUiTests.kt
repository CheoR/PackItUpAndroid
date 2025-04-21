package com.example.packitupandroid.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.repository.MockItemsRepository2
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


//typealias ParameterizedCollection = Collection<Array<Any>>
//
//private sealed class RefGroup {
//    object Loading : RefGroup()
//    object Success : RefGroup()
//    object Error : RefGroup()
//}
//
//val onFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {
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
//val generateIconsColumn = fun (element: Item): @Composable (ColumnScope.() -> Unit) {
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

const val initialValue = 5

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class ItemsScreenUiTests {

    @get:Rule
    val composeTestRule = createComposeRule() // createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ItemsScreenViewModel
    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var coroutineScope: CoroutineScope

    val testData = TestDataSource()
    val items = testData.items

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initializeViewModel() {
        viewModel = ItemsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = MockItemsRepository2(),
            defaultDispatcher = coroutineRule.testDispatcher,
        )
        viewModel.create(initialValue)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
//        coroutineScope = CoroutineScope(coroutineRule.testDispatcher)
        unregisterComposeEspressoIdlingResource()
//        snackbarHostState = SnackbarHostState()

        initializeSnackbar()
        initializeCoroutineScope()
        initializeViewModel()
        setupComposeSetContent()

//        assertNotNull(viewModel)
//        assertNotNull(coroutineScope)
    }

//    @After
//    fun tearDown() {
//        IdlingRegistry.getInstance().unregister(composeTestRule)
//    }
//    companion object {
//        @JvmStatic
//        @Parameters(name = "{index}: group={0}, result={1}")
//        fun data(): ParameterizedCollection {
//            val testData = TestDataSource()
//            val items = testData.items
//            return listOf(
//                arrayOf(RefGroup.Loading, Result.Loading),
//                arrayOf(RefGroup.Success, Result.Success(items)),
//                arrayOf(RefGroup.Error, Result.Error(Exception("Test Error")))
//            )
//        }
//    }

    @Test
    fun itemsScreen_init_noLazyColumnListElementsExist() {
        composeTestRule
            .onRoot()
            .printToLog("MOO")
//            .onAllNodesWithContentDescription("base card", substring = true, ignoreCase = true) // Replace with the actual content description for your cards
//            .assertCountEquals(30)
//            .onAllNodes(hasContentDescription("base card", ignoreCase = true, substring = true))
//            .assertCountEquals(0)
    }
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
}


//    private fun unregisterComposeEspressoIdlingResource() {
//        IdlingRegistry.getInstance().resources
//            .filter { it.name == "Compose-Espresso link" }
//            .forEach { IdlingRegistry.getInstance().unregister(it) }
//        composeTestRule.unregisterIdlingResource(idlingResource).
//        IdlingRegistry.getInstance().resources.forEach {
//            if (it.name == "Compose-Espresso link") {
//                IdlingRegistry.getInstance().unregister(it)
//            }
//        }
//        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
//        IdlingRegistry.getInstance().register(composeTestRule.idlingResource)
//        composeTestRule.registerIdlingResource(idlingResource)
//    }



//
//    @Test
//    fun itemsScreen_initEmptyList_emptyListExistAndIsEmpty() {
//        val list = composeTestRule
//            .onNodeWithContentDescription("list is empty")
//
//        list
//            .assertExists()
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun itemsScreen_initEmptyList_emptyListTextIsDisplayed() {
//        composeTestRule
//            .onNodeWithText("no items found")
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun itemsScreen_init_noLazyColumnListExist() {
//        composeTestRule
//            .onAllNodes(hasContentDescription("items list"))
//            .assertCountEquals(0)
//    }
//


    // TODO: move to common test since it is used in multiple tests
//    @Test
//    fun itemsScreen_initialValues() {
//        composeTestRule
//            .onNodeWithText("add")
//            .assertIsDisplayed()
//            .assertIsNotEnabled()
//
//        composeTestRule
//            .onNodeWithContentDescription("Counter Value $initialValue", ignoreCase = true, substring = true)
//            .assertTextEquals(initialValue.toString())
//
//        composeTestRule.assertChildrenCount("BaseCard", initialValue)
//    }

//
//    @Test
//    fun itemsScreen_incrementByFive_fiveItemsDisplayed() {
//        composeTestRule.incrementCounter(incrementCountByFive)
//        composeTestRule.clickAdd()
//
//        // TODO: replace with LazyState to get all children instead of neeeding to scroll down
//        // to view all children before assertion
//        composeTestRule
//            .scrollToLastElement("items", incrementCountByFive)
//
//        composeTestRule
//            .onAllNodes(hasContentDescription("base card"))
//            .assertCountEquals(incrementCountByFive)
//    }
//
//    @Test
//    fun itemsScreen_incrementByTwentyFive_twentyFiveItemsDisplayed() {
//        val incrementValueByTwentyFive = incrementCountByFive * 5
//
//        composeTestRule.incrementCounter(incrementValueByTwentyFive)
//        composeTestRule.clickAdd()
//        composeTestRule.scrollToLastElement("items", incrementValueByTwentyFive)
//
//        composeTestRule
//            .onNodeWithText("Item $incrementValueByTwentyFive")
//            .assertExists()
//    }
//
//    @Test
//    fun itemsScreen_incrementByFive_decrementByFour_oneItemDisplayed() {
//        val count = incrementCountByFive - decrementCountByFour
//
//        composeTestRule.incrementCounter(incrementCountByFive)
//        composeTestRule.decrementCounter(decrementCountByFour)
//        composeTestRule.clickAdd()
//        composeTestRule.scrollToLastElement("items", count)
//
//        composeTestRule
//            .onAllNodes(hasContentDescription("base card"))
//            .assertCountEquals(count)
//    }
//

//    @Test
//    fun itemsScreen_deleteOneItem_fourItemsDisplayed() {
//        composeTestRule.insertFiveItems()
//
//        val cardNode = composeTestRule
//            .onNodeWithText("Item 3", useUnmergedTree = true)
//            .onChildren()
////            .assertExists()
////            .assertIsDisplayed()
//
//        cardNode.printToLog("cardNode")
//

//        val parentNode = composeTestRule
//            .onNodeWithText("Item 3", useUnmergedTree = true)
//        parentNode.assertIsDisplayed()


//        val childrenNodes = parentNode
//            .onChildren()
//            .fetchSemanticsNodes()


//        composeTestRule.onNodeWithContentDescription("items list")
        // todo: find use utils to find way to "swipe left to open menu"
//        val cardNode = composeTestRule
//            .onNodeWithText("Item 3", useUnmergedTree = true)
//            .assertExists()
//            .assertIsDisplayed()

//// Fetch the semantics node for `cardNode`
//        val cardSemanticsNode = cardNode.fetchSemanticsNode()
//
//// Traverse the children of `cardNode` to locate the swipe column
//        val swipeColumnNode = cardSemanticsNode.children.firstOrNull { child ->
//            child.config.getOrNull(SemanticsProperties.ContentDescription)?.contains("swipe left to open menu") == true
//        } ?: throw AssertionError("Swipe column node with contentDescription 'open_menu' not found.")
//
//// Ensure the swipeColumnNode is valid
//        assert(swipeColumnNode.config.get(SemanticsProperties.ContentDescription) == "swipe left to open menu") {
//            "Expected ContentDescription 'open_menu' not found in swipeColumnNode."
//        }




//        println("\n\n\t\tchildrenNodes: ${childrenNodes.size}")
//        val swipeableColumnNode = childrenNodes.firstOrNull { node ->
//            val contentDescriptions = node.config.getOrNull(SemanticsProperties.ContentDescription)
//            contentDescriptions?.contains("swipe left to open menu") == true }
//        assert(swipeableColumnNode != null)
        //"swipe left to open menu") // .fetchSemanticsNode("swipe left to open menu")

//        swipeable.config.getOrNull(SemanticsProperties.ContentDescription)

        // (hasContentDescription("Open Menu"))

//        node.onChildren(hasContentDescription("swipe left to open menu"))
//        val nodes = composeTestRule
//            .onAllNodes(hasContentDescription("base card"))
//
//        if (nodes.fetchSemanticsNodes().isEmpty()) {
//            throw IllegalStateException("No cards found!")
//        }

//        val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
//            node.config.getOrNull(SemanticsProperties.TestTag)?.substringAfter("BaseCard ")
//        }
//        val node = nodes[3]
//        val id = node.config[hasContentDescription("id")]
//    }

//        val id = composeTestRule.getRandomCardIdFromList("edit card")
//        val card = composeTestRule.getCardById(id)
//
//        // TODO: add check for no card
//        composeTestRule.openMenuSelectionOption(card,"delete")
//        composeTestRule.confirmationDialogSelection("confirm")
//        composeTestRule.scrollToLastElement(incrementCountByFive - 1)
//
//        composeTestRule.assertChildrenCount("BaseCard", incrementCountByFive - 1)
//    }

//    @Test
//    fun itemsScreen_cancelDeleteOneItem_fiveItemsRemain() {
//        composeTestRule.insertFiveItems()
//
//        val id = composeTestRule.getRandomCardIdFromList("edit card")
//        val card = composeTestRule.getCardById(id)
//
//        composeTestRule.openMenuSelectionOption(card,"delete")
//        composeTestRule.confirmationDialogSelection("cancel")
//        composeTestRule.scrollToLastElement(incrementCountByFive)
//
//        composeTestRule.assertChildrenCount("LazyColumn", incrementCountByFive, false)
//    }
//
//    @Test
//    fun itemsScreen_cancelDeleteOneItem_tapOffScreen_FiveItemsRemain() {
//        composeTestRule.insertFiveItems()
//
//        val id = composeTestRule.getRandomCardIdFromList("edit card")
//        val card = composeTestRule.getCardById(id)
//
//        composeTestRule.openMenuSelectionOption(card,"delete")
//
//        composeTestRule
//            .onNodeWithContentDescription("Delete Card")
//            .assertExists()
//
//        // because could not get goBack() to work
//        composeTestRule
//            .onNodeWithTag("LazyColumn")
//            .performClick()
//
//        composeTestRule.scrollToLastElement(incrementCountByFive)
//
//        composeTestRule.assertChildrenCount("LazyColumn", incrementCountByFive, false)
//    }

//    @Test
//    fun itemsScreen_editOneItem_hasEditableFields() {
//        composeTestRule.insertFiveItems()
//
////        val id = composeTestRule.getRandomCardIdFromList("basecard")
////        val card = composeTestRule.getCardById(id)
//
//        val id = composeTestRule.getRandomCardIdFromList()
//        val card = composeTestRule.getCardById(id)
//
//        composeTestRule.openMenuSelectionOption(card,"edit")
//
//        composeTestRule.assertFieldIsEditable("Edit Name Field")
////        composeTestRule.assertFieldIsEditable("Edit Dropdown Selection Value")
////        composeTestRule.assertFieldIsEditable("Edit Description Field")
////        composeTestRule.assertFieldIsEditable("Edit Fragile Checkbox")
////        composeTestRule.assertFieldIsEditable("Edit Value Field")
//    }

//    @Test
//    fun itemsScreen_editOneItem_editPersist() {
//        val newName = "MOO COW"
//        val newDescription = "OINK OINK"
//        val newValue = 123.45
//
//        composeTestRule.insertFiveItems()
//
//        val id = composeTestRule.getRandomCardIdFromList("edit card")
//        val card = composeTestRule.getCardById(id)
//        val nameField = composeTestRule.getFieldNodeByContentDescription(card, "Name Field")
//        val descriptionField = composeTestRule.getFieldNodeByContentDescription(card, "Description Field")
//        val oldNameFieldValue = composeTestRule.getFieldValue(nameField)
//
//        composeTestRule.openMenuSelectionOption(card,"edit")
//        composeTestRule.replaceTextField("Edit Name Field", newName)
//        composeTestRule.replaceTextField("Edit Description Field", newDescription)
//        composeTestRule.replaceTextField("Edit Value Field", newValue.asCurrencyString())
//        composeTestRule.toggleCheckbox()
//        composeTestRule.confirmationDialogSelection()
//
//        val subComposables = card.onChildren()
//
//        subComposables.apply {
//            filterToOne(hasText((newName)))
//                .assertExists()
//            filterToOne(hasText((newDescription)))
//                .assertExists()
//            filterToOne(hasText(newValue.asCurrencyString()))
//                .assertExists()
//            filterToOne(hasContentDescription("Fragile Checkbox"))
//                .assertIsOn()
//        }
//
//        composeTestRule
//            .onNodeWithText(oldNameFieldValue)
//            .assertDoesNotExist()
//    }
//
//    @Test
//    fun itemsScreen_captureImageOneItem_imagePersist() {
//        composeTestRule.insertFiveItems()
//
//        val id = composeTestRule.getRandomCardIdFromList("edit card")
//        val card = composeTestRule.getCardById(id)
//        val nameField = composeTestRule.getFieldNodeByContentDescription(card, "Name Field")
//        val descriptionField = composeTestRule.getFieldNodeByContentDescription(card, "Description Field")
//        val oldNameFieldValue = composeTestRule.getFieldValue(nameField)
//
//        composeTestRule.openMenuSelectionOption(card,"camera")
//        composeTestRule.waitForIdle()
//        composeTestRule.mainClock.advanceTimeBy(DELAY * 10)
//
//        composeTestRule.confirmationDialogSelection()
//
//        val subComposables = card.onChildren()
//
//        // TODO: wait for image preview to load before taking image
//    }
//
//    @Test
//    fun itemsScreen_updateBoxDropdown_boxPersist() {
//        composeTestRule.insertFiveItems()
//
//        val id = composeTestRule.getRandomCardIdFromList("edit card")
//        val card = composeTestRule.getCardById(id)
//
//        composeTestRule.openMenuSelectionOption(card,"edit")
//        val editCard = composeTestRule.onNodeWithContentDescription("Edit Card")
//
//        editCard
//            .onChildren()
//            .onFirst()
//            .onChildren()
//            .filterToOne(hasContentDescription("Icon Selector"))
//            .performClick()
//
//        composeTestRule.mainClock.advanceTimeBy(DELAY * 20)
//
//        composeTestRule.onNodeWithContentDescription("Edit Dropdown Menu").assertIsDisplayed()
//        composeTestRule.onNodeWithText("Box 2")
//            .performClick()
//        composeTestRule.confirmationDialogSelection()
//
//        composeTestRule
//            .onNodeWithText("Box 2")
//            .assertExists()
//    }
//}

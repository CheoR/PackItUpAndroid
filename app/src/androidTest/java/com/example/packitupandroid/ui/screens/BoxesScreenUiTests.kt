package com.example.packitupandroid.ui.screens


//class BoxesScreenUiTests {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val coroutineRule = MainCoroutineRule()
//
//    private lateinit var viewModel: BoxesScreenViewModel
//    private lateinit var snackbarHostState: SnackbarHostState
//    private lateinit var coroutineScope: CoroutineScope
//
//    private fun unregisterComposeEspressoIdlingResource() {
//        IdlingRegistry.getInstance().resources
//            .filter { it.name == "Compose-Espresso link" }
//            .forEach { IdlingRegistry.getInstance().unregister(it) }
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun initializeViewModel() {
//        viewModel = BoxesScreenViewModel(
//            savedStateHandle = SavedStateHandle(),
//            repository = MockBoxesRepository2(),
//            defaultDispatcher = coroutineRule.testDispatcher,
//        )
//        viewModel.create(initialValue)
//    }
//
//    private fun setupComposeSetContent() {
//        composeTestRule.setContent {
//            val context = LocalContext.current
//            val themeManager = rememberThemeManager(context)
//
//            PackItUpAndroidTheme(themeManager) {
//                BoxesScreen(
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
//    @Test
//    fun boxesScreen_initialValues() {
//
//        composeTestRule
//            .onNodeWithContentDescription("Counter Value")
//            .assertTextEquals(initialValue.toString())
//
//        composeTestRule.assertChildrenCount("BaseCard", initialValue)
//    }
//
//    @Test
//    fun testBoxesScreen_incrementByFive_fiveBoxesInserted() {
//        composeTestRule.incrementCounter(incrementCountByFive)
//        composeTestRule.clickAdd()
//        composeTestRule.scrollToLastElement(incrementCountByFive)
//
//        composeTestRule
//            .onNodeWithText("Box $incrementCountByFive")
//            .assertExists()
//    }
//
//    @Test
//    fun testBoxesScreen_incrementByTwentyFive_twentyFiveBoxesInserted() {
//        val incrementValueByTwentyFive = incrementCountByFive * 5
//
//        composeTestRule.incrementCounter(incrementValueByTwentyFive)
//        composeTestRule.clickAdd()
//        composeTestRule.scrollToLastElement(incrementValueByTwentyFive)
//
//        composeTestRule
//            .onNodeWithText("Box $incrementValueByTwentyFive")
//            .assertExists()
//    }
//
//    @Test
//    fun testBoxesScreen_incrementByFive_decrementByFour_oneBoxInserted() {
//        val result = incrementCountByFive - decrementCountByFour
//
//        composeTestRule.incrementCounter(incrementCountByFive)
//        composeTestRule.decrementCounter(decrementCountByFour)
//        composeTestRule.clickAdd()
//        composeTestRule.scrollToLastElement(result)
//
//        composeTestRule.assertChildrenCount("BaseCard", result)
//    }
//
//    @Test
//    fun testBoxesScreen_incrementByTen_decrementByEight_twoBoxInserted() {
//        val result =
//            incrementCountByFive + incrementCountByFive - decrementCountByFour - decrementCountByFour
//
//        composeTestRule.incrementCounter(incrementCountByFive + incrementCountByFive)
//        composeTestRule.decrementCounter(decrementCountByFour + decrementCountByFour)
//        composeTestRule.clickAdd()
//        composeTestRule.scrollToLastElement(result)
//
//        composeTestRule.assertChildrenCount("BaseCard", result)
//    }
//
//    @Test
//    fun testBoxesScreen_decrementBelowZero_zeroBoxesInserted() {
//        composeTestRule.decrementCounter(incrementCountByFive)
//        composeTestRule
//            .onNodeWithText("add")
//            .assertIsDisplayed()
//            .assertIsNotEnabled()
//
//        composeTestRule.clickAdd()
//
//        composeTestRule.assertChildrenCount("BaseCard", initialValue)
//    }
//
//    @Test
//    fun testBoxesScreen_deleteOneBox_fourBoxesRemain() {
//        composeTestRule.insertFiveBoxes()
//
//        val id = composeTestRule.getRandomCardIdFromList("base card")
//        val card = composeTestRule.getCardById(id)
//
//        composeTestRule.openMenuSelectionOption(card,"delete")
//        composeTestRule.confirmationDialogSelection("confirm")
//
//        composeTestRule.scrollToLastElement(incrementCountByFive - 1)
//
//        composeTestRule.assertChildrenCount("BaseCard", incrementCountByFive - 1)
//    }
//
//    @Test
//    fun testBoxesScreen_cancelDeleteOneItem_fiveBoxesRemain() {
//        composeTestRule.insertFiveBoxes()
//
//        val id = composeTestRule.getRandomCardIdFromList("base card")
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
//    fun testBoxesScreen_cancelDeleteOneBox_tapOffScreen_FiveBoxesRemain() {
//        composeTestRule.insertFiveBoxes()
//
//        val id = composeTestRule.getRandomCardIdFromList("base card")
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
//
//    @Test
//    fun testBoxesScreen_editOneBox_hasEditableFields() {
//        composeTestRule.insertFiveBoxes()
//
//        val id = composeTestRule.getRandomCardIdFromList("base card")
//        val card = composeTestRule.getCardById(id)
//
//        composeTestRule.openMenuSelectionOption(card,"edit")
//
//        composeTestRule.assertFieldIsEditable("Edit Name Field")
//        composeTestRule.assertFieldIsEditable("Edit Dropdown Selection Value")
//        composeTestRule.assertFieldIsEditable("Edit Description Field")
//        composeTestRule.assertFieldIsEditable("Edit Fragile Checkbox", false)
//        composeTestRule.assertFieldIsEditable("Edit Value Field", false)
//    }
//
//    @Test
//    fun testBoxesScreen_editOneItem_editPersist() {
//        val newName = "MOO COW"
//        val newDescription = "OINK OINK"
//
//        composeTestRule.insertFiveBoxes()
//
//        val id = composeTestRule.getRandomCardIdFromList("base card")
//        val card = composeTestRule.getCardById(id)
//        val nameField = composeTestRule.getFieldNodeByContentDescription(card, "Name Field")
//        val descriptionField = composeTestRule.getFieldNodeByContentDescription(card, "Description Field")
//        val oldNameFieldValue = composeTestRule.getFieldValue(nameField)
//
//        composeTestRule.openMenuSelectionOption(card,"edit")
//        composeTestRule.replaceTextField("Edit Name Field", newName)
//        composeTestRule.replaceTextField("Edit Description Field", newDescription)
////        composeTestRule.replaceTextField("Edit Value Field", newValue.asCurrencyString())
////        toggleCheckbox()
//        composeTestRule.confirmationDialogSelection()
//
//        val subComposables = card.onChildren()
//
//        subComposables.apply {
//            filterToOne(hasText((newName)))
//                .assertExists()
//            filterToOne(hasText((newDescription)))
//                .assertExists()
////            filterToOne(hasText(newValue.asCurrencyString()))
////                .assertExists()
////            filterToOne(hasContentDescription("Fragile Checkbox"))
////                .assertIsOn()
//        }
//
//        composeTestRule
//            .onNodeWithText(oldNameFieldValue)
//            .assertDoesNotExist()
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun boxesScreen_updateItemValue_boxValueUpdated() {
//
//        val itemsViewModel = ItemsScreenViewModel(
//            savedStateHandle = SavedStateHandle(),
//            repository = MockItemsRepository2(),
//            defaultDispatcher = coroutineRule.testDispatcher,
//        )
//
//        composeTestRule.insertFiveBoxes()
//
//        val id = composeTestRule.getRandomCardIdFromList("base card")
//        val card = composeTestRule.getCardById(id)
////        val value =
////        card.printToLog("BOXES SCREEN")
////
////        val children = card.onChildren()
////        val text = children.filterToOne(hasContentDescription("Badge1 count"))
////        println("value is ${text}")
////        text.printToLog("BOXES SCREEN")
////        children.printToLog("BOXES SCREEN")
//
//
////        println("children: ${children.}")
//        val badge1CountNode = composeTestRule.getFieldNodeByContentDescription(card, "Badge1 count")
//        val badge1CountValue = composeTestRule.getFieldValue(badge1CountNode)
//
//        badge1CountNode.printToLog("badge1CountNode: ")
//        println("badge1CountValue: $badge1CountValue.")
//
//        itemsViewModel.create(COUNT)
////        val result = itemsViewModel.elements.value
////        val items = Result.Success(result).data // Result(itemsViewModel.elements.value) as List<Item?>
//
////        val result: Result<Item?>>? = itemsViewModel.elements.value
////        val items: List<Item?>? = (result as? Result.Success)?.data
//
//        val items = when(val result = itemsViewModel.elements.value) {
//            is Result.Success -> result.data
//            else -> emptyList()
//        }
//
//
//        for (item in items) {
//            item?.copy(
//                boxId = id,
//            )?.let {
//                itemsViewModel.update(
//                    it
//                )
//            }
//        }
//        composeTestRule.mainClock.advanceTimeBy(DELAY)
//
////        viewModel.refreshUIState()
////
////        composeTestRule.mainClock.advanceTimeBy(DELAY)
//
//        composeTestRule.openMenuSelectionOption(card,"edit")
//        composeTestRule.replaceTextField("Edit Name Field", "Add stuff")
//        composeTestRule.confirmationDialogSelection()
//        composeTestRule.mainClock.advanceTimeBy(DELAY)
//
//        println("items: $items")
//
//        val badge1CountNode2 = composeTestRule.getFieldNodeByContentDescription(card, "Badge1 count")
//        val badge1CountValue2 = composeTestRule.getFieldValue(badge1CountNode)
//
//        badge1CountNode.printToLog("badge1CountNode2: ")
//        println("badge1CountValue: $badge1CountValue2.")
//
//        TestCase.assertEquals(initialValue + COUNT, badge1CountValue2)
//
////        val count = card
//////            .fetchSemanticsNode()
//////            .config
////            .onChildren()
////            .filterToOne(hasContentDescription("Badge1 Count"))
//
////        val countValue = composeTestRule.getFieldValue(count)
////        println("countValue is $count")
////        count.printToLog("BOXES SCREEN")
////        count.printToLog("count")
//    }
//}
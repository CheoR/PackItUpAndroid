package com.example.packitupandroid.ui.common.card

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.parseCurrencyToDouble
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EditCardUiTestsItem {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var cameraButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var dropdownContentDescription: String
    private lateinit var selectedCard: MutableState<Item?>

    val boxDropdownOptions = listOf(
        BoxIdAndName(id = "1", name="box1"),
        BoxIdAndName(id = "2", name="box2"),
        BoxIdAndName(id = "3", name="box3"),
        BoxIdAndName(id = "4", name="box4"),
    )

    val boxDropdownOptionsResult = Result.Success(boxDropdownOptions)

    private var imageUri: String? = null
    val itemName = "cleaning rags"
    val itemDescription = "because too lazy to get new ones"
    val itemValue = 12.50
    val itemIsFragile = false
    val itemCurrentBoxId = "1"
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    val onFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList<EditFields>()
        element.value?.let { currentItem ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentItem.copy(description = value) else currentItem
                EditFields.Dropdown -> if(editableFields.contains(field))  currentItem.copy(boxId = value) else currentItem
                EditFields.ImageUri -> if(editableFields.contains(field))  currentItem.copy(imageUri = value) else currentItem
                EditFields.IsFragile -> if(editableFields.contains(field))  currentItem.copy(isFragile = value.toBoolean()) else currentItem
                EditFields.Name -> if(editableFields.contains(field))  currentItem.copy(name = value) else currentItem
                EditFields.Value -> if(editableFields.contains(field))  currentItem.copy(value = value.parseCurrencyToDouble()) else currentItem
            }
            element.value = updatedElement
        }
    }

    @get:Rule
    val composeTestRule = createComposeRule()


//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val coroutineRule = MainCoroutineRule()
//
//    private fun unregisterComposeEspressoIdlingResource() {
//        IdlingRegistry.getInstance().resources
//            .filter { it.name == "Compose-Espresso link" }
//            .forEach { IdlingRegistry.getInstance().unregister(it) }
//
//    }

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            nameFieldContentDescription = stringResource(R.string.name) + " field"
            descriptionContentDescription = stringResource(R.string.description) + " field"
            valueContentDescription = stringResource(R.string.value)
            isFragileContentDescription = stringResource(R.string.fragile_checkbox)
            imageContentDescription = stringResource(R.string.default_item_badge)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            cameraButtonContentDescription = stringResource(R.string.camera)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            dropdownContentDescription = stringResource(R.string.dropdown_select_box)

            imageUri = null

            selectedCard = remember { mutableStateOf<Item?>(
                Item(
                    id = "1",
                    name = itemName,
                    description = itemDescription,
                    value = itemValue,
                    isFragile = itemIsFragile,
                    boxId = itemCurrentBoxId,
                    imageUri = imageUri,
                )
            ) }

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                EditCard<Item>(
                    selectedCard = selectedCard,
                    editableFields = Item.EDIT_FIELDS,
                    onFieldChange = onFieldChange,
                    iconsContent = {
                        IconBadge(
                            image = defaultItemIcon,
                            badgeContentDescription = imageContentDescription,
                            badgeCount = 0,
                            type = "items",
                        )
                    },
                    modifier = Modifier,
                    dropdownOptions = boxDropdownOptionsResult,
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
//        unregisterComposeEspressoIdlingResource()
        setupComposeSetContent()
    }

    @Test
    fun editCard_nameField_nameFieldExist() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertExists()
    }

    @Test
    fun editCard_nameField_nameFieldIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_nameField_nameFieldHasCorrectText() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertTextEquals(itemName)
    }

    @Test
    fun editCard_updateNameField_nameFieldHasCorrectText() {
        val newName = "oink oink oink oink oink oink oink oink"
        val nameBefore = selectedCard.value!!.name

        val node = composeTestRule.onNodeWithContentDescription(nameFieldContentDescription)

        node.performTextReplacement(newName)
        node.assertTextEquals(newName)
        node.assert(!hasText(nameBefore))
    }

    @Test
    fun editCard_descriptionField_descriptionFieldExist() {
        composeTestRule
            .onNodeWithContentDescription(descriptionContentDescription)
            .assertExists()
    }

    @Test
    fun editCard_descriptionField_descriptionFieldIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(descriptionContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_descriptionField_descriptionFieldHasCorrectText() {
        composeTestRule
            .onNodeWithContentDescription(descriptionContentDescription)
            .assertTextEquals(itemDescription)
    }

    @Test
    fun editCard_updateDescriptionField_descriptionFieldHasCorrectText() {
        val newDescription = "oink oink oink oink oink oink oink oink"
        val descriptionBefore = selectedCard.value!!.description ?: "oink oink"

        val node = composeTestRule.onNodeWithContentDescription(descriptionContentDescription)

        node.performTextReplacement(newDescription)
        node.assertTextEquals(newDescription)
        node.assert(!hasText(descriptionBefore))
    }

    @Test
    fun editCard_valueField_valueFieldExists() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertExists()
    }

    @Test
    fun editCard_valueField_valueFieldIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_valueField_valueFieldHasCorrectValue() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertTextContains(itemValue.toString(), substring = true)
    }

    @Test
    fun editCard_updateValueField_valueFieldHasCorrectValue() {
        val newValue = 123.45
        val valueBefore = selectedCard.value!!.value

        val node = composeTestRule.onNodeWithContentDescription(valueContentDescription)

        node.performTextReplacement(newValue.toString())
        node.assertTextContains(newValue.toString(), substring = true)
        node.assert(!hasText(valueBefore.toString(), substring = true))
    }

    @Test
    fun editCard_isFragileField_isFragileFieldExists() {
        composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)
            .assertExists()

        composeTestRule.onNodeWithContentDescription(isFragileContentDescription).printToLog("MOO")
    }

    @Test
    fun editCard_isFragileField_isFragileFieldIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_isFragileField_isFragileFieldHasCorrectValue() {
        composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)
            .assertTextEquals(itemIsFragile.toString())
            .assertIsDisplayed()
    }

    @Test
    fun editCard_updateIsFragileField_isFragileFieldHasCorrectValue() {
        val valueBefore = selectedCard.value!!.isFragile
        val newValue = !valueBefore

        val node = composeTestRule.onNodeWithContentDescription(isFragileContentDescription, useUnmergedTree = true)
            .onChild()

        node
            .apply { if(valueBefore == false) assertIsOff() else assertIsOn()}

        node.performClick()

        node
            .apply { if(newValue == true) assertIsOn() else assertIsOff()}

    }

    @Test
    fun editCard_swipeToOpenMenu_swipeToOpenMenuDoesNotExists() {
        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .assertDoesNotExist()
    }

    @Test
    fun editCard_dropdown_dropdownDoesNotExists() {
        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .assertExists()
    }

    @Test
    fun editCard_dropdown_dropdownIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_dropdown_dropdownHasCorrectTextSelection() {
        val box = boxDropdownOptions.find { it.id == selectedCard.value!!.boxId }

        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .onChild()
            .assertTextEquals(box?.name ?: "Select Options")
    }

    @Test
    fun editCard_updatedEditableDropdown_selectionIsUpdated() {
//        val beforeBox = boxDropdownOptions.find { it.id == selectedCard.value!!.boxId }
        val newBox = boxDropdownOptions.find { it.id == "2" }

        val node = composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .performClick() // Expands dropdown menu

        composeTestRule
            .onNodeWithText(newBox?.name ?: "Select Options")
            .performClick()

        composeTestRule.waitForIdle()

        node
            .onChild()
            .assertTextEquals(newBox?.name ?: "Select Options")

        assertEquals(selectedCard.value!!.boxId, newBox?.id)
    }
}


class EditCardUiTestsItem2 { // TODO: refactor so 2nd test class not needed just to test when boxId is null
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var cameraButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var dropdownContentDescription: String
    private lateinit var selectedCard: MutableState<Item?>

    private var imageUri: String? = null
    val itemName = "cleaning rags"
    val itemDescription = "because too lazy to get new ones"
    val itemValue = 12.50
    val itemIsFragile = false
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    val onFieldChange = fun (element: MutableState<Item?>, field: EditFields, value: String) {}

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            nameFieldContentDescription = stringResource(R.string.name) + " field"
            descriptionContentDescription = stringResource(R.string.description) + " field"
            valueContentDescription = stringResource(R.string.value)
            isFragileContentDescription = stringResource(R.string.fragile_checkbox)
            imageContentDescription = stringResource(R.string.default_item_badge)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            cameraButtonContentDescription = stringResource(R.string.camera)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            dropdownContentDescription = stringResource(R.string.dropdown_select_box)

            imageUri = null

            selectedCard = remember { mutableStateOf<Item?>(
                Item(
                    id = "1",
                    name = itemName,
                    description = itemDescription,
                    value = itemValue,
                    isFragile = itemIsFragile,
                    boxId = null,
                    imageUri = imageUri,
                )
            ) }

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                EditCard<Item>(
                    selectedCard = selectedCard,
                    editableFields = Item.EDIT_FIELDS,
                    onFieldChange = onFieldChange,
                    iconsContent = {
                        IconBadge(
                            image = defaultItemIcon,
                            badgeContentDescription = imageContentDescription,
                            badgeCount = 0,
                            type = "items",
                        )
                    },
                    modifier = Modifier,
                    dropdownOptions = Result.Success(emptyList()),
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        setupComposeSetContent()
    }

    @Test
    fun editCard_editableDropdownSelectionIsNull_dropdownDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .onChild()
            .assertTextEquals("Select Options")
    }
}
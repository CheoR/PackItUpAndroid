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
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
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
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.source.local.TestDataSource
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


class EditCardUiTestsBox {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var addButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var dropdownContentDescription: String
    private lateinit var badgeCountContentDescription: String
    private lateinit var selectedCard: MutableState<Box?>

    val testDataSource = TestDataSource()
    val collections = testDataSource.collections
    val collectionDropdownOptions = testDataSource.collectionIdAndNames
    val boxes = testDataSource.boxes
    val items = testDataSource.items

    val collectionDropdownOptionsResult = Result.Success(collectionDropdownOptions)
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    val onFieldChange = fun (element: MutableState<Box?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        println("field: $field, value: $value editableFields: $editableFields isEditable: ${editableFields.contains(field)}")
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> if(editableFields.contains(field))  currentBox.copy(collectionId = value) else currentBox
                EditFields.ImageUri -> currentBox // if(editableFields.contains(field))  currentBox.copy(imageUri = value) else currentBox
                EditFields.IsFragile -> if(editableFields.contains(field))  currentBox.copy(isFragile = value.toBoolean()) else currentBox
                EditFields.Name -> if(editableFields.contains(field))  currentBox.copy(name = value) else currentBox
                EditFields.Value -> if(editableFields.contains(field))  currentBox.copy(value = value.parseCurrencyToDouble()) else currentBox
            }
            element.value = updatedElement
        }
    }

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
            addButtonContentDescription = stringResource(R.string.add)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            badgeCountContentDescription = stringResource(
                R.string.badgeContentDescription, "items", 0
            )
            dropdownContentDescription = stringResource(R.string.dropdown_select_box)

            selectedCard = remember { mutableStateOf<Box?>(boxes[0]) }

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                EditCard<Box>(
                    selectedCard = selectedCard,
                    editableFields = Box.EDIT_FIELDS,
                    onFieldChange = onFieldChange,
                    iconsContent = {
                        IconBadge(
                            image = defaultItemIcon,
                            badgeContentDescription = imageContentDescription,
                            badgeCount = selectedCard.value?.itemCount ?: 0,
                            type = "items",
                        )
                    },
                    modifier = Modifier,
                    dropdownOptions = collectionDropdownOptionsResult,
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
            .assertTextEquals(boxes[0].name)
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
            .assertTextEquals(boxes[0].description.toString())
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
            .assertTextContains(selectedCard.value!!.value.toString(), substring = true)
    }

    @Test
    fun editCard_updateValueField_isNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertIsNotEnabled()
    }

    @Test
    fun editCard_disabledValueField_updateAttemptFails_valueFieldRemainsUnchanged() {
        val newValue = 123.45
        val valueBefore = selectedCard.value!!.value

        val node = composeTestRule.onNodeWithContentDescription(valueContentDescription)

        try {
            node.performTextReplacement(newValue.toString())
            assert(false) { "performTextReplacement should failed because the field is disabled." }
        } catch (e: AssertionError) {
           println("AssertionError caught: ${e.message}")
        }

        node.assertTextContains(valueBefore.toString(), substring = true)
    }

    // TODO: investigate why can't import assertFailedWith even after adding
    //     testImplementation(kotlin("test")) to gradle
//    @Test
//    fun editCard_disabledValueField_updateAttemptThrowsException() {
//        val newValue = 123.45
//        val valueBefore = selectedCard.value!!.value
//
//        val node = composeTestRule.onNodeWithContentDescription(valueContentDescription)
//
//        assertFailsWith<IllegalStateException> {
//            node.performTextReplacement(newValue.toString())
//        }
//
//        // should never read here
//        node.assertTextContains(valueBefore.toString(), substring = true)
//    }

    @Test
    fun editCard_isFragileField_isFragileFieldExists() {
        composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)
            .assertExists()
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
            .assertIsNotEnabled()
    }

    @Test
    fun editCard_disabledIsFragileField_updateAttemptFails_isFragileFieldRemainsUnchanged() {
        val valueBefore = selectedCard.value!!.isFragile

        val node = composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)

        try {
            node.performClick()
            assert(false) { "performClick should failed because the field is disabled." }
        } catch (e: AssertionError) {
            println("AssertionError caught: ${e.message}")
        }

        val newValue = selectedCard.value!!.isFragile
        assert(newValue == valueBefore)

        node
            .onChild()
            .apply { if(valueBefore == false) assertIsOff() else assertIsOn()}
    }

    @Test
    fun editCard_swipeToOpenMenu_swipeToOpenMenuDoesNotExists() {
        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .assertDoesNotExist()
    }

    @Test
    fun editCard_dropdown_dropdownExists() {
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
        val collection = collectionDropdownOptions.find { it.id == selectedCard.value!!.collectionId }

        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .onChild()
            .assertTextEquals(collection?.name ?: "Select Options")
    }

    @Test
    fun editCard_updatedEditableDropdown_selectionIsUpdated() {
//        val beforeBox = collectionDropdownOptions.find { it.id == selectedCard.value!!.collectionId }
        val newCollection = collectionDropdownOptions.find { it.id == collections[1].id }

        val node = composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .performClick() // Expands dropdown menu

        composeTestRule
            .onNodeWithText(newCollection?.name ?: "Select Options")
            .performClick()

        composeTestRule.waitForIdle()

        node
            .onChild()
            .assertTextEquals(newCollection?.name ?: "Select Options")

        assertEquals(selectedCard.value!!.collectionId, newCollection?.id)
    }

    @Test
    fun editCard_badgeCountGreaterThanZero_badgeCountExists() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertExists()
    }

    @Test
    fun editCard_badgeCountGreaterThanZero_badgeCountIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_badgeCountGreaterThanZero_badgeCountHasCorrectValue() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertTextEquals(selectedCard.value?.itemCount.toString())
    }
}


class EditCardUiTestsBox2 { // TODO: refactor so 2nd test class not needed just to test when collectionId is null
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var addButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var dropdownContentDescription: String
    private lateinit var badgeCountContentDescription: String
    private lateinit var selectedCard: MutableState<Box?>

    val testDataSource = TestDataSource()
    val collections = testDataSource.collections
    val collectionDropdownOptions = testDataSource.collectionIdAndNames
    val boxes = testDataSource.boxes
    val items = testDataSource.items

    val collectionDropdownOptionsResult = Result.Success(collectionDropdownOptions)
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    val onFieldChange = fun (element: MutableState<Box?>, field: EditFields, value: String) {}

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
            addButtonContentDescription = stringResource(R.string.add)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            badgeCountContentDescription = stringResource(
                R.string.badgeContentDescription, "items", 0
            )
            dropdownContentDescription = stringResource(R.string.dropdown_select_box)

            selectedCard = remember { mutableStateOf<Box?>(
                Box(
                    id = boxes[0].id,
                    name = boxes[0].name,
                    description = boxes[0].description,
                    value = 0.00,
                    isFragile = false,
                    collectionId = null,
                    itemCount = 0,
                )
            ) }

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                EditCard<Box>(
                    selectedCard = selectedCard,
                    editableFields = Box.EDIT_FIELDS,
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
                    dropdownOptions = collectionDropdownOptionsResult,
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


    @Test
    fun editCard_badgeCountIsnZero_badgeCountDoesNotExists() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun editCard_badgeCountIsnZero_badgeCountIsNotDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertIsNotDisplayed()
    }
}

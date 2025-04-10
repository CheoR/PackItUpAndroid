package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.parseCurrencyToDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EditCardUiTestsCollection {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var boxImageContentDescription: String
    private lateinit var itemImageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var addButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var dropdownContentDescription: String
    private lateinit var boxBadgeCountContentDescription: String
    private lateinit var itemBadgeCountContentDescription: String
    private lateinit var selectedCard: MutableState<Collection?>

    val testDataSource = TestDataSource()
    val collections = testDataSource.collections
    val boxes = testDataSource.boxes
    val items = testDataSource.items

    val defaultBoxIcon =ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    private val onFieldChange = fun (element: MutableState<Collection?>, field: EditFields, value: String) {
        val editableFields = element.value?.editFields ?: emptyList()
        element.value?.let { currentBox ->
            val updatedElement = when(field) {
                EditFields.Description -> if(editableFields.contains(field)) currentBox.copy(description = value) else currentBox
                EditFields.Dropdown -> currentBox //  if(editableFields.contains(field))  currentBox.copy(dropdown = value) else currentBox
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
            boxImageContentDescription = stringResource(R.string.default_box_badge)
            itemImageContentDescription = stringResource(R.string.default_item_badge)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            addButtonContentDescription = stringResource(R.string.add)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            boxBadgeCountContentDescription = stringResource(
                R.string.badgeContentDescription, "boxes", 0
            )
            itemBadgeCountContentDescription = stringResource(
                R.string.badgeContentDescription, "items", 0
            )
            dropdownContentDescription = stringResource(R.string.dropdown_select_box)

            selectedCard = remember { mutableStateOf<Collection?>(collections[0]) }

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                EditCard<Collection>(
                    selectedCard = selectedCard,
                    editableFields = Collection.EDIT_FIELDS,
                    onFieldChange = onFieldChange,
                    iconsContent = {
                        Column {
                            IconBadge(
                                image = defaultBoxIcon,
                                badgeContentDescription = boxImageContentDescription,
                                badgeCount = selectedCard.value?.boxCount ?: 0,
                                type = "boxes",
                            )
                            IconBadge(
                                image = defaultItemIcon,
                                badgeContentDescription = itemImageContentDescription,
                                badgeCount = selectedCard.value?.itemCount ?: 0,
                                type = "items",
                            )
                        }
                    },
                    modifier = Modifier,
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
            .assertTextEquals(collections[0].name)
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
            .assertTextEquals(collections[0].description.toString())
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
    fun editCard_dropdown_dropdownDoesNotExist() {
        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .assertDoesNotExist()
    }

    @Test
    fun editCard_dropdown_dropdownIsNotDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(dropdownContentDescription)
            .assertIsNotDisplayed()
    }

    @Test
    fun editCard_boxBadgeCountGreaterThanZero_boxBadgeCountExists() {
        composeTestRule
            .onNodeWithContentDescription("number of boxes", substring = true, ignoreCase = true)
            .assertExists()
    }

    @Test
    fun editCard_boxBadgeCountGreaterThanZero_boxBadgeCountIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("number of boxes", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_boxBadgeCountGreaterThanZero_boxBadgeCountHasCorrectValue() {
        composeTestRule
            .onNodeWithContentDescription("number of boxes", substring = true, ignoreCase = true)
            .assertTextEquals(selectedCard.value?.boxCount.toString())
    }

    @Test
    fun editCard_itemBadgeCountGreaterThanZero_itemBadgeCountExists() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertExists()
    }

    @Test
    fun editCard_itemBadgeCountGreaterThanZero_itemBadgeCountIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun editCard_itemBadgeCountGreaterThanZero_itemBadgeCountHasCorrectValue() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertTextEquals(selectedCard.value?.itemCount.toString())
    }
}


class EditCardUiTestsCollection2 {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var boxImageContentDescription: String
    private lateinit var itemImageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var addButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var boxBadgeCountContentDescription: String
    private lateinit var itemBadgeCountContentDescription: String
    private lateinit var selectedCard: MutableState<Collection?>

    val testDataSource = TestDataSource()
    val collections = testDataSource.collections
    val boxes = testDataSource.boxes
    val items = testDataSource.items

    val defaultBoxIcon =ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    private val onFieldChange = fun (element: MutableState<Collection?>, field: EditFields, value: String) {}

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            nameFieldContentDescription = stringResource(R.string.name) + " field"
            descriptionContentDescription = stringResource(R.string.description) + " field"
            valueContentDescription = stringResource(R.string.value)
            isFragileContentDescription = stringResource(R.string.fragile_checkbox)
            boxImageContentDescription = stringResource(R.string.default_box_badge)
            itemImageContentDescription = stringResource(R.string.default_item_badge)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            addButtonContentDescription = stringResource(R.string.add)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            boxBadgeCountContentDescription = stringResource(
                R.string.badgeContentDescription, "boxes", 0
            )
            itemBadgeCountContentDescription = stringResource(
                R.string.badgeContentDescription, "items", 0
            )

            selectedCard = remember { mutableStateOf<Collection?>(
                Collection(
                    id = collections[0].id,
                    name = collections[0].name,
                    description = collections[0].description,
                    value = 0.00,
                    isFragile = false,
                    boxCount = 0,
                    itemCount = 0,
                )
            ) }

            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                EditCard<Collection>(
                    selectedCard = selectedCard,
                    editableFields = Collection.EDIT_FIELDS,
                    onFieldChange = onFieldChange,
                    iconsContent = {
                        Column {
                            IconBadge(
                                image = defaultBoxIcon,
                                badgeContentDescription = boxImageContentDescription,
                                badgeCount = selectedCard.value?.boxCount ?: 0,
                                type = "boxes",
                            )
                            IconBadge(
                                image = defaultItemIcon,
                                badgeContentDescription = itemImageContentDescription,
                                badgeCount = selectedCard.value?.itemCount ?: 0,
                                type = "items",
                            )
                        }
                    },
                    modifier = Modifier,
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
    fun editCard_boxBadgeCountIsnZero_boxBadgeCountDoesNotExists() {
        composeTestRule
            .onNodeWithContentDescription("number of boxes", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun editCard_boxBadgeCountIsnZero_boxBadgeCountIsNotDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("number of boxes", substring = true, ignoreCase = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun editCard_itemBadgeCountIsnZero_itemBadgeCountDoesNotExists() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun editCard_itemBadgeCountIsnZero_itemBadgeCountIsNotDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("number of items", substring = true, ignoreCase = true)
            .assertIsNotDisplayed()
    }
}

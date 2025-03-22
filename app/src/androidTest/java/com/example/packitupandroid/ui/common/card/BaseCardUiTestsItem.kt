package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.espresso.IdlingRegistry
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


// TODO: followup on using JUNIT5 instead for paramerteize tests with ITEM, BOX, COLLECTION
// AND REFACTOR
class BaseCardUiTestsItem {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var cameraButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String

    val boxDropdownOptions = listOf(
        BoxIdAndName(id = "1", name="box1"),
        BoxIdAndName(id = "2", name="box1"),
        BoxIdAndName(id = "3", name="box1"),
        BoxIdAndName(id = "4", name="box1"),
    )

    val boxDropdownOptionsResult = Result.Success(boxDropdownOptions)

    private var imageUri: String? = null
    val itemName = "cleaning rags"
    val itemDescription = ""
    val itemValue = 12.50
    val itemIsFragile = true
    val itemCurrentBoxId = "1"
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    val item = Item(
        id = "1",
        name = itemName,
        description = itemDescription,
        value = itemValue,
        isFragile = itemIsFragile,
        boxId = itemCurrentBoxId,
        imageUri = imageUri,
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private fun unregisterComposeEspressoIdlingResource() {
        IdlingRegistry.getInstance().resources
            .filter { it.name == "Compose-Espresso link" }
            .forEach { IdlingRegistry.getInstance().unregister(it) }

    }

    private fun setupComposeSetContent() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)
            nameFieldContentDescription = stringResource(R.string.name) + " field"
            descriptionContentDescription = stringResource(R.string.description) + " field"
            valueContentDescription = stringResource(R.string.value) + " field"
            isFragileContentDescription = stringResource(R.string.fragile_checkbox)
            imageContentDescription = stringResource(R.string.default_item_badge)
            swipeToOpenMenuContentDescription = stringResource(R.string.open_menu)
            cameraButtonContentDescription = stringResource(R.string.camera)
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            imageUri = null

            PackItUpAndroidTheme(themeManager) {
                BaseCard<Item>(
                    data = item,
                    iconsContent = {
                        Column {
                            IconBadge(
                                image = defaultItemIcon,
                                badgeContentDescription = imageContentDescription,
                                badgeCount = 0,
                                type = "items",
                            )
                        }
                    },
                    onCamera = { },
                    onDelete = { },
                    onUpdate = { },
                    modifier = Modifier,
                    dropdownOptions =  boxDropdownOptionsResult,
                    onAdd = { },
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        unregisterComposeEspressoIdlingResource()
        setupComposeSetContent()
    }

    @Test
    fun test_BaseCard_init_badgeColumnExist() {
        composeTestRule
            .onNodeWithContentDescription(imageContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_badgeColumnIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(imageContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_nameFieldExist() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_nameIsDisplayed() {
        composeTestRule
            .onNodeWithText(itemName)
            .assertIsDisplayed()
    }

    @Test fun test_BaseCard_init_nameFieldIsNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertIsNotEnabled()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldExist() {
        val selectedName = boxDropdownOptionsResult.data.find { it.id == itemCurrentBoxId }?.name ?: ""
        composeTestRule
            .onNodeWithContentDescription("$selectedName selected")
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldIsDisplayed() {
        val selectedName = boxDropdownOptionsResult.data.find { it.id == itemCurrentBoxId }?.name ?: ""
        composeTestRule
            .onNodeWithText(selectedName)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldIsNotEnabled() {
        val selectedName = boxDropdownOptionsResult.data.find { it.id == itemCurrentBoxId }?.name ?: ""
        composeTestRule
            .onNodeWithContentDescription("$selectedName selected")
            .assertIsNotEnabled()
    }

    @Test
    fun test_BaseCard_init_descriptionFieldExist() {
        composeTestRule
            .onNodeWithContentDescription(descriptionContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_descriptionIsDisplayed() {
        composeTestRule
            .onNodeWithText(itemDescription)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_descriptionFieldIsNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(descriptionContentDescription)
            .assertIsNotEnabled()
    }


    @Test
    fun test_BaseCard_init_fragileCheckboxFieldExist() {
        composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_fragileCheckboxIsChecked() {
        composeTestRule
            .onNodeWithContentDescription(isFragileContentDescription)
            .assertIsOn()
    }

    @Test
    fun test_BaseCard_init_valueFieldExist() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_valueIsDisplayed() {
        composeTestRule
            .onNode(hasText("$itemValue", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_valueFieldIsNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertIsNotEnabled()
    }

    @Test
    fun test_BaseCard_Item_swipeLeft_iconDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .performTouchInput {
                swipeLeft()
            }

        composeTestRule
            .onNodeWithContentDescription(cameraButtonContentDescription)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(deleteButtonContentDescription)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)
    }
}

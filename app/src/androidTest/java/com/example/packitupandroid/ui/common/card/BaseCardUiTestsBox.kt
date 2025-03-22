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
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
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


class BaseCardUiTestsBox {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var addButtonContentDescription: String
    private lateinit var itemBadgeCountContentDescription: String

    val collectionDropdownOptions = listOf(
        CollectionIdAndName(id = "1", name = "collection1"),
        CollectionIdAndName(id = "2", name = "collection1"),
        CollectionIdAndName(id = "3", name = "collection1"),
        CollectionIdAndName(id = "4", name = "collection1"),
    )

    val collectionDropdownOptionsResult = Result.Success(collectionDropdownOptions)

    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)

    val item = Item(
        id = "1",
        name = "itemName",
        description = "itemDescription",
        value = 12.50,
        isFragile = true,
        boxId = "1",
    )

    val item2 = Item(
        id = "2",
        name = "junk",
        description = "donate maybe",
        value = 12.50,
        isFragile = false,
        boxId = "1",
    )

    val boxName = "for home"
    val boxDescription = "do not throw box away"
    val boxIsFragile = listOf(item, item2).any { it.isFragile }
    val boxValue = listOf(item, item2).sumOf { it.value }
    val boxCurrentCollectionId = "1"
    val itemCount = listOf(item, item2).size

    val box = Box(
        id = "1",
        name = boxName,
        description = boxDescription,
        isFragile = boxIsFragile,
        value = boxValue,
        collectionId = boxCurrentCollectionId,
        itemCount = itemCount,
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
            deleteButtonContentDescription = stringResource(R.string.delete)
            editButtonContentDescription = stringResource(R.string.edit)
            addButtonContentDescription = stringResource(R.string.add)
            itemBadgeCountContentDescription = stringResource(R.string.badgeContentDescription, "items", box.itemCount)

            PackItUpAndroidTheme(themeManager) {
                BaseCard<Box>(
                    data = box,
                    iconsContent = {
                        Column {
                            IconBadge(
                                image = defaultItemIcon,
                                badgeContentDescription = imageContentDescription,
                                badgeCount = box.itemCount,
                                type = "items",
                            )
                        }
                    },
                    onCamera = { },
                    onDelete = { },
                    onUpdate = { },
                    modifier = Modifier,
                    dropdownOptions = collectionDropdownOptionsResult,
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
            .onNodeWithContentDescription(itemBadgeCountContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_badgeColumnIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(itemBadgeCountContentDescription)
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
            .onNodeWithText(boxName)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_nameFieldIsNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertIsNotEnabled()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldExist() {
        val selectedName =
            collectionDropdownOptionsResult.data.find { it.id == boxCurrentCollectionId }?.name ?: ""
        composeTestRule
            .onNodeWithContentDescription("$selectedName selected")
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldIsDisplayed() {
        val selectedName =
            collectionDropdownOptionsResult.data.find { it.id == boxCurrentCollectionId }?.name ?: ""
        composeTestRule
            .onNodeWithText(selectedName)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldIsNotEnabled() {
        val selectedName =
            collectionDropdownOptionsResult.data.find { it.id == boxCurrentCollectionId }?.name ?: ""
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
            .onNodeWithText(boxDescription)
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
            .onNode(hasText("$", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_valueFieldIsNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(valueContentDescription)
            .assertIsNotEnabled()
    }

    @Test
    fun test_BaseCard_Box_swipeLeft_iconDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(swipeToOpenMenuContentDescription)
            .performTouchInput {
                swipeLeft()
            }
        composeTestRule
            .onNodeWithContentDescription(addButtonContentDescription)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(deleteButtonContentDescription)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)
    }
}

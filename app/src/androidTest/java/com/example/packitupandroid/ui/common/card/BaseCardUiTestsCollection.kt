package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
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
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class BaseCardUiTestsCollection {
    private lateinit var nameFieldContentDescription: String
    private lateinit var descriptionContentDescription: String
    private lateinit var valueContentDescription: String
    private lateinit var isFragileContentDescription: String
    private lateinit var imageContentDescription: String
    private lateinit var swipeToOpenMenuContentDescription: String
    private lateinit var cameraButtonContentDescription: String
    private lateinit var deleteButtonContentDescription: String
    private lateinit var editButtonContentDescription: String
    private lateinit var addButtonContentDescription: String
    private lateinit var itemBadgeCountContentDescription: String
    private lateinit var boxBadgeCountContentDescription: String

    private var imageUri: String? = null
    val itemName = "cleaning rags"
    val itemDescription = ""
    val itemValue = 12.50
    val itemIsFragile = true
    val itemCurrentBoxId = "1"
    val defaultItemIcon = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
    val defaultBoxIcon = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)

    val item = Item(
        id = "1",
        name = itemName,
        description = itemDescription,
        value = itemValue,
        isFragile = itemIsFragile,
        boxId = itemCurrentBoxId,
        imageUri = imageUri,
    )

    val item2 = Item(
        id = "2",
        name = "junk",
        description = "donate maybe",
        value = itemValue,
        isFragile = false,
        boxId = itemCurrentBoxId,
        imageUri = null,
    )

    val item3 = Item(
        id = "3",
        name = "stuff",
        description = "donate maybe",
        value = itemValue,
        isFragile = false,
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

    val box2 = Box(
        id = "2",
        name = "toss box",
        description = "nothing worth keeping",
        isFragile = listOf(item3).any { it.isFragile },
        value = listOf(item3).sumOf { it.value },
        collectionId = boxCurrentCollectionId,
        itemCount = listOf(item3).size,
    )

    val collectionName = "home stuff"
    val collectionDescription = " needs sorting"
    val collectionIsFragile = listOf(box, box2).any { it.isFragile }
    val collectionValue = listOf(box, box2).sumOf { it.value }
    val collectionItemCount = listOf(box, box2).sumOf { it.itemCount }
    val collectionBoxCount = listOf(box, box2).size

    val collection = Collection(
        id = "1",
        name = collectionName,
        description = collectionDescription,
        isFragile = collectionIsFragile,
        value = collectionValue,
        itemCount = collectionItemCount,
        boxCount = collectionBoxCount,
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
            addButtonContentDescription = stringResource(R.string.add)
            itemBadgeCountContentDescription = stringResource(R.string.badgeContentDescription, "items", collection.itemCount)
            boxBadgeCountContentDescription = stringResource(R.string.badgeContentDescription, "boxes", collection.boxCount)

            PackItUpAndroidTheme(themeManager) {
                BaseCard<Collection>(
                    data = collection,
                    iconsContent = {
                        Column {
                            IconBadge(
                                image = defaultBoxIcon,
                                badgeContentDescription = imageContentDescription,
                                badgeCount = collection.boxCount,
                                type = "boxes"
                            )
                            IconBadge(
                                image = defaultItemIcon,
                                badgeContentDescription = imageContentDescription,
                                badgeCount = collection.itemCount,
                                type = "items",
                            )
                        }
                    },
                    onCamera = { },
                    onDelete = { },
                    onUpdate = { },
                    modifier = Modifier,
                    dropdownOptions = null,
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
    fun test_BaseCard_init_boxBadgeColumnExist() {
        composeTestRule
            .onNodeWithContentDescription(boxBadgeCountContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_boxBadgeColumnIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(boxBadgeCountContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_itemBadgeColumnExist() {
        composeTestRule
            .onNodeWithContentDescription(itemBadgeCountContentDescription)
            .assertExists()
    }

    @Test
    fun test_BaseCard_init_itemBadgeColumnIsDisplayed() {
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
            .onNodeWithText(collectionName)
            .assertIsDisplayed()
    }

    @Test
    fun test_BaseCard_init_nameFieldIsNotEnabled() {
        composeTestRule
            .onNodeWithContentDescription(nameFieldContentDescription)
            .assertIsNotEnabled()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldDoesNotExist() {
        composeTestRule
            .onNodeWithContentDescription("selected", substring = true)
            .assertDoesNotExist()
    }

    @Test
    fun test_BaseCard_init_dropdownSelectionFieldIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("selected", substring = true)
            .assertIsNotDisplayed()
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
            .onNodeWithText(collectionDescription)
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
            .onNode(hasText("$collectionValue", substring = true))
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

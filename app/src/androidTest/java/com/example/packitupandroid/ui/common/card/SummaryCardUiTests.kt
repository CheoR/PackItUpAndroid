package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.ActionColumnIcon
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters


typealias ParameterizedCollection = Collection<Array<Any>>

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class SummaryCardUiTests(
    private val cardNameResId: Int,
    private val badgeContentDescriptionResId: Int,
    private val iconImage: ImageContent,
    private val badgeCount: Int,
) {

    private lateinit var badgeContentDescription: String
    private lateinit var cardName: String
    private lateinit var cardDescription: String
    private lateinit var icon: ImageContent

    companion object {
        @JvmStatic
        @Parameters(name = "{0} - name: {1}, description: {2}, count: {4}")
        fun data(): ParameterizedCollection {
            return listOf(
                arrayOf(
                    R.string.collections,
                    R.string.collections,
                    ImageContent.VectorImage(Icons.Default.Category),
                    1,
                ),
                arrayOf(
                    R.string.boxes,
                    R.string.boxes,
                    ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
                    2
                ),
                arrayOf(
                    R.string.items,
                    R.string.items,
                    ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                    3
                )
            )
        }
    }

    val state: Result<Summary?> = Result.Success(
        Summary(
            collectionCount = 1,
            boxCount = 2,
            itemCount = 3,
            value = 123.45,
            isFragile = true,
        )
    )

    val result = when (state) {
        is Result.Error -> Summary(0, 0, 0, 0.0, false)
        is Result.Loading -> Summary(0, 0, 0, 0.0, false)
        is Result.Success -> state.data
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupComposeSetContent() {
        val actionIcon: ActionColumnIcon = ActionColumnIcon.RightArrow

        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            badgeContentDescription = stringResource(
                R.string.badgeContentDescription,
                stringResource(badgeContentDescriptionResId),
                badgeCount
            )

            cardName = stringResource(cardNameResId)
            cardDescription = stringResource(cardNameResId)
            icon = iconImage

            PackItUpAndroidTheme(themeManager) {
                SummaryCard(
                    name = cardNameResId,
                    description = cardDescription,
                    actionIcon = actionIcon,
                    canNavigateToScreen = true,
                    iconsContent = {
                        Column {
                            IconBadge(
                                image = iconImage,
                                badgeContentDescription = badgeContentDescription,
                                badgeCount = badgeCount,
                                type = stringResource(badgeContentDescriptionResId),
                            )
                        }
                    },
                    navigateToTopLevelDestination = {},
                )
            }
        }
    }

    @Before
    fun setup() {
        setupComposeSetContent()
    }

    @Test
    fun test_SummaryCard_init_badgeCountExists() {
        composeTestRule
            .onNodeWithContentDescription(badgeContentDescription)
            .assertExists()
    }

    @Test
    fun test_SummaryCard_init_badgeCountIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(badgeContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun test_SummaryCard_init_badgeCountIsCorrect() {
        composeTestRule
            .onNodeWithContentDescription(badgeContentDescription)
            .assertTextEquals(badgeCount.toString())
    }

    @Test
    fun test_IconBadge_iconIsDisplayed() {
        // TODO: review, it is working, but should ther ereally be a test ?
        when (iconImage) {
            is ImageContent.VectorImage -> {
                composeTestRule
                    .onNode(hasContentDescription(badgeContentDescription))
                    .assertIsDisplayed()
            }
            is ImageContent.DrawableImage -> {
                composeTestRule
                    .onNode(hasContentDescription(badgeContentDescription))
                    .assertIsDisplayed()
            }

            is ImageContent.BitmapImage -> {
                composeTestRule
                    .onNode(hasContentDescription(badgeContentDescription))
                    .assertIsDisplayed()
            }
            is ImageContent.BitmapStringImage -> {
                composeTestRule
                    .onNode(hasContentDescription(badgeContentDescription))
                    .assertIsDisplayed()
            }

            is ImageContent.FileImage -> {
                composeTestRule
                    .onNode(hasContentDescription(badgeContentDescription))
                    .assertIsDisplayed()
            }
        }
    }

    @Test
    fun test_SummaryCard_init_nameFieldExists() {
        composeTestRule
            .onNodeWithContentDescription("$cardNameResId name field")
            .assertExists()
    }

    @Test
    fun test_SummaryCard_init_nameFieldIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("$cardNameResId name field")
            .assertIsDisplayed()
            .assertTextEquals(cardName)
    }

    @Test
    fun test_SummaryCard_init_descriptionFieldExists() {
        composeTestRule
            .onNodeWithContentDescription("$cardNameResId description field")
            .assertIsDisplayed()
            .assertTextEquals(cardDescription)
    }
}

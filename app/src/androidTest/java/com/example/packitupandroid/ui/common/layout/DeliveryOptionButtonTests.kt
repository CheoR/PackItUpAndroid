package com.example.packitupandroid.ui.common.layout

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.R
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DeliveryOptionButtonTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var icon: ImageVector
    private lateinit var contentDescription: String
    private lateinit var buttonText: String
    private var wasClicked: Boolean = false

    @Before
    fun setup() {
        composeTestRule.setContent {
            contentDescription = "Shipping"
            buttonText = "Shipping"
            wasClicked = false
            icon = ImageVector.vectorResource(id = R.drawable.baseline_category_24)

            DeliveryOptionButton(
                icon = icon,
                text = contentDescription,
                onClick = { wasClicked = true }
            )
        }
    }

    @Test
    fun deliveryOptionButton_displayIcon_iconIsDisplayedWithCorrectContentDescription() {
        composeTestRule.onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun deliveryOptionButton_displayIconText_iconTextIsDisplayed() {
        composeTestRule.onNodeWithText(buttonText)
            .assertIsDisplayed()
    }

    @Test
    fun deliveryOptionButton_clickIcon_onClickIsTriggered() {
        composeTestRule
            .onNodeWithContentDescription(contentDescription)
            .performClick()

        assertTrue("Button click was not triggered", wasClicked)
    }
}

package com.example.packitupandroid

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import org.junit.Rule
import org.junit.Test

class CounterTest {
    private val onCreate: (Int) -> Unit = { _ -> }
    private val initialValue = 0
    private val incrementValueByFive = 5
    private val decrementValueByFour = 4

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun counter_InitialState_InitialValueIsZero() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals((initialValue).toString())
    }

    @Test
    fun incrementCounterFive() {

        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        repeat(incrementValueByFive) {
            composeTestRule.onNodeWithContentDescription("increment").performClick()
        }

        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals((incrementValueByFive).toString())
    }

    @Test
    fun incrementAndDecrementCounter_ResultIsOne() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        repeat(incrementValueByFive) {
            composeTestRule.onNodeWithContentDescription("increment").performClick()
        }

        repeat(decrementValueByFour) {
            composeTestRule.onNodeWithContentDescription("decrement").performClick()
        }

        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals((incrementValueByFive - decrementValueByFour).toString())
    }

    @Test
    fun decrementCounterByFour_ResultIsZero() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        repeat(decrementValueByFour) {
            composeTestRule.onNodeWithContentDescription("decrement").performClick()
        }

        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals(initialValue.toString())
    }

    @Test
    fun decrementButton_DisableWhenCounterIsNonPositive() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        repeat(decrementValueByFour) {
            composeTestRule.onNodeWithContentDescription("decrement").performClick()
        }

        composeTestRule.onNodeWithContentDescription("add").assertIsNotEnabled()
    }

    @Test
    fun incrementButton_EnableWhenCounterIsPositive() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        repeat(incrementValueByFive) {
            composeTestRule.onNodeWithContentDescription("increment").performClick()
        }

        composeTestRule.onNodeWithContentDescription("add").assertIsEnabled()
    }

    @Test
    fun addButtonClick_ResetsValueToZero() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }

        repeat(incrementValueByFive) {
            composeTestRule.onNodeWithContentDescription("increment").performClick()
        }

        composeTestRule.onNodeWithContentDescription("add").performClick()

        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals(initialValue.toString())
    }
}
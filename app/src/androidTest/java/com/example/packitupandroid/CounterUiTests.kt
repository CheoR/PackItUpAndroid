package com.example.packitupandroid

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.ui.common.component.Counter
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CounterTest {
    private val onCreate: (Int) -> Unit = { _ -> }
    private val initialValue = 0
    private val incrementValueByFive = 5
    private val decrementValueByFour = 4

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun incrementCounter(times: Int) {
        repeat(times) {
            composeTestRule.onNodeWithContentDescription("increment").performClick()
        }
    }

    private fun decrementCounter(times: Int) {
        repeat(times) {
            composeTestRule.onNodeWithContentDescription("decrement").performClick()
        }
    }

    private fun assertCounterValue(expectedValue: Int) {
        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals(expectedValue.toString())
    }

    @Before
    fun setUp() {
        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
            }
        }
    }

    @Test
    fun counter_InitialState_InitialValueIsZero() {
        assertCounterValue(initialValue)
    }

    @Test
    fun incrementCounterFive() {
        incrementCounter(incrementValueByFive)
        assertCounterValue(incrementValueByFive)
    }

    @Test
    fun incrementAndDecrementCounter_ResultIsOne() {
        incrementCounter(incrementValueByFive)
        decrementCounter(decrementValueByFour)
        assertCounterValue(incrementValueByFive - decrementValueByFour)
    }

    @Test
    fun decrementCounterByFour_ResultIsZero() {
        decrementCounter(decrementValueByFour)
        assertCounterValue(initialValue)
    }

    @Test
    fun decrementButton_DisableWhenCounterIsNonPositive() {
        decrementCounter(decrementValueByFour)
        composeTestRule.onNodeWithContentDescription("add").assertIsNotEnabled()
    }

    @Test
    fun incrementButton_EnableWhenCounterIsPositive() {
        incrementCounter(incrementValueByFive)
        composeTestRule.onNodeWithContentDescription("add").assertIsEnabled()
    }

    @Test
    fun addButtonClick_ResetsValueToZero() {
        incrementCounter(incrementValueByFive)
        composeTestRule.onNodeWithContentDescription("add").performClick()
        assertCounterValue(initialValue)
    }
}
package com.example.packitupandroid.ui.common.component

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.decrementCountByFour
import com.example.packitupandroid.utils.decrementCounter
import com.example.packitupandroid.utils.incrementCountByFive
import com.example.packitupandroid.utils.incrementCounter
import com.example.packitupandroid.utils.initialValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CounterTest {
    private val onCreate: (Int) -> Unit = { _ -> }

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun assertCounterValue(expectedValue: Int) {
        composeTestRule.onNodeWithContentDescription(
            label = "Counter Value $expectedValue",
            ignoreCase = true,
        )
            .assertTextEquals(expectedValue.toString())
    }

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
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
        composeTestRule.incrementCounter(incrementCountByFive)
        assertCounterValue(incrementCountByFive)
    }

    @Test
    fun incrementAndDecrementCounter_ResultIsOne() {
        composeTestRule.incrementCounter(incrementCountByFive)
        composeTestRule.decrementCounter(decrementCountByFour)
        assertCounterValue(incrementCountByFive - decrementCountByFour)
    }

    @Test
    fun decrementCounterByFour_ResultIsZero() {
        composeTestRule.decrementCounter(decrementCountByFour)
        assertCounterValue(initialValue)
    }

    @Test
    fun decrementButton_DisableWhenCounterIsNonPositive() {
        composeTestRule.decrementCounter(decrementCountByFour)
        composeTestRule.onNodeWithContentDescription("add").assertIsNotEnabled()
    }

    @Test
    fun incrementButton_EnableWhenCounterIsPositive() {
        composeTestRule.incrementCounter(incrementCountByFive)
        composeTestRule.onNodeWithContentDescription("add").assertIsEnabled()
    }

    @Test
    fun addButtonClick_ResetsValueToZero() {
        composeTestRule.incrementCounter(incrementCountByFive)
        composeTestRule.onNodeWithContentDescription("add").performClick()
        assertCounterValue(initialValue)
    }
}

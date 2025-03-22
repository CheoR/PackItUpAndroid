package com.example.packitupandroid.ui.common.component

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import com.example.packitupandroid.utils.assertCounterValue
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
        composeTestRule.assertCounterValue(initialValue)
    }

    @Test
    fun incrementCounterFive() {
        composeTestRule.incrementCounter(incrementCountByFive)
        composeTestRule.assertCounterValue(incrementCountByFive)
    }

    @Test
    fun incrementAndDecrementCounter_ResultIsOne() {
        composeTestRule.incrementCounter(incrementCountByFive)
        composeTestRule.decrementCounter(decrementCountByFour)
        composeTestRule.assertCounterValue(incrementCountByFive - decrementCountByFour)
    }

    @Test
    fun decrementCounterByFour_ResultIsZero() {
        composeTestRule.decrementCounter(decrementCountByFour)
        composeTestRule.assertCounterValue(initialValue)
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
        composeTestRule.assertCounterValue(initialValue)
    }
}

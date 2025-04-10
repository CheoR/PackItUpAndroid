package com.example.packitupandroid.ui.common.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SpinnerUiTests {
    lateinit var spinner_content_description: String

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            spinner_content_description = stringResource(R.string.spinner_content_description)
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
                Spinner(
                    modifier = Modifier,
                )
            }
        }

    }

    @Test
    fun spinner_init_spinnerExists() {
        composeTestRule
            .onNodeWithContentDescription(spinner_content_description)
            .assertExists()
    }

    @Test
    fun spinner_init_spinnerIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(spinner_content_description)
            .assertIsDisplayed()
    }

    @Test
    fun spinner_spinnerRotationAnimation() {
//        composeTestRule.setContent { // TODO: look into how to test animation, without needing second class with different duration or avoid twice setcontent error
//            Spinner(animationDuration = 4000) // Ensure duration matches test timing
//        }

        val node = composeTestRule
            .onNodeWithContentDescription(spinner_content_description)

        // Stop auto advancing to control timing manually
        composeTestRule.mainClock.autoAdvance = false

        // TODO: look into how to test animation, for funsies
        // TODO: BY test, i mean, at point x logo is pointing up, at x + 1000 logo is pointing down
        composeTestRule.mainClock.advanceTimeBy(2000)
        node
            .assertIsDisplayed()
    }

    // TODO: test spinner colors?
}
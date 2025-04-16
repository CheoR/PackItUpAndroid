package com.example.packitupandroid.ui.common.layout

import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.R
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DeliveryOptionsModalBottomSheetTests {

    lateinit var optionShipping: String
    lateinit var optionSummarize: String
    lateinit var optionBackup: String
    var onDismissCalled = false

    private lateinit var sheetState: SheetState
    private lateinit var coroutineScope: CoroutineScope

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {

        composeTestRule.setContent {
            optionShipping = stringResource(R.string.delivery_option_shipping)
            optionSummarize = stringResource(R.string.delivery_option_summarize)
            optionBackup = stringResource(R.string.delivery_option_backup)

            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            coroutineScope = rememberCoroutineScope()

            if (sheetState.isVisible) {
                DeliveryOptionsModalBottomSheet(
                    sheetState = sheetState,
                    coroutineScope = coroutineScope,
                    onDismissRequest = { coroutineScope.launch {
                        sheetState.hide()
                        onDismissCalled = true
                    }}
                )
            }


        }
    }

    @Test
    fun deliveryOptionsModalBottomSheet_init_sheetIsNotDisplayed() {
        composeTestRule.onNodeWithTag("ModalBottomSheet")
            .assertIsNotDisplayed()
    }

    @Test
    fun deliveryOptionsModalBottomSheet_init_deliveryOptionsAreNotDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(optionShipping)
            .assertIsNotDisplayed()

        composeTestRule
            .onNodeWithContentDescription(optionSummarize)
            .assertIsNotDisplayed()

        composeTestRule
            .onNodeWithContentDescription(optionBackup)
            .assertIsNotDisplayed()
    }

    @Test
    fun deliveryOptionsModalBottomSheet_clickModalSheet_buttonsAreDisplayed() {

        composeTestRule.runOnIdle {
            coroutineScope.launch {
                sheetState.show()
            }
        }

        composeTestRule
            .onNodeWithContentDescription(optionShipping)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(optionSummarize)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(optionBackup)
    }

    @Test
    fun deliveryOptionsModalBottomSheet_optionClicked_StateSheetHidden() {

        composeTestRule.runOnIdle {
            coroutineScope.launch {
                sheetState.show()
            }
        }

        // TODO: lookup if i shoul dhave a test for each button since they're the same
        // and just differe in text/icon?
        composeTestRule
            .onNodeWithContentDescription(optionShipping)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()
        assertTrue("onDismissRequest callback was not triggered", onDismissCalled)
    }
}

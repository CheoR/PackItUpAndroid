package com.example.packitupandroid

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//class ButtonTest {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun button_enabled_onClicked() {
//        composeTestRule.setContent {
//                AddConfirmCancelButton(button = ButtonType.Confirm, onClick = { /*TODO*/ })
//        }
//
//        composeTestRule.onNodeWithText("confirm").assertIsEnabled()
//    }
//}
class CounterUiTests {

    private val onClick: (Item, Int?) -> Unit = { _, _ -> }
    private val item = Item(
        name = "Sample Item",
        description = "This is a sample item",
        value = 10.0,
        isFragile = false
    )


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun incrementCounterBy5() {
        val initialCount = 0
        val addThisMany = 5

        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (
                    type = item,
                    onClick = onClick
                )
            }
        }

        repeat(addThisMany) {
            composeTestRule.onNodeWithContentDescription("add").performClick()
        }

        composeTestRule.onNodeWithContentDescription("Counter Value")
            .assertTextEquals((addThisMany).toString())
//            .assertTextEquals((initialCount + addThisMany).toString())
    }
}
//@RunWith(AndroidJUnit4::class)
//class Test {
//    // Arrange
//    val item = Item(
//        name = "Sample Item",
//        description = "This is a sample item",
//        value = 10.0,
//        isFragile = false
//    )
//    var receivedItem: Item? = null
//    var receivedCount: Int? = null
//    val onClick: (Item, Int?) -> Unit = { item, count ->
//        receivedItem = item
//        receivedCount = count
//    }
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//    @Test
//    fun testCounterOnClick() {
//        composeTestRule.setContent {
////            PackItUpAndroidTheme {
//                // Act
//                Counter(
//                    onClick = onClick,
//                    type = item,
//                )
////            }
//        }
//        composeTestRule.onNodeWithTag("ADD").performClick()
//        composeTestRule.onNodeWithText("1").assertExists()
////        composeTestRule.onNodeWithContentDescription("Counter Value")
////        composeTestRule.onRoot().captureToImage()
//
////        composeTestRule.onNodeWithTag("ADD") // .captureToImage()
//
////        composeTestRule.onNodeWithContentDescription("add")
////        composeTestRule.onNodeWithContentDescription("add").performClick()
//        // Assert
////        assertEquals(item, receivedItem)
////        assertEquals(1, receivedCount)
//    }
//}

//class CounterUiTests {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private val onClick: (Item, Int?) -> Unit = { _, _ -> }
//    private val item = Item(
//        id = "aff",
//        name = "test item",
//    )
//
//
//    @Test
//    fun incrementCount() {
//        composeTestRule.setContent {
//            PackItUpAndroidTheme {
//                Counter<Item>(
//                    onClick = onClick,
//                    type = item,
//                )
//            }
//        }
//
//        // Check initial state
//        composeTestRule.onNodeWithText("0").assertExists()
//
//        // Click on add button
//        composeTestRule.onNodeWithContentDescription("add").performClick()
//
//        // Check if count increased
//        composeTestRule.onNodeWithText("1").assertExists()
//
//        // Click on subtract button
//        composeTestRule.onNodeWithContentDescription("subtract").performClick()
//
//        // Check if count decreased
//        composeTestRule.onNodeWithText("0").assertExists()
//    }
//}
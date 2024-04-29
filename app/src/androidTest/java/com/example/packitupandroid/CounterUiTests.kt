package com.example.packitupandroid

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import org.junit.Rule
import org.junit.Test
import java.util.Date

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

    private val onCreate: (Int) -> Unit = { _ -> }
    private val item = Item(
        name = "Sample Item",
        description = "This is a sample item",
        value = 10.0,
        isFragile = false,
        boxId = "e99a99f8-748d-427a-a305-14bda19d71a0",
        lastModified = Date(1638912000000L),
    )


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun incrementCounterBy5() {
        val initialCount = 0
        val addThisMany = 5

        composeTestRule.setContent {
            PackItUpAndroidTheme {
                Counter (onCreate = onCreate)
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
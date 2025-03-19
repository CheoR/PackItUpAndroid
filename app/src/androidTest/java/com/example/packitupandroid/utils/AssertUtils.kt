package com.example.packitupandroid.utils

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import junit.framework.TestCase.assertEquals


fun ComposeTestRule.assertFieldIsEditable(semanticText: String, isEnabled: Boolean = true) {
    this.onNodeWithContentDescription(semanticText)
        .assertExists()
        .assertIsDisplayed()
        .apply {
            if (isEnabled) assertIsEnabled() else assertIsNotEnabled()
        }
}

fun ComposeTestRule.assertChildrenCount(tag: String, expectedCount: Int, allNodes: Boolean = true) {
    // TODO: add intermediate checks to ensure valid state (e.g., the presence of parent nodes).
    // TODO: Fetching semantics nodes from onAllNodes or onNode and assuming availability might lead to flaky tests.
    val childrenCount = if (allNodes) {
        this
            .onAllNodes(hasTestTagThatContains(tag))
            .fetchSemanticsNodes()
            .size
    } else {
        this
            .onNode(hasTestTagThatContains(tag))
            .onChildren()
            .fetchSemanticsNodes()
            .size
    }
    assertEquals(expectedCount, childrenCount)
}

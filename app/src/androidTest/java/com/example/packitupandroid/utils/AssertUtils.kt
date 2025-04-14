package com.example.packitupandroid.utils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import junit.framework.TestCase.assertEquals


fun ComposeTestRule.assertNodeExists(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).assertExists()
}

fun ComposeTestRule.assertNodeDoesNotExist(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).assertDoesNotExist()
}

fun ComposeTestRule.assertNodeIsDisplayed(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).assertIsDisplayed()
}

fun ComposeTestRule.assertNodeIsNotDisplayed(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).assertIsNotDisplayed()
}

fun ComposeTestRule.assertNodeHasText(text: String) {
    onNodeWithText(text).assertIsDisplayed()
}

fun ComposeTestRule.assertNodeIsNotEnabled(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).assertIsNotEnabled()
}

fun ComposeTestRule.assertNodeIsOn(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).assertIsOn()
}

fun ComposeTestRule.assertFieldIsEditable(semanticText: String, isEnabled: Boolean = true) {
    this.onNodeWithContentDescription(semanticText)
        .assertExists()
        .assertIsDisplayed()
        .apply {
            if (isEnabled) assertIsEnabled() else assertIsNotEnabled()
        }
}

fun ComposeTestRule.assertExists(matcher: SemanticsNodeInteraction) {
    matcher.assertExists()
}

fun ComposeTestRule.assertIsDisplayed(matcher: SemanticsNodeInteraction) {
    matcher.assertIsDisplayed()
}

fun ComposeTestRule.assertIsNotEnabled(matcher: SemanticsNodeInteraction) {
    matcher.assertIsNotEnabled()
}

fun ComposeTestRule.assertDoesNotExist(matcher: SemanticsNodeInteraction) {
    matcher.assertDoesNotExist()
}

fun ComposeTestRule.assertIsNotDisplayed(matcher: SemanticsNodeInteraction) {
    matcher.assertIsNotDisplayed()
}

fun ComposeTestRule.assertIsChecked(matcher: SemanticsNodeInteraction) {
    matcher.assertIsOn()
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

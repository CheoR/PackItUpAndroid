package com.example.packitupandroid.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex


fun hasContentDescriptionThatEquals(contentDescription: String): SemanticsMatcher {
    return SemanticsMatcher("ContentDescription equals $contentDescription") { node ->
        val description = node.config.getOrNull(SemanticsProperties.ContentDescription)
        description?.contains(contentDescription) == true
    }
}

fun hasTestTagThatContains(substring: String): SemanticsMatcher {
    // TODO: matcher works fine but can lead to false positives if multiple elements have
    //  overlapping tags. Adding more specificity to the matcher logic could be useful,
    //  especially in complex UI.
    return SemanticsMatcher("TestTag contains $substring") { node ->
        val testTag = node.config.getOrNull(SemanticsProperties.TestTag)
        testTag?.contains(substring) == true
    }
}

fun ComposeTestRule.performActionOnNode(
    contentDescription: String,
    times: Int,
    action: SemanticsNodeInteraction.() -> SemanticsNodeInteraction
) {
    repeat(times) {
        this.onNodeWithContentDescription(contentDescription)
            .action()
    }
    this.mainClock.advanceTimeBy(DELAY)
}

fun ComposeTestRule.incrementCounter(times: Int) =
    performActionOnNode("increment", times) { performClick() }

fun ComposeTestRule.decrementCounter(times: Int) =
    performActionOnNode("decrement", times) { performClick() }

fun ComposeTestRule.assertCounterValue(expectedValue: Int) {
    onNodeWithContentDescription(
        label = "counter value $expectedValue",
        ignoreCase = true,
    )
        .assert(hasAnyChild(hasText(expectedValue.toString())))
}

fun ComposeTestRule.clickAdd() =
    performActionOnNode("add", 1) { performClick() }

fun ComposeTestRule.confirmationDialogSelection(selection: String = "confirm") {
    this
        .onNodeWithText(selection)
        .performClick()
    this.mainClock.advanceTimeBy(DELAY)
}

fun ComposeTestRule.insertFiveItems() {
    this.incrementCounter(incrementCountByFive)
    this.clickAdd()
    this.mainClock.advanceTimeBy(DELAY)
}

fun ComposeTestRule.insertFiveBoxes() {
    incrementCounter(incrementCountByFive)
    clickAdd()
    this.mainClock.advanceTimeBy(DELAY)
}


fun ComposeTestRule.scrollToLastElement(elements: Int) {
    // TODO: Add a check for no card here if needed
    this
        .onNodeWithTag("LazyColumn")
        .performScrollToIndex(elements - 1)
}

fun ComposeTestRule.openMenuSelectionOption(card: SemanticsNodeInteraction, selection: String = "edit") {
    openMenu(card)
    this.mainClock.advanceTimeBy(DELAY * 2)
    this
        .onNodeWithText(selection)
        .assertExists()
        .performClick()
    this.mainClock.advanceTimeBy(DELAY)
}

fun ComposeTestRule.openMenu(card: SemanticsNodeInteraction) {
    card
        .onChildren()
        .filterToOne(hasContentDescription("Open Menu"))
        .performClick()
    this.mainClock.advanceTimeBy(DELAY)
}

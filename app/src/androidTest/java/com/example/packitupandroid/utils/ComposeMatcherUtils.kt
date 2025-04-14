package com.example.packitupandroid.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex


fun createCustomMatcher(): SemanticsMatcher {
    return hasContentDescription("Your Description")
}

fun createHasTextMatcher(text: String): SemanticsMatcher {
    return hasText(text)
}

fun createIsDisabledMatcher(): SemanticsMatcher {
    return SemanticsMatcher.keyIsDefined(SemanticsProperties.Disabled)
}

fun createIsOnMatcher(): SemanticsMatcher {
    return SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, ToggleableState.On)
}

fun hasTextThatContains(substring: String): SemanticsMatcher {
    return SemanticsMatcher("Text contains $substring") { node ->
        val text = node.config.getOrNull(SemanticsProperties.Text)
        text?.any { it.text.contains(substring) } ?: false
    }
}

//fun hasContentDescriptionThatEquals(contentDescription: String): SemanticsMatcher {
//    println("\n\n-----\ncontentDescritpion is: $contentDescription")
//    val data = SemanticsMatcher("ContentDescription equals $contentDescription") { node ->
//        val description = node.config.getOrNull(SemanticsProperties.ContentDescription)
//            ?.map { it.lowercase() }
//        println("\n\tdescription is: $description")
//        description?.contains(contentDescription.lowercase()) == true
//    }
//    println("\n\n returning data: ${data.toString()}")
//    return data
//}

fun hasContentDescriptionThatEquals(contentDescription: String): SemanticsMatcher {
    return SemanticsMatcher("ContentDescription equals $contentDescription") { node ->
        val description = node.config.getOrNull(SemanticsProperties.ContentDescription)
        description?.contains(contentDescription) == true
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

fun ComposeTestRule.scrollToLastElement(type: String, elements: Int) {
    // TODO: Add a check for no card here if needed
    this
        .onNodeWithContentDescription("$type list")
        .performScrollToIndex(elements - 1)
}

fun ComposeTestRule.openMenuSelectionOption(
    card: SemanticsNodeInteraction,
    selection: String = "edit"
) {
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

fun hasTestTagThatContains(substring: String): SemanticsMatcher {
    // TODO: matcher works fine but can lead to false positives if multiple elements have
    //  overlapping tags. Adding more specificity to the matcher logic could be useful,
    //  especially in complex UI.
    return SemanticsMatcher("TestTag contains $substring") { node ->
        val testTag = node.config.getOrNull(SemanticsProperties.TestTag)
        testTag?.contains(substring) == true
    }
}

fun ComposeTestRule.scrollToLastElement(elements: Int) {
    // TODO: Add a check for no card here if needed
    this
        .onNodeWithTag("LazyColumn")
        .performScrollToIndex(elements - 1)
}

fun ComposeTestRule.badgeColumnMatcher(contentDescription: String) =
    this.onNodeWithContentDescription(contentDescription)

fun ComposeTestRule.nameFieldMatcher(contentDescription: String) =
    this.onNodeWithContentDescription(contentDescription)

fun ComposeTestRule.dropdownSelectionMatcher(selectedName: String) =
    this.onNodeWithContentDescription("$selectedName selected")

fun ComposeTestRule.descriptionFieldMatcher(contentDescription: String) =
    this.onNodeWithContentDescription(contentDescription)

fun ComposeTestRule.fragileCheckboxMatcher(contentDescription: String) =
    this.onNodeWithContentDescription(contentDescription)

fun ComposeTestRule.valueFieldMatcher(contentDescription: String) =
    this.onNodeWithContentDescription(contentDescription)

fun ComposeTestRule.swipeMenuMatcher(contentDescription: String) =
    this.onNodeWithContentDescription(contentDescription)

package com.example.packitupandroid.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft


fun ComposeTestRule.performClick(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).performClick()
}

fun ComposeTestRule.performSwipeLeft(contentDescription: String) {
    onNodeWithContentDescription(contentDescription).performTouchInput { swipeLeft() }
}

fun ComposeTestRule.findNodeByContentDescription(contentDescription: String): SemanticsNodeInteraction {
    return onNodeWithContentDescription(contentDescription)
}

fun ComposeTestRule.findNodeWithText(text: String): SemanticsNodeInteraction {
    return onNodeWithText(text)
}

//fun ComposeTestRule.getRandomCardIdFromList(): String {
//
//    val nodes = this.onAllNodes(hasTestTagThatContains("edit card "))
//    if (nodes.fetchSemanticsNodes().isEmpty()) {
//        throw IllegalStateException("No cards found!")
//    }
//
//    val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
//        node.config.getOrNull(SemanticsProperties.TestTag)?.substringAfter("BaseCard ")
//    }
//
//    return cardIds.random()
//}


//fun ComposeTestRule.hasContentDescriptionThatContains(substring: String): SemanticsMatcher {
//    return SemanticsMatcher("ContentDescription contains $substring") { node ->
//        val description = node.config.getOrNull(SemanticsProperties.ContentDescription)
//        description?.contains(substring) ?: false
//    }
//}
fun ComposeTestRule.hasContentDescriptionThatContains(substring: String): SemanticsMatcher {
    return SemanticsMatcher("ContentDescription contains $substring") { node ->
        val description = node.config.getOrNull(SemanticsProperties.ContentDescription)
        description?.contains(substring) ?: false
    }
}

//fun ComposeTestRule.getFieldNodeByContentDescription(
//    card: SemanticsNodeInteraction,
//    contentDescription: String
//): SemanticsNodeInteraction {
//    val field = card
//        .onChildren()
//        .filterToOne(hasContentDescriptionThatContains(contentDescription))
//    return field
//}

//fun ComposeTestRule.getFieldNodeByContentDescription(
//    card: SemanticsNodeInteraction,
//    contentDescription: String
//): SemanticsNodeInteraction {
//    return card.onChildren().filterToOne(hasContentDescription(contentDescription))
//}

fun ComposeTestRule.getFieldNodeByContentDescription(
    card: SemanticsNodeInteraction,
    contentDescription: String
): SemanticsNodeInteraction {
    val field = card
        .onChildren()
        .filterToOne(hasContentDescriptionThatContains(contentDescription))
    return field
}

//fun ComposeTestRule.getRandomCardIdFromList(): String {
//    val nodes = this.onAllNodes(hasTestTagThatContains("BaseCard"))
//    val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
//        node.config.getOrNull(SemanticsProperties.TestTag)?.substringAfter("BaseCard ")
//    }
//
//    return cardIds.random()
//}
//
//fun ComposeTestRule.getRandomCardIdFromList(): String {
////    val nodes = this.onAllNodes(hasTestTagThatContains("BaseCard"))
//    val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
//        node.config.getOrNull(SemanticsProperties.ContentDescription)?.firstOrNull()?.substringAfter("BaseCard ")
//    }
//    return cardIds.random()
//}

//fun ComposeTestRule.getRandomCardIdFromList(contentDescriptionPrefix: String): String {
//    val nodes = this.onAllNodes(hasContentDescriptionThatEquals(contentDescriptionPrefix))
//    println("\n========\tthere are ${nodes.fetchSemanticsNodes().size} elements with content description: $contentDescriptionPrefix")
//    if (nodes.fetchSemanticsNodes().isEmpty()) {
//        throw IllegalStateException("No elements found with content description: $contentDescriptionPrefix")
//    }
//
//    val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
//        node.config.getOrNull(SemanticsProperties.ContentDescription)
//            ?.firstOrNull()
////            ?.substringAfter(contentDescriptionPrefix)
//    }
//
//    val selectedCArd = cardIds.random()
//    println("\n==========\n\t\tSELECTED CARD: $selectedCArd")
//    println("\n==========\n\t\tSELECTED CARD: $cardIds.random()")
//    return cardIds.random()
//}

fun ComposeTestRule.getCardById(id: String): SemanticsNodeInteraction {
    return this.onNode(hasContentDescriptionThatContains("BaseCard $id"))

}

fun ComposeTestRule.getFieldValue(field: SemanticsNodeInteraction): String {
    return field
        .fetchSemanticsNode()
        .config.getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text ?: ""
}

fun ComposeTestRule.replaceTextField(fieldName: String, text: String) {
    this
        .onNodeWithContentDescription(fieldName)
        .performTextReplacement(text)
}

fun ComposeTestRule.toggleCheckbox() {
    this
        .onNodeWithContentDescription("Edit Fragile Checkbox")
        .performClick()
}

fun ComposeTestRule.swipeLeft(contentDescription: String) {
    this.onNodeWithContentDescription(contentDescription).performTouchInput {
        swipeLeft()
    }
}
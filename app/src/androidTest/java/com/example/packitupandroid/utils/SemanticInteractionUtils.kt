package com.example.packitupandroid.utils

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement


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

fun ComposeTestRule.getRandomCardIdFromList(contentDescriptionPrefix: String): String {
    val nodes = this.onAllNodes(hasContentDescriptionThatEquals(contentDescriptionPrefix))
    if (nodes.fetchSemanticsNodes().isEmpty()) {
        throw IllegalStateException("No elements found with content description: $contentDescriptionPrefix")
    }

    val cardIds = nodes.fetchSemanticsNodes().mapNotNull { node ->
        node.config.getOrNull(SemanticsProperties.ContentDescription)
            ?.firstOrNull()
            ?.substringAfter(contentDescriptionPrefix)
    }

    return cardIds.random()
}


fun ComposeTestRule.getCardById(id: String): SemanticsNodeInteraction {
    return this.onNode(hasTestTagThatContains(id))
}

fun ComposeTestRule.getFieldNodeByContentDescription(
    card: SemanticsNodeInteraction,
    contentDescription: String
): SemanticsNodeInteraction {
    return card.onChildren().filterToOne(hasContentDescription(contentDescription))
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

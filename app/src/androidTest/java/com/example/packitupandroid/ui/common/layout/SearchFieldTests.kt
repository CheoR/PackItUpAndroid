package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextReplacement
import com.example.packitupandroid.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SearchFieldTests {

    private lateinit var searchContentDescription: String
    private lateinit var searchQuery: MutableState<String>

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        searchQuery = mutableStateOf("")

        composeTestRule.setContent {
            searchContentDescription = stringResource(R.string.search)
            SearchField(
                searchQuery = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Test
    fun searchField_initialState_placeHolderIsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(searchContentDescription, useUnmergedTree = true)
            .onChild()
            .assertTextEquals(searchContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun searchField_inputText_searchQueryIsUpdated() {
        val inputText = "Test Query"

        composeTestRule
            .onNodeWithContentDescription(searchContentDescription)
            .performTextReplacement(inputText)

        composeTestRule
            .onNodeWithContentDescription(searchContentDescription)
            .assertTextEquals(inputText)

        assert(searchQuery.value == inputText)
    }

    @Test
    fun searchField_inputText_placeholderDisappearsOnInput() {
        val inputText = "Query"

        composeTestRule
            .onNodeWithContentDescription(searchContentDescription)
            .performTextReplacement(inputText)

        composeTestRule
            .onNodeWithContentDescription(searchContentDescription, useUnmergedTree = true)
            .onChild()
            .assert(!hasText(searchContentDescription))
    }

     // TODO: test styles/colors/etc.
}

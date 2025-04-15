package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


/**
 * SearchField composable function creates search field for text input to filter element list.
 *
 * This composable provides a styled text field for users to input search queries.
 * It displays a placeholder "Search" text when the input is empty and updates
 * the provided [searchQuery] state with every change. It also sets a content description
 * for accessibility purposes.
 *
 * @param searchQuery The current text value in the search field.
 * @param onValueChange A lambda function invoked when the text in the search field changes.
 *                      It receives the new text value as a parameter.
 * @param modifier Modifier to be applied to the search field.
 */
@Composable
fun SearchField(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchContentDescription = stringResource(R.string.search)

    BasicTextField(
        value = searchQuery,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.space_arrangement_small))
            .semantics { contentDescription = searchContentDescription },
        textStyle = MaterialTheme.typography.bodyLarge,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
                    .padding(8.dp)
            ) {
                if (searchQuery.isEmpty()) {
                    Text(
                        stringResource(R.string.search),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
            }
        }
    )
}


@Preview
@Composable
fun PreviewSearchField() {
    var searchQuery by remember { mutableStateOf("") }

    SearchField(
        searchQuery = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth()
    )
}

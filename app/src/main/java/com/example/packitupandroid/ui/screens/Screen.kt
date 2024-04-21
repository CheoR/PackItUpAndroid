package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.R
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.components.counter.Counter

@Composable
fun <T: BaseCardData> Screen(
    uiState: PackItUpUiState,
    createElement: (Int) -> Unit,
    updateElement: (T) -> Unit,
    destroyElement: (T) -> Unit,
    card: @Composable (T, (T) -> Unit, (T) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val elements: List<T>? = when(val result = uiState.result) {
        is Result.Complete -> result.elements?.map { it as T }
        else -> emptyList()
    }

    Column(modifier = modifier) {
        when(uiState.result) {
            is Result.Loading -> ContentMessage(text="Loading . .")
            is Result.Error ->  ContentMessage(text = "Error . . ")
            is Result.Complete -> {
                LazyColumn(
                    modifier = modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.space_arrangement_small)
                    )
                ) {
                    items(
                        items = elements ?: emptyList(),
                        key = { it.id }
                    ) { element ->
                        card(element, updateElement, destroyElement)
                    }
                }
            }

            else -> {}
        }
        Counter(onClick = createElement)
    }
}

@Composable
private fun ContentMessage(text: String) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = text)
    }
}

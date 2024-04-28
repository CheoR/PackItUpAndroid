package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.R
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.components.spinner.Spinner
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.utils.CardType

@Composable
fun <T: BaseCardData> Screen(
    uiState: PackItUpUiState,
    createElement: (Int) -> Unit,
    updateElement: (T) -> Unit,
    destroyElement: (T) -> Unit,
    card: @Composable (T, (T) -> Unit, (T) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rememberedElements: List<T>? = remember(uiState.result) {
         when(uiState.result) {
            is Result.Complete -> uiState.result.elements?.map { it as T }
            else -> emptyList()
        }
    }

    Column(modifier = modifier) {
        when(uiState.result) {
            is Result.Loading -> Spinner()
            is Result.Error ->  Error()
            is Result.Complete -> {
                LazyColumn(
                    modifier = modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.space_arrangement_small)
                    )
                ) {
                    items(
                        items = rememberedElements ?: emptyList(),
                        key = { it.id }
                    ) { element ->
                        card(element, updateElement, destroyElement)
                    }
                }
            }
        }
        Counter(onCreate = onCreate)
    }
}

@Composable
private fun Error() {
    // TODO: move to utils
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Screen Error")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val boxes = localDataSource.loadBoxes()
    val uiState = PackItUpUiState(
        currentScreen = PackItUpRoute.BOXES,
        result = Result.Complete(boxes)
    )
    Screen<Box>(
        uiState = uiState,
        createElement = {},
        updateElement = {},
        destroyElement = {},
        card = { data, update, destroy ->
            BaseCard(
                data = data,
                onUpdate = update,
                onDestroy = destroy,
                cardType = CardType.Box,
            )
        },
    )
}

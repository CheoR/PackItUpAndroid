package com.example.packitupandroid.ui.screens.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType

@Composable
fun<T: BaseCardData> ItemsScreen(
    uiState: PackItUpUiState,
    createElement: (Int) -> Unit,
    updateElement: (T) -> Unit,
    destroyElement: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Screen(
        modifier = modifier,
        uiState = uiState,
        card = { data, update, destroy ->
            BaseCard(
                data = data,
                onUpdate = update,
                onDestroy = destroy,
                cardType = CardType.Item,
            )
        },
        createElement = createElement,
        updateElement = updateElement,
        destroyElement = destroyElement,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val items = localDataSource.loadItems()
    val uiState = PackItUpUiState(
        currentScreen = "Items",
        result = Result.Complete(items)
    )
    ItemsScreen<Item>(
        uiState = uiState,
        createElement = {},
        updateElement = {},
        destroyElement = {},
    )
}

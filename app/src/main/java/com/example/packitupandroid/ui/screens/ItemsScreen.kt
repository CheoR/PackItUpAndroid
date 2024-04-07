package com.example.packitupandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.CardType

@Composable
fun ItemsScreen(
    uiState: PackItUpUiState,
    onCreate: (BaseCardData, Int?) -> Unit,
    onDestroy: (BaseCardData) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Screen(
        modifier = modifier,
        elements = uiState.items,
        // TODO: fix this
        type = Item(name="fix this"),
        card = { data, update, destroy ->
            BaseCard(
                data = data,
                onUpdate = { baseCardData -> update(baseCardData as Item) },
                onDestroy = { destroy(data) },
                cardType = CardType.Item,
            )
        },
        onClick = onCreate,
        updateElement = onUpdate,
        destroyElement = onDestroy,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val currentScreen = ScreenType.Summary
    val items = localDataSource.loadItems()
    val boxes = localDataSource.loadBoxes()
    val collections = localDataSource.loadCollections()

    val uiState = PackItUpUiState(
        currentScreen = currentScreen,
        items = items,
        boxes = boxes,
        collections = collections,
    )

    ItemsScreen(
        uiState = uiState,
        onCreate = { item , count -> },
        onDestroy = {},
        onUpdate = {},
    )
}

package com.example.packitupandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.packitupandroid.data.ScreenType
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.CardType

@Composable
fun BoxesScreen(
    uiState: PackItUpUiState,
    onCreate: (BaseCardData, Int?) -> Unit,
    onDestroy: (BaseCardData) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Screen(
        modifier = modifier,
        elements = uiState.boxes,
        card = { data, update, destroy ->
            BaseCard(
                data = data,
                onUpdate = { baseCardData -> update(baseCardData as Box) },
                onDestroy = { destroy(data) } , // { baseCardData -> destroy(baseCardData as Box) },
                cardType = CardType.Box,
            )
        },
        onClick = onCreate,
        updateElement = onUpdate,
        destroyElement = onDestroy,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxesScreen(
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

    BoxesScreen(
        uiState = uiState,
        onCreate = { box, count -> },
        onDestroy = {},
        onUpdate = {},
    )
}

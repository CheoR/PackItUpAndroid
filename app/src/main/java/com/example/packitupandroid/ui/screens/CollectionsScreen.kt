package com.example.packitupandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.CardType

@Composable
fun CollectionsScreen(
    uiState: PackItUpUiState,
    onCreate: (BaseCardData, Int?) -> Unit,
    onDestroy: (BaseCardData) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Screen(
        modifier = modifier,
        elements = uiState.collections,
        card = { data, update, destroy ->
            BaseCard(
                data = data,
                onUpdate = { baseCardData -> update(baseCardData as Collection) },
                onDestroy = { destroy(data) }, // { baseCardData -> destroy(baseCardData as Collection) },
                cardType = CardType.Collection,
            )
        },
        onClick = onCreate,
        updateElement =  onUpdate,
        destroyElement = onDestroy,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCollectionsScreen(
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

    CollectionsScreen(
        uiState = uiState,
        onCreate = { collection, count -> },
        onDestroy = {},
        onUpdate = {},
    )
}

package com.example.packitupandroid.ui.screens.collection

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType

@Composable
fun <T: BaseCardData> CollectionsScreen(
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
                cardType = CardType.Collection,
            )
        },
        createElement = createElement,
        updateElement = updateElement,
        destroyElement = destroyElement,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCollectionsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collections = localDataSource.loadCollections()
    val uiState = PackItUpUiState(
        currentScreen = "Boxes",
        result = Result.Complete(collections)
    )
    CollectionsScreen<Collection>(
        uiState = uiState,
        createElement = {},
        updateElement = {},
        destroyElement = {},
    )
}

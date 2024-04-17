package com.example.packitupandroid.ui.screens.box

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.Result

@Composable
fun <T: BaseCardData> BoxesScreen(
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
                cardType = CardType.Box,
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
    val boxes = localDataSource.loadBoxes()
    val uiState = PackItUpUiState(
        currentScreen = "Boxes",
        result = Result.Complete(boxes)
    )
    BoxesScreen<Box>(
        uiState = uiState,
        createElement = {},
        updateElement = {},
        destroyElement = {},
    )
}

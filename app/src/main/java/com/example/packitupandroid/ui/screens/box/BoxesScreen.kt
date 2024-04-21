package com.example.packitupandroid.ui.screens.box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType

@Composable
fun <T: BaseCardData> BoxesScreen(
    modifier: Modifier = Modifier,
    viewModel: BoxesScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
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
        createElement = viewModel::create,
        updateElement = viewModel::update,
        destroyElement = viewModel::destroy,
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewItemsScreen(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val boxes = localDataSource.loadBoxes()
//    val uiState = PackItUpUiState(
//        currentScreen = "Boxes",
//        result = Result.Complete(boxes)
//    )
//    BoxesScreen<Box>(
//        uiState = uiState,
//        createElement = {},
//        updateElement = {},
//        destroyElement = {},
//    )
//}

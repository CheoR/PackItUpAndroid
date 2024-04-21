package com.example.packitupandroid.ui.screens.collection

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
fun <T: BaseCardData> CollectionsScreen(
    modifier: Modifier = Modifier,
    viewModel: CollectionsScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
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
                cardType = CardType.Collection,
            )
        },
        createElement = viewModel::create,
        updateElement = viewModel::update,
        destroyElement = viewModel::destroy,
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCollectionsScreen(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collections = localDataSource.loadCollections()
//    val uiState = PackItUpUiState(
//        currentScreen = "Boxes",
//        result = Result.Complete(collections)
//    )
//    CollectionsScreen<Collection>(
//        uiState = uiState,
//        createElement = {},
//        updateElement = {},
//        destroyElement = {},
//    )
//}

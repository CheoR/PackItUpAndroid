package com.example.packitupandroid.ui.screens.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType

@Composable
fun ItemsScreen(
    viewModel: ItemsScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
) {
    val uiState = viewModel.uiState.collectAsState().value
    Screen(
        uiState = uiState,
        onCreate = viewModel::create,
        onUpdate = viewModel::update,
        onDestroy = viewModel::destroy,
        cardType = CardType.Item,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val items = localDataSource.loadItems()
    val uiState = ItemsPackItUpUiState(
        elements = items,
        result = Result.Complete(items)
    )
    Screen<Item>(
        uiState = uiState,
        onCreate = {},
        onUpdate = {},
        onDestroy = {},
        cardType = CardType.Item,
    )
}

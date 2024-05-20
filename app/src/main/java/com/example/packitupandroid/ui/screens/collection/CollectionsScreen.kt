package com.example.packitupandroid.ui.screens.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType

@Composable
fun CollectionsScreen(
    filterBoxesByCollectionId:(id: String) -> Unit,
    viewModel: CollectionsScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
) {
    val uiState = viewModel.uiState.collectAsState().value
    Screen<Collection>(
        uiState = uiState,
        onCreate = viewModel::create,
        onUpdate = viewModel::update,
        onDestroy = viewModel::destroy,
        cardType = CardType.Collection,
        filterElements = filterBoxesByCollectionId,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collections = localDataSource.loadCollections()
    val uiState = CollectionsScreenUiState(
        elements = collections,
        result = Result.Complete(collections)
    )
    Screen<Collection>(
        uiState = uiState,
        onCreate = {},
        onUpdate = {},
        onDestroy = {},
    )
}

package com.example.packitupandroid.ui.screens.box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.CardType

@Composable
fun BoxesScreen(
    filterItemsByBoxId:((id: String) -> Unit)? = null,
    getDropdownOptions: () -> List<QueryDropdownOptions>,
    viewModel: BoxesScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
) {
    val uiState = viewModel.uiState.collectAsState().value
    Screen<Box>(
        uiState = uiState,
        onCreate = viewModel::create,
        onUpdate = viewModel::update,
        onDestroy = viewModel::destroy,
        getDropdownOptions = getDropdownOptions,
        cardType = CardType.Box,
        filterElements = filterItemsByBoxId,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val boxes = localDataSource.loadBoxes()
    val uiState = BoxesScreenUiState(
        elements = boxes,
        result = Result.Complete(boxes)
    )
    Screen<Item>(
        uiState = uiState,
        onCreate = {},
        onUpdate = {},
        onDestroy = {},
        getDropdownOptions = { emptyList() },
    )
}

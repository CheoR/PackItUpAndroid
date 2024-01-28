package com.example.packitupandroid.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.ItemCard
import com.example.packitupandroid.ui.components.common.PackItUpAppBar
import com.example.packitupandroid.ui.components.counter.Counter


@Composable
fun ItemsScreen(
    modifier: Modifier = Modifier,
    uiState: PackItUpUiState,
    onCreateClick: (Int?) -> Unit,
    onDeleteClick: (String) -> Unit,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
) {
    Scaffold(
        topBar = {
            PackItUpAppBar(
                title = stringResource(R.string.items),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = modifier
                    .weight(2f),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                )
            ) {
                items(
                    items = uiState.items,
                    key = { it.id }
                ) {
                    ItemCard(
                        item = it,
                        onUpdate = {},
                        onDelete = onDeleteClick,
                        onCardClick = {},
                    )
                    // TODO: remove this line when done with it
                    Text(text = "Total items: ${uiState.items.size}")
                }
            }
            Counter(screen = ScreenType.Items, onClick = onCreateClick)
        }
    }
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
        onCreateClick = { count -> Log.i("Items ", "Creating ${count} items")},
        onDeleteClick = { Log.i("Items ", "Deleting ${items[0].id} items")},
        navigateBack = {},
        onNavigateUp = {},
        canNavigateBack = true,
        modifier = Modifier,
    )
}

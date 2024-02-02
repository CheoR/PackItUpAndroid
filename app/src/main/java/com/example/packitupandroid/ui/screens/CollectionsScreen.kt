package com.example.packitupandroid.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.CollectionCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.counter.Counter

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    uiState: PackItUpUiState,
    onCreate: (Int?) -> Unit,
    onDelete: (String) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            )
        ) {
            items(
                items = uiState.collections,
                key = { it.id }
            ) {
                CollectionCard(
                    collection = it,
                    onUpdate = onUpdate,
                    onDelete = {},
                    onCardClick = {},
                )
            }
        }
        Counter(screen = ScreenType.Collections, onClick = onCreate)
    }
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
        onCreate = { count -> Log.i("Collections ", "Creating ${count} collections")},
        onDelete = { Log.i("Collections ", "Deleting ${collections[0].id} collections")},
        onUpdate = { Log.i("Collections ", "Updating ${collections[0].id}") },
        modifier = Modifier,
    )
}

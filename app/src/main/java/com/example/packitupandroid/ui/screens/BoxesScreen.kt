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
import com.example.packitupandroid.ui.components.BoxCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.counter.Counter

@Composable
fun BoxesScreen(
    uiState: PackItUpUiState,
    onCreate: (Int?) -> Unit,
    onDelete: (String) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            )
        ) {
            items(
                items = uiState.boxes,
                key = { it.id }
            ) {
                BoxCard(
                    box = it,
                    onUpdate = onUpdate,
                    onDelete = {},
                    onCardClick = {},
                )
            }
        }
        Counter(screen = ScreenType.Boxes, onClick = onCreate)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxesScreen(
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

    BoxesScreen(
        uiState = uiState,
        onCreate = { count -> Log.i("Boxes ", "Creating ${count} boxes")},
        onDelete = { Log.i("Boxes ", "Deleting ${boxes[0].id} boxes")},
        onUpdate = { Log.i("Boxes ", "Updating ${boxes[0].id}") },
        modifier = Modifier,
    )
}

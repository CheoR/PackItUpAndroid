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
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.ui.components.CollectionCard
import com.example.packitupandroid.ui.components.counter.Counter

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    cards: List<Collection> = emptyList(),
    onClick: (Int?) -> Unit,
) {
    Column(
        modifier = Modifier,
    ) {
        LazyColumn(
            modifier = modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            )
        ) {
            items(
                items = cards,
                key = { it.id }
            ) {
                CollectionCard(
                    collection = it,
                    onUpdate = {},
                    onDelete = {},
                    onCardClick = {},
                )
            }
        }
        Counter(screen = ScreenType.Collections, onClick = onClick)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCollectionsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collections = localDataSource.loadCollections()

    CollectionsScreen(
        cards = collections,
        onClick = { count -> Log.i("Collections ", "Creating ${count} collections")},
    )
}

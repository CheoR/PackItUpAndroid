package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.ui.components.CollectionCard

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    cards: List<Collection> = emptyList(),
    ) {
    LazyColumn(
        modifier = modifier,
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
}

@Preview(showBackground = true)
@Composable
fun PreviewCollectionsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collections = localDataSource.loadCollections()

    CollectionsScreen(
        cards = collections
    )
}

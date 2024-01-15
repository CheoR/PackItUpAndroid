package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.repository.LocalDataRepository
import com.example.packitupandroid.ui.components.BoxCard

@Composable
fun BoxesScreen(
    modifier: Modifier = Modifier,
    cards: List<Box> = emptyList(),
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
            BoxCard(
                box = it,
                onUpdate = {},
                onDelete = {},
                onCardClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxesScreen(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    BoxesScreen(
        cards = localDataRepository.loadBoxes(),
    )
}

package com.example.packitupandroid.ui.screens

import BaseCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Item
import androidx.compose.ui.res.vectorResource
import com.example.packitupandroid.R
import kotlin.random.Random


@Composable
fun ItemsScreen(
    modifier: Modifier = Modifier,
    cards: List<Item> = LocalDataSource().loadItems(),
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
            BaseCard(
                title = it.name,
                description = it.description,
                onCardClick = {},
                imageId = if(Random.nextBoolean()) R.drawable.pug else null,
                imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
                buttonIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
                onButtonIconClick = { },
                value = it.value,
                isFragile = it.isFragile,
                onCheckedChange = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen() {
    ItemsScreen()
}

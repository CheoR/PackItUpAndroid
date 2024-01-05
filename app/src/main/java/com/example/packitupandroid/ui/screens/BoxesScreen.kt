package com.example.packitupandroid.ui.screens

import BaseCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box

@Composable
fun BoxesScreen(
    modifier: Modifier = Modifier,
    cards: List<Box> = LocalDataSource().loadBoxes(),
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
                description = "",
                onCardClick = {},
                imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
                buttonIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
                onButtonIconClick = { },
                value = it.totalValue,
                isFragile = it.isFragile,
                onCheckedChange = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxesScreen() {
    BoxesScreen()
}

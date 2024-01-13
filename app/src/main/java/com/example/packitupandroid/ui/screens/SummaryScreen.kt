package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.SummaryCard
import com.example.packitupandroid.ui.components.formatValue

data class Summary (
    val id: String,
    val name: String,
    val description: String = "",
    val collections: List<Collection> = emptyList(),
    val boxes: List<Box> = emptyList(),
    val items: List<Item> = emptyList(),
) {
    val isFragile: Boolean = items.any { it.isFragile }
    val value: Double = items.sumOf { it.value }
}

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    collections: List<Collection> = emptyList(),
    boxes: List<Box> = emptyList(),
    items: List<Item> = emptyList(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small))
    ) {

        SummaryCard(
            summary = Summary(
                id = "collections",
                name = "collections",
                description = "number of collections",
                collections = collections,
            ),
            onUpdate = {},
            onDelete = {},
            onCardClick = {}
        )

        SummaryCard(
            summary = Summary(
                id ="boxes",
                name = "boxes",
                description = "number of boxes",
                boxes = boxes,
            ),
            onUpdate = {},
            onDelete = {},
            onCardClick = {}
        )

        SummaryCard(
            summary = Summary(
                id ="items",
                name = "items",
                description = "number of items",
                items = items,
            ),
            onUpdate = {},
            onDelete = {},
            onCardClick = {}
        )


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Checkbox(
                checked = items.any{ it.isFragile },
                onCheckedChange = { },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Fragile")

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Total: ${items.sumOf { it.value }.formatValue()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen() {
    val collections = LocalDataSource().loadCollections()
    val boxes = LocalDataSource().loadBoxes()
    val items = LocalDataSource().loadItems()
    SummaryScreen(
        collections = collections,
        boxes = boxes,
        items = items,
    )
}
package com.example.packitupandroid.ui.screens

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.SummaryCard
import com.example.packitupandroid.ui.components.asCurrencyString
import com.example.packitupandroid.ui.components.card.BaseCardData

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
    uiState: PackItUpUiState,
    onCreate: (Int?) -> Unit,
    onDelete: (String) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        SummaryCard(
            summary = Summary(
                id = stringResource(R.string.collections),
                name = stringResource(R.string.collections),
                description = "number of collections",
                collections = uiState.collections,
            ),
            onUpdate = onUpdate,
            onDelete = { onDelete("collections") },
        )

        SummaryCard(
            summary = Summary(
                id = stringResource(R.string.boxes),
                name = stringResource(R.string.boxes),
                description = "number of boxes",
                boxes = uiState.boxes,
            ),
            onUpdate = onUpdate,
            onDelete = { onDelete("boxes") },
        )

        SummaryCard(
            summary = Summary(
                id = stringResource(R.string.items),
                name = stringResource(R.string.items),
                description = "number of items",
                items = uiState.items,
            ),
            onUpdate = onUpdate,
            onDelete = { onDelete("items") },
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Checkbox(
                checked = uiState.items.any{ it.isFragile },
                onCheckedChange = { },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Fragile")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Total: ${uiState.items.sumOf { it.value }.asCurrencyString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen(
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

    SummaryScreen(
        uiState = uiState,
        onCreate = { count -> Log.i("Items ", "Creating ${count} items")},
        onDelete = { Log.i("Items ", "Deleting ${items[0].id} items") },
        onUpdate = { Log.i("Items ", "Updating ${items[0].id}") },
    )
}

package com.example.packitupandroid.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData


@Composable
fun <T: BaseCardData> SummaryScreen(
//    uiState: PackItUpUiState,
//    onCreate: (T, Int) -> Unit,
//    onDestroy: (T) -> Unit,
//    onUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Row {
            Text(text = "Summary screen placeholder")
        }
        // TODO: FIX
    }
}
// TODO: Fix
//        SummaryCard(
//            summary = Summary(
//                id = stringResource(R.string.collections),
//                name = stringResource(R.string.collections),
//                description = "number of collections",
//                collections = uiState.collections,
//            ),
//            onUpdate = onUpdate,
//            onDestroy = onDestroy,
//        )
//
//        SummaryCard(
//            summary = Summary(
//                id = stringResource(R.string.boxes),
//                name = stringResource(R.string.boxes),
//                description = "number of boxes",
//                boxes = uiState.boxes,
//            ),
//            onUpdate = onUpdate,
//            onDestroy = onDestroy,
//        )
//
//        SummaryCard(
//            summary = Summary(
//                id = stringResource(R.string.items),
//                name = stringResource(R.string.items),
//                description = "number of items",
//                items = uiState.items,
//            ),
//            onUpdate = onUpdate,
//            onDestroy = onDestroy,
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth(),
//        ) {
//            Checkbox(
//                checked = uiState.items.any{ it.isFragile },
//                onCheckedChange = { },
//            )
//            Spacer(modifier = Modifier.width(4.dp))
//            Text("Fragile")
//            Spacer(modifier = Modifier.weight(1f))
//            Text(
//                text = "Total: ${uiState.items.sumOf { it.value }.asCurrencyString()}",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.secondary
//            )
//        }

// TODO : FIX
//@Preview(showBackground = true)
//@Composable
//fun PreviewSummaryScreen(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val currentScreen = PackItUpRoute.SUMMARY
//    val items = localDataSource.loadItems()
//    val boxes = localDataSource.loadBoxes()
//    val collections = localDataSource.loadCollections()
//
//    val uiState = PackItUpUiState(
//        currentScreen = currentScreen,
//        items = items,
//        boxes = boxes,
//        collections = collections,
//    )
//
//    SummaryScreen(
//        uiState = uiState,
//        onCreate = { count -> Log.i("Items ", "Creating ${count} items")},
//        onDestroy = { Log.i("Items ", "Deleting ${items[0].id} items") },
//        onUpdate = { Log.i("Items ", "Updating ${items[0].id}") },
//    )
//}

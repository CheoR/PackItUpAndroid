package com.example.packitupandroid.ui.screens.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData

@Composable
fun <T: BaseCardData> CollectionsScreen(
    uiState: PackItUpUiState,
    onCreate: (T, Int) -> Unit,
    onDestroy: (T) -> Unit,
    onUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Row {
            Text(text = "Collection Card Space Holder")
        }
    }
}

// TODO: Fix
//@Preview(showBackground = true)
//@Composable
//fun PreviewCollectionsScreen(
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
//    CollectionsScreen(
//        uiState = uiState,
//        onCreate = { collection, count -> },
//        onDestroy = {},
//        onUpdate = {},
//    )
//}

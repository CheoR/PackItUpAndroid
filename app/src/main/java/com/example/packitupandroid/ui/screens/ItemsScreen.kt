package com.example.packitupandroid.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.CardType

@Composable
fun ItemsScreen(
    uiState: PackItUpUiState,
    onCreate: (BaseCardData, Int?) -> Unit,
    onDestroy: (BaseCardData) -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Screen(
        modifier = modifier,
        elements = uiState.items,
        card = { data, update, destroy ->
            val mutableStateData = remember {
                mutableStateOf<BaseCardData>(data)
            }
//            BaseCard(
//                data = data,
//                onUpdate = { baseCardData -> update(baseCardData as Item) },
//                onDestroy = { destroy(data) },
//                cardType = CardType.Item,
//            )
            BaseCard(
                data = mutableStateData as MutableState<BaseCardData>,
                onUpdate = { updatedData ->
                    mutableStateData.value = updatedData
//                           mutableStateData.value = updatedData as Item
                    update(updatedData as Item)
                },
                onDestroy = { destroy(data) },
                cardType = CardType.Item,
            )
        },
        onClick = onCreate,
        updateElement = onUpdate,
        destroyElement = onDestroy,
    )
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewItemsScreen(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val currentScreen = ScreenType.Summary
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
//    ItemsScreen(
//        uiState = uiState,
//        onCreate = { item , count -> Log.i("Items ", "Creating ${count} items")},
//        onDestroy = { Log.i("Items ", "Deleting ${items[0].id} items") },
//        onUpdate = { Log.i("Items ", "Updating ${items[0].id}") },
//    )
//}

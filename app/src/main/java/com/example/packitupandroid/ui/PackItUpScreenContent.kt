package com.example.packitupandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.ui.screens.SummaryScreen

//@Composable
//fun PackItUpListOnlyContent(
//    modifier: Modifier = Modifier,
//    uiState: PackItUpUiState,
//    onClick: () -> Unit,
//) {
//    Column() {
//        SummaryScreen(
//            uiState = uiState,
//            onClick = onClick,
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewPackItUpListOnlyContent(
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
//    PackItUpListOnlyContent(
//        uiState = uiState,
//        onClick = {},
//    )
//}
package com.example.packitupandroid.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.ui.utils.PackItUpContentType
import com.example.packitupandroid.ui.utils.PackItUpNavigationType

//@Composable
//fun PackItUpScreen(
//    navigationType: PackItUpNavigationType,
//    contentType: PackItUpContentType,
//    uiState: PackItUpUiState,
//    onTabPressed: (ScreenType) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val navigationItemContentList = listOf(
//        NavigationItemContent(
//            screenType = ScreenType.Summary,
//            icon = Icons.Default.Home,
//            text = stringResource(id = R.string.summary)
//        ),
//        NavigationItemContent(
//            screenType = ScreenType.Collections,
//            icon = ImageVector.vectorResource(R.drawable.baseline_category_24),
//            text = stringResource(id = R.string.collections)
//        ),
//        NavigationItemContent(
//            screenType = ScreenType.Boxes,
//            icon = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
//            text = stringResource(id = R.string.boxes)
//        ),
//        NavigationItemContent(
//            screenType = ScreenType.Items,
//            icon = ImageVector.vectorResource(R.drawable.baseline_label_24),
//            text = stringResource(id = R.string.items)
//        ),
//    )
//
//    PackItUpAppContent(
//        navigationType = navigationType,
//        contentType = contentType,
//        uiState = uiState,
//        onTabPressed = onTabPressed,
//        navigationItemContentList = navigationItemContentList,
//        modifier = modifier
//    )
//}
//
//@Composable
//private fun PackItUpAppContent(
//    navigationType: PackItUpNavigationType,
//    contentType: PackItUpContentType,
//    uiState: PackItUpUiState,
//    onTabPressed: ((ScreenType) -> Unit),
//    navigationItemContentList: List<NavigationItemContent>,
//    modifier: Modifier = Modifier,
//) {
//    Box(modifier = modifier) {
//        Row(modifier = Modifier.fillMaxSize()) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.inverseOnSurface)
//            ) {
//
//                PackItUpListOnlyContent(
//                    modifier = Modifier.weight(1f)
//                        .padding(
//                            horizontal = dimensionResource(R.dimen.padding_small)
//                        ),
//                    uiState =  uiState,
//                    onClick = {},
//                )
//
//                AnimatedVisibility(visible = navigationType == PackItUpNavigationType.BOTTOM_NAVIGATION) {
//                    val bottomNavigationContentDescription = stringResource(R.string.navigation_bottom)
//                    PackItUpBottomNavigationBar(
//                        currentTab = uiState.currentScreen,
//                        onTabPressed = onTabPressed,
//                        navigationItemContentList = navigationItemContentList,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .testTag(bottomNavigationContentDescription),
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun PackItUpBottomNavigationBar(
//    currentTab: ScreenType,
//    onTabPressed: ((ScreenType) -> Unit),
//    navigationItemContentList: List<NavigationItemContent>,
//    modifier: Modifier = Modifier
//) {
//    NavigationBar(modifier = modifier) {
//        for (navItem in navigationItemContentList) {
//            NavigationBarItem(
//                selected = currentTab == navItem.screenType,
//                onClick = { onTabPressed(navItem.screenType) },
//                icon = {
//                    BadgedBox(
//                        modifier = Modifier,
//                        badge = {
//                            Badge(
//                                modifier = Modifier
//                            ) {
//                                Text("0")
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = navItem.icon,
//                            contentDescription = navItem.text
//                        )
//                    }
//                }
//            )
//        }
//    }
//}
//private data class NavigationItemContent(
//    val screenType: ScreenType,
//    val icon: ImageVector,
//    val text: String
//)
//
//
//@Preview
//@Composable
//fun PreviewSummaryScreen(
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
//    PackItUpScreen(
//        navigationType = PackItUpNavigationType.BOTTOM_NAVIGATION,
//        contentType = PackItUpContentType.LIST_ONLY,
//        uiState = uiState,
//        onTabPressed = {},
//        modifier = Modifier,
//    )
//}

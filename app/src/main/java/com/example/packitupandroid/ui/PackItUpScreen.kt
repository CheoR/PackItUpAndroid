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
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.WebStories
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.R
import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.ui.utils.PackItUpContentType
import com.example.packitupandroid.ui.utils.PackItUpNavigationType

@Composable
fun PackItUpScreen(
    navigationType: PackItUpNavigationType,
    contentType: PackItUpContentType,
    packItUpUiState: PackItUpUiState,
    onTabPressed: (ScreenType) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            screenType = ScreenType.Summary,
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.summary)
        ),
        NavigationItemContent(
            screenType = ScreenType.Collections,
            icon = Icons.Default.Collections,
            text = stringResource(id = R.string.collections)
        ),
        NavigationItemContent(
            screenType = ScreenType.Boxes,
            icon = Icons.Default.Inventory2,
            text = stringResource(id = R.string.boxes)
        ),
        NavigationItemContent(
            screenType = ScreenType.Items,
            icon = Icons.Default.WebStories,
            text = stringResource(id = R.string.items)
        ),
    )

    PackItUpAppContent(
        navigationType = navigationType,
        contentType = contentType,
        packItUpUiState = packItUpUiState,
        onTabPressed = onTabPressed,
        navigationItemContentList = navigationItemContentList,
        modifier = modifier
    )
}

@Composable
private fun PackItUpAppContent(
    navigationType: PackItUpNavigationType,
    contentType: PackItUpContentType,
    packItUpUiState: PackItUpUiState,
    onTabPressed: ((ScreenType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {

                PackItUpListOnlyContent(
                    modifier = Modifier.weight(1f)
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_small)
                        )
                )

                AnimatedVisibility(visible = navigationType == PackItUpNavigationType.BOTTOM_NAVIGATION) {
                    val bottomNavigationContentDescription = stringResource(R.string.navigation_bottom)
                    PackItUpBottomNavigationBar(
                        currentTab = packItUpUiState.currentScreen,
                        onTabPressed = onTabPressed,
                        navigationItemContentList = navigationItemContentList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(bottomNavigationContentDescription),
                    )
                }
            }
        }
    }
}

@Composable
private fun PackItUpBottomNavigationBar(
    currentTab: ScreenType,
    onTabPressed: ((ScreenType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.screenType,
                onClick = { onTabPressed(navItem.screenType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}
private data class NavigationItemContent(
    val screenType: ScreenType,
    val icon: ImageVector,
    val text: String
)


@Preview
@Composable
fun PreviewSummaryScreen() {
    val viewModel: PackItUpViewModel = viewModel()
    val packItUpUiState = viewModel.uiState.collectAsState().value

    PackItUpScreen(
        navigationType = PackItUpNavigationType.BOTTOM_NAVIGATION,
        contentType = PackItUpContentType.LIST_ONLY,
        packItUpUiState = packItUpUiState,
        onTabPressed = {},
        modifier = Modifier,
    )
}

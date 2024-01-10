package com.example.packitupandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.packitupandroid.ui.navigation.PackItUpBottomNavigationBar
import com.example.packitupandroid.ui.navigation.PackItUpNavigationActions
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.ui.navigation.PackItUpTopLevelDestination
import com.example.packitupandroid.ui.screens.BoxesScreen
import com.example.packitupandroid.ui.screens.CollectionsScreen
import com.example.packitupandroid.ui.screens.ItemsScreen
import com.example.packitupandroid.ui.screens.SummaryScreen
import com.example.packitupandroid.ui.utils.PackItUpContentType
import com.example.packitupandroid.ui.utils.PackItUpNavigationType

@Composable
fun PackItUpApp(
    windowSize: WindowSizeClass,
//    packItUpUiState: PackItUpUiState,,
) {

    /*
        To implement navigation drawer, determine navigation type based on app's window size.
     */
    val navigationType: PackItUpNavigationType

    /*
        For various window sizes to help determine appropriate content type selection,
        depending on screen size
     */
    val contentType: PackItUpContentType

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = PackItUpNavigationType.BOTTOM_NAVIGATION
            contentType = PackItUpContentType.LIST_ONLY
        }
        else -> {
            navigationType = PackItUpNavigationType.BOTTOM_NAVIGATION
            contentType = PackItUpContentType.LIST_ONLY
        }
    }

    PackItUpNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
//        packItUpUiState = packItUpUiState,
    )
}

@Composable
private fun PackItUpNavigationWrapper(
    navigationType: PackItUpNavigationType,
    contentType: PackItUpContentType,
//    packItUpUiState: PackItUpUiState,
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        PackItUpNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: PackItUpRoute.SUMMARY

    PackItUpContent(
        navigationType = navigationType,
        contentType = contentType,
//        packItUpUiState = packItUpUiState,
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo,
    )
}

@Composable
fun PackItUpContent(
    modifier: Modifier = Modifier,
    navigationType: PackItUpNavigationType,
    contentType: PackItUpContentType,
//    packItUpUiState: PackItUpUiState,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
//    navigateToDetail: (Long, PackItUpContentType) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        PackItUpNavHost(
            navController = navController,
            contentType = contentType,
//            packItUpUiState = packItUpUiState,
            navigationType = navigationType,
//    navigateToDetail =  (Long, PackItUpNavigationType) -> Unit,
            modifier = Modifier.weight(1f)
        )
        PackItUpBottomNavigationBar(
            selectedDestination = selectedDestination,
            navigateToTopLevelDestination = navigateToTopLevelDestination
        )
    }

}

@Composable
private fun PackItUpNavHost(
    navController: NavHostController,
    contentType: PackItUpContentType,
//    packItUpUiState: PackItUpUiState,
    navigationType: PackItUpNavigationType,
//  navigateToDetail: (Long, PackItUpNavigationType) -> Unit,
    modifier: Modifier,
) {

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        navController = navController,
        startDestination = PackItUpRoute.SUMMARY
    ) {
        composable(PackItUpRoute.SUMMARY) {
            SummaryScreen()
        }
        composable(PackItUpRoute.COLLECTIONS) {
            CollectionsScreen()
        }
        composable(PackItUpRoute.BOXES) {
            BoxesScreen()
        }
        composable(PackItUpRoute.ITEMS) {
            ItemsScreen()
        }
    }
}

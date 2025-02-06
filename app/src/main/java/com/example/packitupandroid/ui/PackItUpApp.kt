package com.example.packitupandroid.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.packitupandroid.ui.navigation.AppNavHost
import com.example.packitupandroid.ui.navigation.PackItUpNavigationActions
import com.example.packitupandroid.ui.navigation.Route
import com.example.packitupandroid.ui.navigation.TopLevelDestination
import com.example.packitupandroid.utils.PackItUpContentType
import com.example.packitupandroid.utils.PackItUpNavigationType


@Composable
fun PackItUpApp(
    windowSize: WindowSizeClass,
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
    )
}


@Composable
private fun PackItUpNavigationWrapper(
    navigationType: PackItUpNavigationType,
    contentType: PackItUpContentType,
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        PackItUpNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: Route.SUMMARY

    PackItUpContent(
        navigationType = navigationType,
        contentType = contentType,
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo,
    )
}


@Composable
fun PackItUpContent(
    navigationType: PackItUpNavigationType,
    contentType: PackItUpContentType,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
) {
    AppNavHost(
        navController = navController,
        contentType = contentType,
        navigationType = navigationType,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigateToTopLevelDestination,
    )
}

package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.ScreenViewModel
import com.example.packitupandroid.ui.screens.box.BoxesScreen
import com.example.packitupandroid.ui.screens.collection.CollectionsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.summary.SummaryScreen
import com.example.packitupandroid.utils.PackItUpContentType
import com.example.packitupandroid.utils.PackItUpNavigationType

@Composable
fun PackItUpNavHost(
//    uiState: PackItUpUiState,
    selectedDestination: String,
    navController: NavHostController,
    contentType: PackItUpContentType,
    navigationType: PackItUpNavigationType,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PackItUpAppBar(
                title = when (navController.currentBackStackEntry?.destination?.route) {
                    PackItUpRoute.SUMMARY -> stringResource(R.string.summary)
                    PackItUpRoute.COLLECTIONS -> stringResource(R.string.collections)
                    PackItUpRoute.BOXES -> stringResource(R.string.boxes)
                    PackItUpRoute.ITEMS -> stringResource(R.string.items)
                    else -> stringResource(R.string.app_name)
                },
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
        bottomBar = {
            PackItUpBottomNavigationBar(
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                setCurrentScreen = viewModel::setCurrentScreen,
                loadCurrentScreenData = viewModel::loadCurrentScreenData,
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = PackItUpRoute.SUMMARY,
        ) {
            composable(route = PackItUpRoute.SUMMARY) {
                SummaryScreen<Summary>()
            }
            composable(route = PackItUpRoute.COLLECTIONS) {
                CollectionsScreen<Collection>()
            }
            composable(route = PackItUpRoute.BOXES) {
                BoxesScreen<Box>()
            }
            composable(route = PackItUpRoute.ITEMS) {
                ItemsScreen<Item>()
            }
        }
    }
}

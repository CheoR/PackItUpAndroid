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
import com.example.packitupandroid.ui.PackItUpUiState
import com.example.packitupandroid.ui.PackItUpViewModel
import com.example.packitupandroid.ui.screens.BoxesScreen
import com.example.packitupandroid.ui.screens.CollectionsScreen
import com.example.packitupandroid.ui.screens.ItemsScreen
import com.example.packitupandroid.ui.screens.SummaryScreen
import com.example.packitupandroid.ui.utils.PackItUpContentType
import com.example.packitupandroid.ui.utils.PackItUpNavigationType

@Composable
fun PackItUpNavHost(
    uiState: PackItUpUiState,
    selectedDestination: String,
    navController: NavHostController,
    contentType: PackItUpContentType,
    navigationType: PackItUpNavigationType,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PackItUpViewModel = viewModel(factory = PackItUpViewModel.Factory),
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
                navigateToTopLevelDestination = navigateToTopLevelDestination
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
                SummaryScreen(
                    uiState = uiState,
                    onCreate = {},
                    onDelete = {},
                    onUpdate = {},
                )
            }
            composable(route = PackItUpRoute.COLLECTIONS) {
                CollectionsScreen(
                    uiState = uiState,
                    onCreate = viewModel::createCollection,
                    onDelete = viewModel::deleteCollection,
                    onUpdate = viewModel::updateElement,
                )
            }
            composable(route = PackItUpRoute.BOXES) {
                BoxesScreen(
                    uiState = uiState,
                    onCreate = viewModel::createBox,
                    onDelete = viewModel::deleteBox,
                    onUpdate = viewModel::updateElement,
                )
            }
            composable(route = PackItUpRoute.ITEMS) {
                ItemsScreen(
                    uiState = uiState,
                    onCreate = viewModel::createItem,
                    onDelete = viewModel::deleteItem,
                    onUpdate = viewModel::updateElement,
                )
            }
        }
    }
}

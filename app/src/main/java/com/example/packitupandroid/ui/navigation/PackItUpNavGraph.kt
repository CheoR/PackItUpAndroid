package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
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
    navController: NavHostController,
    contentType: PackItUpContentType,
    uiState: PackItUpUiState,
    navigationType: PackItUpNavigationType,
    modifier: Modifier,
    viewModel : PackItUpViewModel = viewModel(factory = PackItUpViewModel.Factory),
) {
    NavHost(
        modifier = modifier
            .padding(
                start = dimensionResource(R.dimen.padding_small),
                end = dimensionResource(R.dimen.padding_small),
            ),
        navController = navController,
        startDestination = PackItUpRoute.SUMMARY
    ) {
        composable(route = PackItUpRoute.SUMMARY) {
            SummaryScreen(
                uiState = uiState,
                onClick = viewModel::resetItemList, // TODO: remove when no longer needed
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                canNavigateBack = false,
            )
        }
        composable(route = PackItUpRoute.COLLECTIONS) {
            CollectionsScreen(
                uiState = uiState,
                onCreateClick = viewModel::createCollection,
                onDeleteClick = viewModel::deleteCollection,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(route = PackItUpRoute.BOXES) {
            BoxesScreen(
                uiState = uiState,
                onCreateClick = viewModel::createBox,
                onDeleteClick = viewModel::deleteBox,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(route = PackItUpRoute.ITEMS) {
            ItemsScreen(
                uiState = uiState,
                onCreateClick = viewModel::createItem,
                onDeleteClick = viewModel::deleteItem,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}

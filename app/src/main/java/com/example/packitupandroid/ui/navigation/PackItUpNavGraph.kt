package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
//  navigateToDetail: (Long, PackItUpNavigationType) -> Unit,
    modifier: Modifier,
    viewModel : PackItUpViewModel = viewModel(factory = PackItUpViewModel.Factory),
) {
    NavHost(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        navController = navController,
        startDestination = PackItUpRoute.SUMMARY
    ) {
        composable(PackItUpRoute.SUMMARY) {
            SummaryScreen(
                uiState = uiState,
                onClick = viewModel::resetItemList, // TODO: remove when no longer needed
            )
        }
        composable(PackItUpRoute.COLLECTIONS) {
            CollectionsScreen(
                uiState = uiState,
                onCreateClick = viewModel::createCollection,
                onDeleteClick = viewModel::deleteCollection,
            )
        }
        composable(PackItUpRoute.BOXES) {
            BoxesScreen(
                uiState = uiState,
                onCreateClick = viewModel::createBox,
                onDeleteClick = viewModel::deleteBox,
            )
        }
        composable(PackItUpRoute.ITEMS) {
            ItemsScreen(
                uiState = uiState,
                onCreateClick = viewModel::createItem,
                onDeleteClick = viewModel::deleteItem,
            )
        }
    }
}

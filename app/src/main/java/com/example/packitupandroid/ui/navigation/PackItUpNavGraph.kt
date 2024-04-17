package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.ScreenViewModel
import com.example.packitupandroid.ui.screens.box.BoxesScreen
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
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
                SummaryScreen<Summary>(
//                    uiState = uiState,
//                    onCreate = viewModel::createElement,
//                    onDestroy = viewModel::destroyElement,
//                    onUpdate = viewModel::updateElement,
                )
            }
//            composable(route = PackItUpRoute.COLLECTIONS) {
//                CollectionsScreen(
//                    uiState = uiState,
//                    onCreate = viewModel::createElement,
//                    onDestroy = viewModel::destroyElement,
//                    onUpdate = viewModel::updateElement,
//                )
//            }
            composable(route = PackItUpRoute.BOXES) {
                val vm: BoxesScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory)
                val uiState by viewModel.uiState.collectAsState()

                BoxesScreen(
                    uiState = uiState,
                    createElement = vm::create,
                    updateElement = vm::update,
                    destroyElement = vm::destroy,
                )
            }
            composable(route = PackItUpRoute.ITEMS) {
                val vm: ItemsScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory)
                val uiState by viewModel.uiState.collectAsState()

                ItemsScreen(
                    uiState = uiState,
                    createElement = vm::create,
                    updateElement = vm::update,
                    destroyElement = vm::destroy,
                )
            }
        }
    }
}

package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.ScreenViewModel
import com.example.packitupandroid.ui.screens.box.BoxesScreen
import com.example.packitupandroid.ui.screens.collection.CollectionsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.summary.SummaryScreen
import com.example.packitupandroid.utils.PackItUpContentType
import com.example.packitupandroid.utils.PackItUpNavigationType
import kotlinx.coroutines.launch

@Composable
fun PackItUpNavHost(
//    uiState: PackItUpUiState,
    selectedDestination: String,
    navController: NavHostController,
    contentType: PackItUpContentType,
    navigationType: PackItUpNavigationType,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory)
) {
    val navGraphState = viewModel.navGraphState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                navGraphState = navGraphState,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                launchSnackBar = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            // TODO - fix with stringResource and maybe LaunchEffect
                            message = "Create $it first",
                        )
                    }
                },
                toggleScreenSnackbar = viewModel::toggleScreenSnackbar,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        NavHost(
            modifier = modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = PackItUpRoute.SUMMARY,
        ) {
            composable(route = PackItUpRoute.SUMMARY) {
                SummaryScreen(
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
            composable(route = PackItUpRoute.COLLECTIONS) {
                CollectionsScreen(
                    filterBoxesByCollectionId = { id ->
                        navController.navigate("${PackItUpRoute.BOXES}/$id")
                    }
                )
            }
            composable(route = PackItUpRoute.BOXES) {
                BoxesScreen(
                    getDropdownOptions = viewModel::getCollectionDropdownOptions,
                )
            }
            composable(route = "${PackItUpRoute.BOXES}/{collectionId}") {
                BoxesScreen(
                    getDropdownOptions = viewModel::getCollectionDropdownOptions,
                )
            }
            composable(route = PackItUpRoute.ITEMS) {
                ItemsScreen(
                    getDropdownOptions = viewModel::getBoxDropdownOptions,
                )
            }
        }
    }
}

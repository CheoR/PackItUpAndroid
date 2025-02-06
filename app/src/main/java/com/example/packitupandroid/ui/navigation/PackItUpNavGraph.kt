package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.box.BoxesScreen
import com.example.packitupandroid.ui.screens.collection.CollectionsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.summary.SummaryScreen
import com.example.packitupandroid.utils.PackItUpContentType
import com.example.packitupandroid.utils.PackItUpNavigationType
import kotlinx.coroutines.launch


/**
 * Composable function that sets up the navigation host for the MoveHaul application.
 * This function uses a `NavController` to manage navigation between different screens.
 * It includes a `Scaffold` with a top app bar, bottom navigation bar, and a snackbar host.
 *
 * @param selectedDestination The currently selected destination PackItUpRoute.
 * @param navController The NavController used for navigation.
 * @param navigateToTopLevelDestination Lambda function to navigate to a top-level destination.
 * @param modifier Modifier to be applied to the navigation host.
 */
@Composable
fun AppNavHost(
    selectedDestination: String,
    contentType: PackItUpContentType,
    navigationType: PackItUpNavigationType,
    navController: NavHostController,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NavHostViewModel = viewModel(factory = PackItUpViewModelProvider.Factory)
) {
    val navHostState by viewModel.navHoseState.collectAsState()
    val toggleScreenSnackbar = viewModel::toggleScreenSnackbar
    val setTitle = viewModel::setTitle

    AppNavHost(
        modifier,
        navHostState,
        navController,
        toggleScreenSnackbar,
        setTitle,
        selectedDestination,
        navigateToTopLevelDestination,
    )
}

/**
 * Composable function that sets up the navigation host and UI structure for the MoveHaul application.
 *
 * This function utilizes a [Scaffold] to arrange the app's layout, including a top bar,
 * bottom navigation bar, and a [AppNavHost] for handling navigation between different screens.
 *
 * @param modifier Modifier to apply to the overall layout.
 * @param navHostState State holder for the navigation host.
 * @param navController Controller for navigation within the app.
 * @param toggleScreenSnackbar Callback to trigger a snackbar with a specific PackItUpRoute.
 * @param setTitle Callback to set the title of the screen.
 * @param selectedDestination Currently selected top-level destination.
 * @param navigateToTopLevelDestination Callback to navigate to a top-level destination.
 */
@Composable
private fun AppNavHost(
    modifier: Modifier,
    navHostState: NavHostState,
    navController: NavHostController,
    toggleScreenSnackbar: (route: String) -> Unit,
    setTitle: (title: String?, addBox: Boolean, id: String?) -> Unit,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val backStackEntry = navController.currentBackStackEntry

    // TODO: ref - pretty ugly :(
    when {
        selectedDestination.startsWith("${Route.ADD_BOXES}/") -> {
            val id = backStackEntry?.arguments?.getString("collectionId")
            if (id != null) {
                setTitle(null, true, id)
            }
        }
        selectedDestination.startsWith("${Route.ADD_ITEMS}/") -> {
            val id = backStackEntry?.arguments?.getString("boxId")
            if (id != null) {
                setTitle(null, false, id)
            }
        }
        else -> setTitle(selectedDestination, false, null)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                title = navHostState.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navHostState = navHostState,
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
                toggleScreenSnackbar = toggleScreenSnackbar,
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
            startDestination = Route.SUMMARY,
        ) {
            composable(route = Route.SUMMARY) {
                SummaryScreen(
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
            composable(route = Route.COLLECTIONS) {
                CollectionsScreen(
                    addElements = { id ->
                        navController.navigate("${Route.ADD_BOXES}/$id")
                    }
                )
            }
            composable(route = Route.BOXES) {
                BoxesScreen(
                    addElements = { id ->
                        navController.navigate("${Route.ADD_ITEMS}/$id")
                    }
                )
            }
            composable(route = "${Route.ADD_BOXES}/{collectionId}") {
                BoxesScreen()
            }
            composable(route = Route.ITEMS) {
                ItemsScreen()
            }
            composable(route = "${Route.ADD_ITEMS}/{boxId}") {
                ItemsScreen()
            }
        }
    }
}

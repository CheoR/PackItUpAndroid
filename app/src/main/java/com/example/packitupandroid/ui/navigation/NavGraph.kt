package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.packitupandroid.ui.ViewModelProvider
import com.example.packitupandroid.ui.screens.box.BoxesScreen
import com.example.packitupandroid.ui.screens.collection.CollectionsScreen
import com.example.packitupandroid.ui.screens.item.ItemsScreen
import com.example.packitupandroid.ui.screens.settings.SettingsScreen
import com.example.packitupandroid.ui.screens.summary.SummaryScreen
import com.example.packitupandroid.utils.ContentType
import com.example.packitupandroid.utils.NavigationType
import kotlinx.coroutines.launch


/**
 * Composable function that sets up the navigation host for the PackItUp application.
 * This function uses a `NavController` to manage navigation between different screens.
 * It includes a `Scaffold` with a top app bar, bottom navigation bar, and a snackbar host.
 *
 * @param selectedDestination The currently selected destination Route.
 * @param navController The NavController used for navigation.
 * @param navigateToTopLevelDestination Lambda function to navigate to a top-level destination.
 * @param modifier Modifier to be applied to the navigation host.
 */
@Composable
fun AppNavHost(
    selectedDestination: String,
    contentType: ContentType,
    navigationType: NavigationType,
    navController: NavHostController,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NavHostViewModel = viewModel(factory = ViewModelProvider.Factory)
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
 * Composable function that sets up the navigation host and UI structure for the PackItUp application.
 *
 * This function utilizes a [Scaffold] to arrange the app's layout, including a top bar,
 * bottom navigation bar, and a [AppNavHost] for handling navigation between different screens.
 *
 * @param modifier Modifier to apply to the overall layout.
 * @param navHostState State holder for the navigation host.
 * @param navController Controller for navigation within the app.
 * @param toggleScreenSnackbar Callback to trigger a snackbar with a specific Route.
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
    val scaffoldState = rememberBottomSheetScaffoldState()

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

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        topBar = {
            AppBar(
                title = navHostState.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        sheetContent = {
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
        sheetPeekHeight = 50.dp,
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
                    snackbarHostState = snackbarHostState,
                    coroutineScope = scope,
                    addElements = { id ->
                        navController.navigate("${Route.ADD_BOXES}/$id")
                    }
                )
            }
            composable(route = Route.BOXES) {
                BoxesScreen(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = scope,
                    addElements = { id ->
                        navController.navigate("${Route.ADD_ITEMS}/$id")
                    }
                )
            }
            composable(route = "${Route.ADD_BOXES}/{collectionId}") {
                BoxesScreen(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = scope,
                )
            }
            composable(route = Route.ITEMS) {
                ItemsScreen(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = scope,
                )
            }
            composable(route = "${Route.ADD_ITEMS}/{boxId}") {
                ItemsScreen(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = scope,
                )
            }
            composable(route = Route.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}

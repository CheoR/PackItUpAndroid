package com.example.packitupandroid.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.common.card.elements.IconImage


/**
 * Composable function that creates a bottom navigation bar for the application.
 *
 * Displays a bottom navigation bar with icons and labels for each top-level
 * destination. The enabled state of each destination is determined by the
 * [NavHostState].
 *
 * @param navHostState The state of the navigation host, which includes information about navigation destinations and their enabled state.
 * @param selectedDestination The currently selected destination route.
 * @param navigateToTopLevelDestination Lambda function to navigate to a top-level destination.
 * @param modifier Modifier to be applied to the navigation bar.
 * @param launchSnackBar Lambda function to launch a snackbar with a message.
 * @param toggleScreenSnackbar Lambda function to toggle the snackbar for a specific route.
 */
@Composable
fun BottomNavigationBar(
    navHostState: NavHostState,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    launchSnackBar: (instance: String) -> Unit,
    toggleScreenSnackbar: (route: String) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { Destination ->

            val selectedIcon = if (selectedDestination == Destination.route) Destination.selectedIcon else Destination.unselectedIcon
            val contentDescription = stringResource(Destination.iconTextId)
            val tintIcon = selectedDestination == Destination.route
            val destinationRoute = Destination.route
            val color = if (selectedDestination == destinationRoute) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

            val isEnabled = when(Destination.route) { // TODO: fix this
                Route.BOXES -> navHostState.showBoxesScreenSnackBar || navHostState.canNavigateToBoxesScreen
                Route.ITEMS -> navHostState.showItemsScreenSnackBar || navHostState.canNavigateToItemsScreen
                else -> true
            }

            NavigationBarItem(
                modifier = Modifier,
                selected = selectedDestination == destinationRoute,
                enabled = isEnabled,
                onClick = {
                    when(destinationRoute) {
                        Route.BOXES -> {
                            if(navHostState.canNavigateToBoxesScreen) {
                                navigateToTopLevelDestination(Destination)
                            } else {
                                toggleScreenSnackbar(destinationRoute)
                                launchSnackBar("Collection")
                            }
                        }
                        Route.ITEMS -> {
                            if(navHostState.canNavigateToItemsScreen) {
                                navigateToTopLevelDestination(Destination)
                            } else {
                                toggleScreenSnackbar(destinationRoute)
                                launchSnackBar("Box")
                            }
                        }
                        else -> navigateToTopLevelDestination(Destination)
                    }
                },
                icon = {
                    IconImage(
                        selectedIcon = selectedIcon,
                        contentDescription = contentDescription,
                        tintIcon = tintIcon,
                    )
                },
                label = {
                    Text(
                        text = destinationRoute,
                        color = color,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            )
        }
    }
}


/**
 * Composable function that creates an app bar with a title and an optional back navigation button.
 *
 * @param title The title to display in the app bar.
 * @param canNavigateBack Boolean indicating whether the back navigation button should be displayed.
 * @param modifier Modifier to be applied to the app bar.
 * @param scrollBehavior The scroll behavior for the top app bar.
 * @param navigateUp Lambda function to handle the action when the back navigation button is clicked.
 */
@Composable
fun AppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    )
}

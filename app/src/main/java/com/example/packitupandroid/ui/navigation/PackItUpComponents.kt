package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.screens.NavGraphState

@Composable
fun PackItUpBottomNavigationBar(
    navGraphState: NavGraphState,
    selectedDestination: String,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    launchSnackBar: (instance: String) -> Unit,
    toggleScreenSnackbar: (route: String) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { packItUpDestination ->
            val selectedIcon = when(packItUpDestination.selectedIcon) {
                is ImageVector -> packItUpDestination.selectedIcon
                else -> ImageVector.vectorResource(id=packItUpDestination.selectedIcon as Int)
            }
            val isEnabled = when(packItUpDestination.route) {
                PackItUpRoute.BOXES -> navGraphState.showBoxesScreenSnackBar || navGraphState.canNavigateToBoxesScreen
                PackItUpRoute.ITEMS -> navGraphState.showItemsScreenSnackBar || navGraphState.canNavigateToItemsScreen
                else -> true
            }

            NavigationBarItem(
                modifier = Modifier,
//                    .height(dimensionResource(R.dimen.navigation_bar_item_height))
                selected = selectedDestination == packItUpDestination.route,
                onClick = {
                    when(packItUpDestination.route) {
                        PackItUpRoute.BOXES -> {
                            if(navGraphState.canNavigateToBoxesScreen) {
                                navigateToTopLevelDestination(packItUpDestination)
                            } else {
                                toggleScreenSnackbar(packItUpDestination.route)
                                // TODO - fix with stringResource
                                launchSnackBar("Collection")
                            }
                        }
                        PackItUpRoute.ITEMS -> {
                            if(navGraphState.canNavigateToItemsScreen) {
                                navigateToTopLevelDestination(packItUpDestination)
                            } else {
                                toggleScreenSnackbar(packItUpDestination.route)
                                // TODO - fix with stringResource
                                launchSnackBar("Box")
                            }
                        }
                        else -> navigateToTopLevelDestination(packItUpDestination)
                    }
                },
                enabled = isEnabled,
                icon = {
                    Icon(
                        imageVector = selectedIcon,
                        contentDescription = stringResource(packItUpDestination.iconTextId),
                        modifier = Modifier.size(40.dp),
                        tint = if (selectedDestination == packItUpDestination.route) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                label = {
                    Text(
                        text = packItUpDestination.route,
                        color = if (selectedDestination == packItUpDestination.route) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                    )
                }
            )
        }
    }
}

/**
 * App bar display title and conditionally display back navigation.
 */
@Composable
fun PackItUpAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

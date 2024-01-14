package com.example.packitupandroid.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.packitupandroid.R

object PackItUpRoute {
    const val SUMMARY = "Summary"
    const val COLLECTIONS = "Collections"
    const val BOXES = "Boxes"
    const val ITEMS = "Items"
}

data class PackItUpTopLevelDestination(
    val route: String,
    val selectedIcon: Any, // ImageVector
    val unselectedIcon: Any, // ImageVector,
    val iconTextId: Int,
)

class PackItUpNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: PackItUpTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    PackItUpTopLevelDestination(
        route = PackItUpRoute.SUMMARY,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = R.string.summary
    ),
    PackItUpTopLevelDestination(
        route = PackItUpRoute.COLLECTIONS,
        selectedIcon = Icons.Default.Category,
        unselectedIcon = Icons.Default.Category,
        iconTextId = R.string.collections
    ),
    PackItUpTopLevelDestination(
        route = PackItUpRoute.BOXES,
        selectedIcon = R.drawable.ic_launcher_foreground,
        unselectedIcon = Icons.Outlined.CheckBoxOutlineBlank,
        iconTextId = R.string.boxes
    ),
    PackItUpTopLevelDestination(
        route = PackItUpRoute.ITEMS,
        selectedIcon = Icons.Default.Label,
        unselectedIcon = Icons.Default.Label,
        iconTextId = R.string.items
    )
)



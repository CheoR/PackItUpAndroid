package com.example.packitupandroid.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.ui.graphics.vector.ImageVector
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
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
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
        selectedIcon = Icons.Default.Inbox,
        unselectedIcon = Icons.Default.Inbox,
        iconTextId = R.string.summary
    ),
    PackItUpTopLevelDestination(
        route = PackItUpRoute.COLLECTIONS,
        selectedIcon = Icons.Default.Article,
        unselectedIcon = Icons.Default.Article,
        iconTextId = R.string.collections
    ),
    PackItUpTopLevelDestination(
        route = PackItUpRoute.BOXES,
        selectedIcon = Icons.Outlined.ChatBubbleOutline,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        iconTextId = R.string.boxes
    ),
    PackItUpTopLevelDestination(
        route = PackItUpRoute.ITEMS,
        selectedIcon = Icons.Default.People,
        unselectedIcon = Icons.Default.People,
        iconTextId = R.string.items
    )
)



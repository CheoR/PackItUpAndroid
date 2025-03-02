package com.example.packitupandroid.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.common.card.elements.ImageContent


/**
 * Object that defines constant routes used for navigation in the application.
 */
object Route {
    /**
     * Route for the summary screen.
     */
    const val SUMMARY = "Summary"

    /**
     * Route for the collections screen.
     */
    const val COLLECTIONS = "Collections"

    /**
     * Route for the boxes screen.
     */
    const val BOXES = "Boxes"

    /**
     * Route for the items screen.
     */
    const val ITEMS = "Items"

    /**
     * Route for adding Items to Box, screen.
     */
    const val ADD_ITEMS = "AddItems"

    /**
     * Route for adding Boxes to Collection, screen.
     */
    const val ADD_BOXES = "AddBoxes"

    /**
     * Route for the settings screen.
     */
    const val SETTINGS = "Settings"
}

/**
 * Data class representing a top-level destination in the navigation graph.
 *
 * @property route The navigation route for the destination.
 * @property selectedIcon The [ImageContent] icon to display when the destination is selected.
 * @property unselectedIcon The [ImageContent] icon to display when the destination is not selected.
 * @property iconTextId The resource ID for the text associated with the icon.
 */
data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageContent,
    val unselectedIcon: ImageContent,
    val iconTextId: Int,
)

class NavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: TopLevelDestination) {
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
    TopLevelDestination(
        route = Route.SUMMARY,
        selectedIcon = ImageContent.VectorImage(Icons.Default.Home),
        unselectedIcon = ImageContent.VectorImage(Icons.Default.Home),
        iconTextId = R.string.summary
    ),
    TopLevelDestination(
        route = Route.COLLECTIONS,
        selectedIcon = ImageContent.VectorImage(Icons.Default.Category),
        unselectedIcon = ImageContent.VectorImage(Icons.Default.Category),
        iconTextId = R.string.collections,
    ),
    TopLevelDestination(
        route = Route.BOXES,
        selectedIcon = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
        unselectedIcon = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
        iconTextId = R.string.boxes
    ),
    TopLevelDestination(
        route = Route.ITEMS,
        selectedIcon = ImageContent.VectorImage(Icons.AutoMirrored.Default.Label),
        unselectedIcon = ImageContent.VectorImage(Icons.AutoMirrored.Default.Label),
        iconTextId = R.string.items
    ),
    TopLevelDestination(
        route = Route.SETTINGS,
        selectedIcon = ImageContent.VectorImage(Icons.Filled.Settings),
        unselectedIcon = ImageContent.VectorImage(Icons.Filled.Settings),
        iconTextId = R.string.settings,
    ),
)



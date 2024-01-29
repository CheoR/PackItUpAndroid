package com.example.packitupandroid.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun PackItUpBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier
            .height(80.dp),
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { packItUpDestination ->
            val selectedIcon = when(packItUpDestination.selectedIcon) {
                is ImageVector -> packItUpDestination.selectedIcon
                else -> ImageVector.vectorResource(id=packItUpDestination.selectedIcon as Int)
            }
            NavigationBarItem(
                modifier = Modifier.height(32.dp),
                selected = selectedDestination == packItUpDestination.route,
                onClick = { navigateToTopLevelDestination(packItUpDestination) },
                icon = {
                    Icon(
                        imageVector = selectedIcon,
                        contentDescription = stringResource(id = packItUpDestination.iconTextId),
                        modifier = Modifier
                            .size(24.dp),
                    )
                }
            )
        }
    }
}

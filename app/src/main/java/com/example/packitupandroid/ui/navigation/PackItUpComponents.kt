package com.example.packitupandroid.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun PackItUpBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        TOP_LEVEL_DESTINATIONS.forEach { packItUpDestination ->
            NavigationBarItem(
                modifier = Modifier,
                selected = selectedDestination == packItUpDestination.route,
                onClick = { navigateToTopLevelDestination(packItUpDestination) },
                icon = {
                    Icon(
                        imageVector = packItUpDestination.selectedIcon,
                        contentDescription = stringResource(id = packItUpDestination.iconTextId)
                    )
                }
            )
        }
    }
}

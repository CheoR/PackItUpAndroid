package com.example.packitupandroid.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.components.card.ColumnIcon
import com.example.packitupandroid.ui.components.card.SummaryCard
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.ui.navigation.PackItUpTopLevelDestination
import com.example.packitupandroid.ui.navigation.TOP_LEVEL_DESTINATIONS
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.asCurrencyString
import com.example.packitupandroid.utils.toBoolean


/**
 * Determines the top-level destination associated with a given route.
 *
 * This function maps a string representation of a route (e.g., "collections", "boxes", "items")
 * to its corresponding [PackItUpTopLevelDestination] enum value. It utilizes a `when` statement
 * for routing based on [PackItUpRoute] constants.
 *
 * **Note:** The current implementation uses hardcoded indices into the [TOP_LEVEL_DESTINATIONS]
 * list, which is marked as a TODO and needs to be improved for better maintainability and clarity.
 *
 * @param route The string representation of the route (e.g., PackItUpRoute.COLLECTIONS).
 * @return The [PackItUpTopLevelDestination] enum value associated with the given route.
 * @throws IllegalArgumentException If the provided route is not a valid [PackItUpRoute].
 */
private fun getTopLevelDestination(route: String): PackItUpTopLevelDestination {
    // TODO - fix - this is ugly
    return when (route) {
        PackItUpRoute.COLLECTIONS -> TOP_LEVEL_DESTINATIONS[1]
        PackItUpRoute.BOXES -> TOP_LEVEL_DESTINATIONS[2]
        PackItUpRoute.ITEMS -> TOP_LEVEL_DESTINATIONS[3]
        else -> throw IllegalArgumentException("Invalid PackItUpRoute: $route")
    }
}

/**
 * Composable function for displaying the summary screen.
 *
 * This composable displays the summary of collections, boxes, and items,
 * including their counts, total value, and whether there are fragile items.
 *
 * @param viewModel The [SummaryScreenViewModel] instance to use for data.
 *                  Defaults to a new instance created using [PackItUpViewModelProvider.Factory].
 */
@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    viewModel: SummaryScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory)
) {
    val result by viewModel.elements.collectAsState()
    /**
     *  A [Summary] object that is derived from the [result] state.
     *  If the result is an error or loading, a default [Summary] object is created.
     */
    val summary = when (result) {
        is Result.Error -> Summary(0,0,0,0.0,false)
        is Result.Loading -> Summary(0,0,0,0.0,false)
        is Result.Success -> (result as Result.Success<Summary?>).data
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.space_arrangement_small)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//        Text("Total Collections: ${summary?.collectionCount}")
//        Text("Total Boxes: ${summary?.boxCount}")
//        Text("Total Items: ${summary?.itemCount}")
//        Text("Total Value: ${summary?.value}")
//        Text("Has Fragile Items: ${summary?.isFragile}")
        // TODO: on error display a message ContentMessage(text = "Error . . ")
        if (summary != null) {
            Summary(
                itemCount = summary.itemCount,
                boxCount = summary.boxCount,
                collectionCount = summary.collectionCount,
                isFragile = summary.isFragile,
                value = summary.value,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
            )
        }
    }
}

/**
 * Composable for displaying a summary of the current data.
 *
 * This composable displays a summary of collections, boxes, and items,
 * including their counts, total value, and whether there are fragile items.
 * It also provides navigation to the respective screens for collections, boxes, and items.
 *
 * @param navigateToTopLevelDestination The lambda function to navigate to a top-level destination.
 *   It takes a [PackItUpTopLevelDestination] as a parameter.
 * @param modifier The modifier to be applied to the composable.
 * @param itemCount The number of items.
 * @param boxCount The number of boxes.
 * @param collectionCount The number of collections.
 * @param isFragile Whether the items are fragile or not.
 * @param value The total value of the items.
 */
@Composable
private fun Summary(
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    itemCount: Int = 0,
    boxCount: Int = 0,
    collectionCount: Int = 0,
    isFragile: Boolean = false,
    value: Double = 0.0,
) {
    Column(modifier=modifier) {
        Column(
            modifier = modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.space_arrangement_small)
            )
        ) {
            SummaryCard(
                icon1 = ColumnIcon.VectorIcon(Icons.Default.Category),
                name = "Collection",
                description = "Made from group of Boxes",
                badgeCount1 = collectionCount,
                canNavigateToScreen = true,
                navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(PackItUpRoute.COLLECTIONS)) },
            )
            SummaryCard(
                name = "Box",
                description = "Made from group of Items",
                icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
                badgeCount1 = boxCount,
                canNavigateToScreen = boxCount.toBoolean(),
                navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(PackItUpRoute.BOXES)) }
            )
            SummaryCard(
                name = "Item",
                description = "Individual Item",
                icon1 = ColumnIcon.VectorIcon(Icons.Default.Label),
                badgeCount1 = itemCount,
                canNavigateToScreen = itemCount.toBoolean(),
                navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(PackItUpRoute.ITEMS)) }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Checkbox(
                checked = isFragile,
                onCheckedChange = {},
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Fragile")
            Spacer(modifier = Modifier.weight(1f))
            BasicTextField(
                value = "Total: ${value.asCurrencyString()}",
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodySmall,
                enabled = false,
                singleLine = true,
            )
        }
    }
}

/**
 * Displays a simple text message centered within a Column.
 *
 * This composable function takes a string as input and displays it within a centered
 * column. It's useful for showing simple messages or placeholder content.
 *
 * @param text The text content to be displayed.
 */
@Composable
private fun ContentMessage(text: String) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collections = localDataSource.loadCollections()
    val boxes = localDataSource.loadBoxes()
    val items = localDataSource.loadItems()

    Summary(
        itemCount = items.size,
        boxCount = boxes.size,
        collectionCount =  collections.size,
        isFragile = items.any { it.isFragile },
        value = items.sumOf { it.value },
        navigateToTopLevelDestination = {},
    )
}

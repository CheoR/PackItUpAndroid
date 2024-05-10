package com.example.packitupandroid.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.components.card.ColumnIcon
import com.example.packitupandroid.ui.components.card.SummaryCard
import com.example.packitupandroid.ui.components.spinner.Spinner
import com.example.packitupandroid.ui.navigation.PackItUpRoute
import com.example.packitupandroid.ui.navigation.PackItUpTopLevelDestination
import com.example.packitupandroid.ui.navigation.TOP_LEVEL_DESTINATIONS
import com.example.packitupandroid.utils.asCurrencyString
import com.example.packitupandroid.utils.toBoolean

private fun getTopLevelDestination(route: String): PackItUpTopLevelDestination {
    // TODO - fix - this is ugly
    return when (route) {
        PackItUpRoute.COLLECTIONS -> TOP_LEVEL_DESTINATIONS[1]
        PackItUpRoute.BOXES -> TOP_LEVEL_DESTINATIONS[2]
        PackItUpRoute.ITEMS -> TOP_LEVEL_DESTINATIONS[3]
        else -> throw IllegalArgumentException("Invalid PackItUpRoute: $route")
    }
}

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    navigateToTopLevelDestination: (PackItUpTopLevelDestination) -> Unit,
    viewModel: SummaryScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = modifier) {
        when (uiState.result) {
            is Result.Loading -> Spinner()
            is Result.Error -> ContentMessage(text = "Error . . ")
            is Result.Complete -> (uiState.result as Result.Complete).summary?.let {
                Summary(
                    itemCount = it.itemCount,
                    boxCount = it.boxCount,
                    collectionCount = it.collectionCount,
                    isFragile = it.isFragile,
                    value = it.value,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                )
            }
        }
    }
}

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

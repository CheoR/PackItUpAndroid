package com.example.packitupandroid.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.components.card.SummaryCard
import com.example.packitupandroid.ui.components.strategyCard.IconBadge
import com.example.packitupandroid.ui.components.strategyCard.ImageContent
import com.example.packitupandroid.ui.navigation.Route
import com.example.packitupandroid.ui.navigation.TOP_LEVEL_DESTINATIONS
import com.example.packitupandroid.ui.navigation.TopLevelDestination
import com.example.packitupandroid.utils.ActionColumnIcon
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.asCurrencyString


private fun getTopLevelDestination(route: String): TopLevelDestination {
    // TODO - fix - this is ugly
    return when (route) {
        Route.COLLECTIONS  -> TOP_LEVEL_DESTINATIONS[1]
        Route.BOXES -> TOP_LEVEL_DESTINATIONS[2]
        Route.ITEMS -> TOP_LEVEL_DESTINATIONS[3]
        else -> throw IllegalArgumentException("Invalid PackItUpRoute: $route")
    }
}

/**
 * Composable function that displays the summary screen.
 *
 * This function fetches data from a [SummaryScreenViewModel] and displays it using a [Summary] composable.
 *
 * @param viewModel The [SummaryScreenViewModel] to fetch data from. Defaults to a new instance from the MoveHaulViewModelProvider.
 */
@Composable
fun SummaryScreen(
    viewModel: SummaryScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory),
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
) {
    val result by viewModel.elements.collectAsState()

    /**
     *  A [Summary] object that is derived from the [result] state.
     *  If the result is an error or loading, a default [Summary] object is created.
     */
    val dataResult = when (result) {
        is Result.Error -> Summary(0, 0, 0, 0.0, false)
        is Result.Loading -> Summary(0, 0, 0, 0.0, false)
        is Result.Success -> (result as Result.Success<Summary?>).data
    }
    SummaryScreen(
        result = dataResult,
        navigateToTopLevelDestination = navigateToTopLevelDestination,
    )
}

/**
 * Composable function that displays a summary of data, including collections, boxes, and items counts,
 * along with a fragile indicator and a value field.
 *
 * @param result An optional [Summary] object containing the data to display. If null, default values are used.
 */
@Composable
private fun SummaryScreen(
    result: Summary? = null,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
) {
    val actionIcon : ActionColumnIcon = ActionColumnIcon.RightArrow

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.space_arrangement_small)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SummaryCard(
            name = "Collections",
            description = "Number of Collections",
            iconsContent = {
                Column {
                    IconBadge(
                        image = ImageContent.VectorImage(Icons.Default.Category),
                        badgeContentDescription = "Number of Collections",
                        badgeCount = result?.collectionCount ?: 0,
                    )
                }
            },
            actionIcon = actionIcon,
            canNavigateToScreen = true,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(Route.COLLECTIONS)) },
        )
        SummaryCard(
            name = "Boxes",
            description = "Number of Boxes",
            iconsContent = {
                Column {
                    IconBadge(
                        image = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
                        badgeContentDescription = "Number of Boxes",
                        badgeCount = result?.boxCount ?: 0,
                    )
                }
            },
            actionIcon = actionIcon,
            canNavigateToScreen = result?.boxCount != 0,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(Route.BOXES)) },
        )
        SummaryCard(
            name = "Items",
            description = "Number of Items",
            iconsContent = {
                Column {
                    IconBadge(
                        image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                        badgeContentDescription = "Number of Items",
                        badgeCount = result?.itemCount ?: 0,
                    )
                }
            },
            actionIcon = actionIcon,
            canNavigateToScreen = result?.itemCount != 0,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(Route.ITEMS)) },
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = result?.isFragile ?: false,
                onCheckedChange = {},
                enabled = false,
                modifier = Modifier
                    .semantics {
                        contentDescription = "Fragile Checkbox"
                    },
            )
            Text(stringResource(R.string.fragile))
            Spacer(modifier = Modifier.weight(1f))
            BasicTextField(
                value = result?.value?.asCurrencyString() ?: "",
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodySmall,
                enabled = false,
                modifier = Modifier.semantics { contentDescription = "Value Field" },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen() {
    val summary = Summary(
        collectionCount = 1,
        boxCount = 2,
        itemCount = 3,
        value = 100.0,
        isFragile = true
    )
    SummaryScreen(
        result = summary,
        navigateToTopLevelDestination = {},
    )
}

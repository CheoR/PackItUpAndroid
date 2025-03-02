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
import com.example.packitupandroid.ui.ViewModelProvider
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.ui.common.card.SummaryCard
import com.example.packitupandroid.ui.navigation.Route
import com.example.packitupandroid.ui.navigation.TOP_LEVEL_DESTINATIONS
import com.example.packitupandroid.ui.navigation.TopLevelDestination
import com.example.packitupandroid.utils.ActionColumnIcon
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.asCurrencyString


/**
 * Determines the top-level destination associated with a given route.
 *
 * This function maps a navigation route string to its corresponding [TopLevelDestination].
 * It's used to identify the main screen or section that a particular route belongs to.
 *
 * Currently, the mapping is based on predefined constants for collections, boxes, and items.
 * If a route is not recognized as one of these top-level destinations, an exception is thrown.
 *
 * @param route The navigation route string (e.g., Route.COLLECTIONS, Route.BOXES, Route.ITEMS).
 * @return The [TopLevelDestination] enum value associated with the provided route.
 * @throws IllegalArgumentException If the provided route is not a recognized top-level destination.
 *
 * @see TopLevelDestination
 * @see Route
 */
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
 * @param viewModel The [SummaryScreenViewModel] to fetch data from. Defaults to a new instance from the PackItUpViewModelProvider.
 */
@Composable
fun SummaryScreen(
    viewModel: SummaryScreenViewModel = viewModel(factory = ViewModelProvider.Factory),
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
    val fragileContentDescription = stringResource(R.string.fragile_checkbox)
    val valueFieldContentDescription = stringResource(R.string.value)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.space_arrangement_small)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SummaryCard(
            name = R.string.collections,
            description = stringResource(R.string.badgeContentDescription, stringResource(R.string.collections)),
            iconsContent = {
                Column {
                    IconBadge(
                        image = ImageContent.VectorImage(Icons.Default.Category),
                        badgeContentDescription = stringResource(R.string.badgeContentDescription, stringResource(R.string.collections)),
                        badgeCount = result?.collectionCount ?: 0,
                    )
                }
            },
            actionIcon = actionIcon,
            canNavigateToScreen = true,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(Route.COLLECTIONS)) },
        )
        SummaryCard(
            name = R.string.boxes,
            description = stringResource(R.string.badgeContentDescription, stringResource(R.string.boxes)),
            iconsContent = {
                Column {
                    IconBadge(
                        image = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
                        badgeContentDescription = stringResource(R.string.badgeContentDescription, stringResource(R.string.boxes)),
                        badgeCount = result?.boxCount ?: 0,
                    )
                }
            },
            actionIcon = actionIcon,
            canNavigateToScreen = result?.boxCount != 0,
            navigateToTopLevelDestination = { navigateToTopLevelDestination(getTopLevelDestination(Route.BOXES)) },
        )
        SummaryCard(
            name = R.string.items,
            description = stringResource(R.string.badgeContentDescription, stringResource(R.string.items)),
            iconsContent = {
                Column {
                    IconBadge(
                        image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                        badgeContentDescription = stringResource(R.string.badgeContentDescription, stringResource(R.string.items)),
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
                        contentDescription = fragileContentDescription
                    },
            )
            Text(stringResource(R.string.fragile))
            Spacer(modifier = Modifier.weight(1f))
            BasicTextField(
                value = result?.value?.asCurrencyString() ?: "",
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodySmall,
                enabled = false,
                modifier = Modifier.semantics { contentDescription = valueFieldContentDescription },
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

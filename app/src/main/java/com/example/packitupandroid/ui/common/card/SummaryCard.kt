package com.example.packitupandroid.ui.common.card

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.utils.ActionColumnIcon


/**
 * A composable function that displays a summary card with a name, description, and icons.
 *
 * @param name The name displayed on the card.
 * @param description The description displayed on the card.
 * @param iconsContent A composable lambda that defines the content of the icons column.
 * @param modifier Modifier to be applied to the card.
 */
@Composable
fun SummaryCard(
    @StringRes name: Int,
    actionIcon: ActionColumnIcon,
    canNavigateToScreen: Boolean,
    description: String,
    iconsContent: @Composable ColumnScope.() -> Unit,
    navigateToTopLevelDestination: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconButtonContentDescription = stringResource(R.string.icon_button_content_description)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.card_height)),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inversePrimary),
        ) {
            Column {
                iconsContent()
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f),
            ) {
                BasicTextField(
                    value = stringResource(name),
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                BasicTextField(
                    value = description,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyMedium,
                    minLines = 3,
                    maxLines = 3,
                    enabled = false,
                    modifier = modifier
                )
            }
            Column(
                modifier = modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (canNavigateToScreen) {
                    IconButton(
                        onClick = {
                            navigateToTopLevelDestination()
                        },
                        modifier = Modifier
                            .fillMaxHeight(),
                        content = {
                            Icon(
                                imageVector = actionIcon.icon,
                                contentDescription = iconButtonContentDescription,
                                modifier = Modifier
                                    .size(24.dp),
                            )
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

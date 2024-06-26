package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.utils.EditMode

@Composable
fun SummaryCard(
    name: String,
    description: String,
    icon1: ColumnIcon,
    modifier: Modifier = Modifier,
    badgeCount1: Int = 0,
    editMode: EditMode = EditMode.NoEdit,
    canNavigateToScreen: Boolean = false,
    navigateToTopLevelDestination: () -> Unit,
) {
    val actionIcon: ActionColumnIcon = ActionColumnIcon.RightArrow

    Card(
        modifier = modifier
            .height(dimensionResource(R.dimen.card_height))
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small))),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.image_size_medium)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small)),
        ) {
            IconsColumn(
                icon1 = icon1,
                badgeCount1 = badgeCount1,
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.titleSmall,
                    enabled = editMode == EditMode.Edit,
                    modifier = Modifier
                        .fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                BasicTextField(
                    // TODO: fix
                    value = description,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodySmall,
                    enabled = editMode == EditMode.Edit,
                    maxLines = 3,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                )
            }
            Column(
                modifier = modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if(canNavigateToScreen) {
                    IconButton(
                        onClick = {
                            navigateToTopLevelDestination()
                        },
                        modifier = Modifier
                            .fillMaxHeight(),
                        content = {
                            Icon(
                                imageVector = actionIcon.icon,
                                contentDescription = "Icon Button",
                                modifier = Modifier
                                    .size(24.dp),
                            )
                        }
                    )
                } else {
                    Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                    )
                }
            }

        }
    }
}

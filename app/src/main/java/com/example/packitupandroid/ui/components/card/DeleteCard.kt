package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.asCurrencyString


@Composable
fun DeleteCard(
    element: BaseCardData,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    cardType: CardType = CardType.Default,
    // TODO: add dropdown
) {
    val (icons, badgeCounts) = getIconsAndBadges(element)
    val icon1 = icons.first
    val icon2 = icons.second
    val badgeCount1 = badgeCounts.first
    val badgeCount2 = badgeCounts.second

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Are you sure you want to delete ${element.name}?",
        )
        Spacer(modifier = Modifier.weight(1f))
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
                    icon2 = icon2,
                    badgeCount1 = badgeCount1,
                    badgeCount2 = badgeCount2,
                    isShowBadgeCount = cardType !is CardType.Item,
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BasicTextField(
                        value = element.name,
                        onValueChange = {},
                        textStyle = MaterialTheme.typography.titleSmall,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
//                    dropdownOptions?.let {
//                        BasicTextField(
//                            value = dropdownOptions.first(), // Todo: display selected value if available else first
//                            onValueChange = {
//                                element.description = it
//                            },
//                            textStyle = MaterialTheme.typography.bodySmall,
//                            enabled = isEditable(EditFields.Dropdown),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(4.dp)
//                        )
//                    }
                    BasicTextField(
                        // TODO: fix
                        value = element.description ?: "missing",
                        onValueChange = {},
                        textStyle = MaterialTheme.typography.bodySmall,
                        enabled = false,
                        maxLines = 3,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    )
                    if(element !is Summary) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Checkbox(
                                checked = element.isFragile,
                                onCheckedChange = {},
                                enabled = false,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fragile")
                            Spacer(modifier = Modifier.weight(1f))
                            BasicTextField(
                                value = element.value.asCurrencyString(),
                                onValueChange = {},
                                textStyle = MaterialTheme.typography.bodySmall,
                                enabled = false,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                singleLine = true,
                            )
                        }
                    }
                }
                Column(
                    modifier = modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .size(24.dp)){}
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AddConfirmCancelButton(button = ButtonType.Confirm, onClick = onDelete, enabled = true)
        AddConfirmCancelButton(button = ButtonType.Cancel, onClick = onCancel, enabled = true)
    }
}



















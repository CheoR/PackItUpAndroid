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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.EditMode
import com.example.packitupandroid.utils.asCurrencyString
import com.example.packitupandroid.utils.parseCurrencyToDouble

@Composable
fun<T: BaseCardData> EditCard(
    element: T,
    onEdit: (T) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.Edit,
    cardType: CardType = CardType.Default,
    // TODO: add dropdown
) {
    fun isEditable(field: EditFields) = editMode == EditMode.Edit && element.editFields.contains(field)
    var localData by remember { mutableStateOf(element) }
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
        Spacer(modifier = Modifier.weight(2f))
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
                        value = localData.name,
                        onValueChange = {
                            // TODO: refactor this
                            localData = when (localData) {
                                is Item -> (localData as Item).copy(name = it) as T
                                is Box -> (localData as Box).copy(name = it) as T
                                is Collection -> (localData as Collection).copy(name = it) as T
                                else -> throw IllegalStateException("Unsupported type")
                            }
                        },
                        textStyle = MaterialTheme.typography.titleSmall,
                        enabled = isEditable(EditFields.Name),
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
                        value = localData.description ?: "missing",
                        onValueChange = {
                            localData = when (localData) {
                                is Item -> (localData as Item).copy(description = it) as T
                                is Box -> (localData as Box).copy(description = it) as T
                                is Collection -> (localData as Collection).copy(description = it) as T
                                else -> throw IllegalStateException("Unsupported type")
                            }
                        },
                        textStyle = MaterialTheme.typography.bodySmall,
                        enabled = isEditable(EditFields.Description),
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
                                checked = localData.isFragile,
                                onCheckedChange = {
                                    localData = when (localData) {
                                        is Item -> (localData as Item).copy(isFragile = it) as T
                                        else -> throw IllegalStateException("Unsupported type")
                                    }
                                },
                                enabled = isEditable(EditFields.IsFragile),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fragile")
                            Spacer(modifier = Modifier.weight(1f))
                            BasicTextField(
                                value = localData.value.asCurrencyString(),
                                onValueChange = {
                                    // Handle the case where the user enters an empty string
                                    val value = if (it.isEmpty()) 0.0 else it.parseCurrencyToDouble()
                                    localData = when (localData) {
                                        is Item -> (localData as Item).copy(value = value) as T
                                        else -> throw IllegalStateException("Unsupported type")
                                    }
                                },
                                textStyle = MaterialTheme.typography.bodySmall,
                                enabled = isEditable(EditFields.Value),
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
        AddConfirmCancelButton(button = ButtonType.Confirm, onClick = { onEdit(localData) }, enabled = true)
        AddConfirmCancelButton(button = ButtonType.Cancel, onClick = onCancel, enabled = true)
    }
}


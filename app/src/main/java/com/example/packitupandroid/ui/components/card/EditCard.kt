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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.components.common.ActionButton
import com.example.packitupandroid.ui.components.common.ButtonAction
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.EditMode
import com.example.packitupandroid.utils.asCurrencyString
import com.example.packitupandroid.utils.parseCurrencyToDouble


/**
 * A composable function to display and edit a card's details.
 *
 * This function renders an elevated card with editable fields, allowing the user
 * to modify the card's data. The card's appearance and editable fields can be
 * customized via the provided parameters.
 *
 * @param selectedCard A [MutableState] holding the currently selected card's data.
 *                     The type of data must implement [BaseCardData].
 *                     This state should be updated to reflect the current state of the card.
 * @param modifier Modifier to be applied to the card. Defaults to an empty modifier.
 * @param editableFields A [Set] of [EditFields] indicating which fields are editable.
 *                       Defaults to an empty set, meaning no fields are editable.
 * @param onFieldChange A callback function triggered when an editable field's value changes.
 *                      It receives the [MutableState] of the card data, the [EditFields] that was modified and the new value.
 *
 * @param D The type of [BaseCardData] being edited.
 *
 * @throws IllegalStateException if [selectedCard] value is null.
 *
 * Example Usage:
 * ```
 *  var cardData by remember { mutableStateOf<MyCardData?>(MyCardData */
@Composable
fun <D: BaseCardData>EditCard(
    selectedCard: MutableState<D?>,
    modifier: Modifier = Modifier,
    editableFields: Set<EditFields> = emptySet(),
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
//    element: T,
//    onEdit: (T) -> Unit,
//    onCancel: () -> Unit,
//    getDropdownOptions: (() -> List<QueryDropdownOptions>)? = null,
//    editMode: EditMode = EditMode.Edit,
//    cardType: CardType = CardType.Default,
//    // TODO: add dropdown
) {
    fun isEditable(field: EditFields) = editableFields.contains(field)

//    var localData by remember { mutableStateOf(element) }
//    val (icons, badgeCounts) = getIconsAndBadges(element)
//    val icon1 = icons.first
//    val icon2 = icons.second
//    val badgeCount1 = badgeCounts.first
//    val badgeCount2 = badgeCounts.second
//
//    val options: List<QueryDropdownOptions> = getDropdownOptions?.invoke() ?: emptyList()
//    var expanded by remember { mutableStateOf(false) }
//    var selectedOption by remember {
//        mutableStateOf(
//            when (element) {
//                is Box -> options.firstOrNull { it.id == element.collectionId }?.id
//                is Item -> options.firstOrNull { it.id == element.boxId }?.id
//                else -> null
//            }
//        )
//    }
    Text("TODO: continue from here")
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.inversePrimary)
//            .semantics {
//                contentDescription = "Edit Card"
//            },
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Spacer(modifier = Modifier.weight(2f))
//        Card(
//            modifier = modifier
//                .height(dimensionResource(R.dimen.card_height))
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small))),
//            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.image_size_medium)),
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(dimensionResource(R.dimen.padding_small)),
//            ) {
//                IconsColumn(
//                    icon1 = icon1,
//                    icon2 = icon2,
//                    badgeCount1 = badgeCount1,
//                    badgeCount2 = badgeCount2,
//                    isShowBadgeCount = cardType !is CardType.Item,
//                )
//                Column(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .weight(2f)
//                        .padding(horizontal = 4.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    BasicTextField(
//                        value = localData.name,
//                        onValueChange = {
//                            // TODO: refactor this
//                            localData = when (localData) {
//                                is Item -> (localData as Item).copy(name = it) as T
//                                is Box -> (localData as Box).copy(name = it) as T
//                                is Collection -> (localData as Collection).copy(name = it) as T
//                                else -> throw IllegalStateException("Unsupported type")
//                            }
//                        },
//                        textStyle = MaterialTheme.typography.titleSmall,
//                        enabled = isEditable(EditFields.Name),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .semantics {
//                                contentDescription = "Edit Name Field"
//                            },
//                    )
//                    Box {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                        ) {
//                            Text(
//                                modifier = Modifier
//                                    .semantics {
//                                        contentDescription = "Edit Dropdown Selection Value"
//                                    },
////                                component does not display if there is no value
//                                text=options.firstOrNull { it.id == selectedOption }?.name ?: " ",
//                                )
//                            IconButton(onClick = { expanded = !expanded }, modifier = Modifier.semantics { contentDescription = "Icon Selector" }) {
//                                Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(R.string.more))
//                            }
//                        }
//
//                        DropdownMenu(
//                            expanded = expanded,
//                            onDismissRequest = { expanded = false },
//                            offset = DpOffset(0.dp, (-150).dp),
//                            modifier = Modifier
//                                .background(MaterialTheme.colorScheme.inversePrimary)
//                                .semantics {
//                                    contentDescription = "Edit Dropdown Menu"
//                                },
//                        ) {
//                            options.forEach { option ->
//                                DropdownMenuItem(
//                                    modifier = Modifier
//                                        .semantics {
//                                            contentDescription = "Edit Dropdown Menu Selection"
//                                        },
//                                    text = {
//                                        Text(text = option.name?: " " )
//                                           },
//                                    onClick = {
//                                        selectedOption = option.id
//                                        localData = when (localData) {
//                                            is Item -> (localData as Item).copy(boxId = option.id) as T
//                                            is Box -> (localData as Box).copy(collectionId = option.id) as T
//                                            else -> throw IllegalStateException("Unsupported type")
//                                        }
//                                        expanded = false
//                                    })
//                            }
//                        }
//                    }
//                    BasicTextField(
//                        // TODO: fix
//                        value = localData.description ?: " ",
//                        onValueChange = {
//                            localData = when (localData) {
//                                is Item -> (localData as Item).copy(description = it) as T
//                                is Box -> (localData as Box).copy(description = it) as T
//                                is Collection -> (localData as Collection).copy(description = it) as T
//                                else -> throw IllegalStateException("Unsupported type")
//                            }
//                        },
//                        textStyle = MaterialTheme.typography.bodySmall,
//                        enabled = isEditable(EditFields.Description),
//                        maxLines = 3,
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxWidth()
//                            .semantics {
//                                contentDescription = "Edit Description Field"
//                            },
//                    )
//                    if(element !is Summary) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                        ) {
//                            Checkbox(
//                                checked = localData.isFragile,
//                                onCheckedChange = {
//                                    localData = when (localData) {
//                                        is Item -> (localData as Item).copy(isFragile = it) as T
//                                        else -> throw IllegalStateException("Unsupported type")
//                                    }
//                                },
//                                enabled = isEditable(EditFields.IsFragile),
//                                modifier = Modifier
//                                    .semantics {
//                                        contentDescription = "Edit Fragile Checkbox"
//                                    },
//                            )
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text("Fragile")
//                            Spacer(modifier = Modifier.weight(1f))
//                            BasicTextField(
//                                value = localData.value.asCurrencyString(),
//                                onValueChange = {
//                                    // Handle the case where the user enters an empty string
//                                    val value = if (it.isEmpty()) 0.0 else it.parseCurrencyToDouble()
//                                    localData = when (localData) {
//                                        is Item -> (localData as Item).copy(value = value) as T
//                                        else -> throw IllegalStateException("Unsupported type")
//                                    }
//                                },
//                                textStyle = MaterialTheme.typography.bodySmall,
//                                enabled = isEditable(EditFields.Value),
//                                keyboardOptions = KeyboardOptions.Default.copy(
//                                    keyboardType = KeyboardType.Number,
//                                    imeAction = ImeAction.Done
//                                ),
//                                singleLine = true,
//                                modifier = Modifier
//                                    .semantics {
//                                        contentDescription = "Edit Value Field"
//                                    },
//                            )
//                        }
//                    }
//                }
//                Column(
//                    modifier = modifier
//                        .fillMaxHeight(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Box(modifier = Modifier
//                        .fillMaxHeight()
//                        .size(24.dp)){}
//                }
//            }
//        }
//        Spacer(modifier = Modifier.weight(1f))
//        ActionButton(action = ButtonAction.Confirm, onClick = { onEdit(localData) }, enabled = true)
//        ActionButton(action = ButtonAction.Cancel, onClick = onCancel, enabled = true)
//    }
}


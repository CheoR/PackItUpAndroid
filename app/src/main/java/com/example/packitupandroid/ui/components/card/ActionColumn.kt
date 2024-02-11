package com.example.packitupandroid.ui.components.card

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.packitupandroid.ui.components.EditCard

@Composable
fun ActionColumn(
    data: BaseCardData,
    onClick: () -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: () -> Unit? = {},
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
    editFields: Set<EditFields> = emptySet(),
) {
    val actionIcon: ActionColumnState = when(cardType) {
        is CardType.Summary -> ActionColumnState.RightArrow
        else -> ActionColumnState.ThreeDots
    }

    var expanded by remember { mutableStateOf(false) }
    var showEditCard by remember { mutableStateOf(false) }
    var elementToEdit by remember { mutableStateOf<BaseCardData?>(data) }

    if(showEditCard && elementToEdit != null) {
        expanded = false
        Dialog(
            onDismissRequest = { showEditCard = false },
        ) {
            when (data) {
                is BaseCardData.ItemData -> {
//                    val itemData = elementToEdit as BaseCardData.ItemData
                    EditCard(
                        data = data, // itemData.item,
                        onUpdate = {
                            val updatedItem = it as BaseCardData.ItemData
                            Log.i("ACTION COLUMN", updatedItem.toString())
                            onUpdate(it)
                            showEditCard = false
                        },
                        editFields = editFields,
                        onCancel = { showEditCard = false },
                    )
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (editMode) {
            is EditMode.Edit -> Box(modifier = Modifier
                .fillMaxHeight()
                .size(24.dp))
            else -> {
                if (cardType == CardType.Summary) {
                    IconButton(
                        onClick = { onClick() }, // for navigation
                        modifier = Modifier
                            .fillMaxHeight(),
                        content = {
                            Icon(
                                imageVector = actionIcon.icon, // to avoid mismatch error
                                contentDescription = "Icon Button",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    )
                }
                else {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .fillMaxHeight(),
                        content = {
                            Icon(
                                imageVector = actionIcon.icon,
                                contentDescription = "Icon Button",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {  expanded = false },
                        offset = DpOffset(0.dp, (-180).dp),
                    ) {
                        if(cardType !is CardType.Item) {
                            DropdownMenuItem(
                                text = { Text("add") },
                                onClick = { /*TODO*/ },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "add"
                                    )
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("edit") },
                            onClick = { showEditCard = true },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "edit"
                                )
                            }
                        )
                        if(cardType == CardType.Item) {
                            DropdownMenuItem(
                                text = { Text("camera") },
                                onClick = { /*TODO*/ },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "camera"
                                    )
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("delete") },
                            onClick = {
                                onDestroy(data)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

//@Preview(
//    showBackground = true,
//    group="Summary",
//)
//@Composable
//fun PreviewActionColumnSummaryCardNoEdit() {
//    ActionColumn(
//        onClick = { },
//        editMode = EditMode.NoEdit,
//        cardType = CardType.Summary,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group="Default",
//)
//@Composable
//fun PreviewActionColumnDefaultCardNoEdit() {
//    ActionColumn(
//        onClick = { },
//        editMode = EditMode.NoEdit,
//        cardType = CardType.Collection, // Box, Item
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group="Edit",
//)
//@Composable
//fun PreviewActionColumnEditCardEdit() {
//    ActionColumn(
//        onClick = { },
//        editMode = EditMode.Edit,
//        cardType = CardType.Collection, // Box, Item
//    )
//}

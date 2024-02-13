package com.example.packitupandroid.ui.components.card

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Summary
import com.example.packitupandroid.ui.components.EditCard

@Composable
fun ActionColumn(
    data: MutableState<BaseCardData>,
//    data: BaseCardData,
    onClick: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onEdit: (BaseCardData) -> Unit,
    isExpanded: MutableState<Boolean>,
    isShowEditCard: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
    onDestroy: () -> Unit? = {},
) {
    val actionIcon: ActionColumnState = when(cardType) {
        is CardType.Summary -> ActionColumnState.RightArrow
        else -> ActionColumnState.ThreeDots
    }
    
    if(isShowEditCard.value) {
        isExpanded.value = false
        Dialog(
            onDismissRequest = onCancel
        ) {
            EditCard(
                data = data,
                onEdit = onEdit,
                onSave = onSave,
                onCancel = onCancel,
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (editMode) {
            is EditMode.Edit -> Box(modifier = Modifier
                .fillMaxHeight()
                .size(24.dp)
            )

            else -> {
                if(cardType is CardType.Summary) {
                    IconButton(
                        onClick = onClick, // for navigation
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
                        onClick = { isExpanded.value = true },
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
                        expanded = isExpanded.value,
                        onDismissRequest = onCancel,
                        offset = DpOffset(0.dp, (-150).dp),
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.inversePrimary),
                    ) {
                        if(cardType !is CardType.Item) {
                            DropdownMenuItem(
                                text = { Text(
                                    "add",
                                )},
                                onClick = { /*TODO*/ },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "add",
                                    )
                                },
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("edit") },
                            onClick = { isShowEditCard.value = true },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "edit"
                                )
                            }
                        )
                        if(cardType is CardType.Item) {
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
                                onDestroy()
                                isExpanded.value = false
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
//    val summary = Summary(
//        id = "summary",
//        name = "summary",
//        description = "summary",
//    )
//
//    val isShowEditCard = remember { mutableStateOf(false) }
//    val isExpanded = remember { mutableStateOf(false) }
////        editMode = EditMode.NoEdit,
//    ActionColumn(
//        data = summary,
//        onUpdate = {},
//        onClick = {},
//        isShowEditCard = isShowEditCard,
//        isExpanded = isExpanded,
////        editMode = EditMode.NoEdit,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group="Default",
//)
//@Composable
//fun PreviewActionColumnDefaultCardNoEdit(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    val isShowEditCard = remember { mutableStateOf(false) }
//    val isExpanded = remember { mutableStateOf(false) }
//    ActionColumn(
//        data = collection,
//        onUpdate = {},
//        onClick = {},
//        isShowEditCard = isShowEditCard,
//        isExpanded = isExpanded,
////        editMode = EditMode.NoEdit,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group="Edit",
//)
//@Composable
//fun PreviewActionColumnEditCardEdit(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    val isShowEditCard = remember { mutableStateOf(false) }
//    val isExpanded = remember { mutableStateOf(false) }
//    ActionColumn(
//        data = collection,
//        onUpdate = {},
//        onClick = {},
//        isShowEditCard = isShowEditCard,
//        isExpanded = isExpanded,
////        editMode = EditMode.Edit,
//    )
//}

package com.example.packitupandroid.ui.components.card

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditMode

@Composable
fun<T: BaseCardData> ActionColumn(
    element: T,
    isExpanded: MutableState<Boolean>,
    isShowEditCard: MutableState<Boolean>,
    isShowCameraCard: MutableState<Boolean>,
    isShowDeleteCard: MutableState<Boolean>,
    onClick: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (T) -> Unit,
    modifier: Modifier = Modifier,
    getDropdownOptions: (() -> List<QueryDropdownOptions>)? = null,
    filterElements:( (id: String) -> Unit)? = null,
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
) {
    val actionIcon: ActionColumnIcon = when(cardType) {
        is CardType.Summary -> ActionColumnIcon.RightArrow
        else -> ActionColumnIcon.ThreeDots
    }

    if(isShowEditCard.value) {
        isExpanded.value = false
        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {},
            dismissButton = {},
            text = {
                EditCard(
                    element = element,
                    onEdit = onEdit,
                    onCancel = onCancel,
                    getDropdownOptions = getDropdownOptions,
                    cardType = cardType,
                )
            }
        )
    }

    if(isShowDeleteCard.value) {
        isExpanded.value = false
        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {},
            dismissButton = {},
            text = {
                DeleteCard(
                    element = element,
                    onDelete = onDelete,
                    onCancel = onCancel,
                    getDropdownOptions = getDropdownOptions,
                    cardType = cardType,
                )
            },
        )
    }

    if(isShowCameraCard.value) {
        isExpanded.value = false
        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {},
            dismissButton = {},
            text = {
                CameraCard(
                    element = element,
                    onClick = onEdit,
                    onCancel = onCancel,
                )
            }
        )
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
                            .fillMaxHeight()
                            .semantics {
                                contentDescription = "Action Column"
                            },
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
                            .fillMaxHeight() //,
                            .semantics {
                                contentDescription = "Open Menu"
                            },
                        content = {
                            Icon(
                                imageVector = actionIcon.icon,
                                contentDescription = null,
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
                            .background(MaterialTheme.colorScheme.inversePrimary)
                            .semantics {
                                contentDescription = "Dropdown Menu"
                            }
                        ,
                    ) {
                        if(cardType !is CardType.Item) {
                            DropdownMenuItem(
                                text = { Text(
                                    "add",
                                )},
                                onClick = {
                                    if (filterElements != null) {
                                        filterElements(element.id)
                                    }
                                },
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
                                onClick = { isShowCameraCard.value = true },
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
                                isShowDeleteCard.value = true
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
//    // to avoid
//    // Creating a state object during composition without using remember More... (Ctrl+F1)
//    val isShowEditCard = remember { mutableStateOf(false) }
//    val isShowCameraCard = remember { mutableStateOf(false) }
//    val isShowDeleteCard = remember { mutableStateOf(false) }
//    val isExpanded = remember { mutableStateOf(false) }
//    ActionColumn(
//        element = summary,
//        onClick = {},
//        onEdit = {},
//        onCancel = {},
//        onDelete = {},
//        getDropdownOptions = { emptyList() },
//        isShowEditCard = isShowEditCard,
//        isShowCameraCard = isShowCameraCard,
//        isShowDeleteCard = isShowDeleteCard,
//        isExpanded = isExpanded,
//        cardType = CardType.Summary,
//    )
//}

// TODO: Fix
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
//    val isShowCameraCard = remember { mutableStateOf(false) }
//    val isShowDeleteCard = remember { mutableStateOf(false) }
//    val isExpanded = remember { mutableStateOf(false) }
//    ActionColumn(
//        element = collection,
//        onClick = {},
//        onEdit = {},
//        onCancel = {},
//        onDelete = {},
//        isShowEditCard = isShowEditCard,
//        isShowCameraCard = isShowCameraCard,
//        isShowDeleteCard = isShowDeleteCard,
//        isExpanded = isExpanded,
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
//    val isShowCameraCard = remember { mutableStateOf(false) }
//    val isShowDeleteCard = remember { mutableStateOf(false) }
//    val isExpanded = remember { mutableStateOf(false) }
//    ActionColumn(
//        element = collection,
//        onClick = {},
//        onEdit = {},
//        onCancel = {},
//        onDelete = {},
//        isShowEditCard = isShowEditCard,
//        isShowCameraCard = isShowCameraCard,
//        isShowDeleteCard = isShowDeleteCard,
//        isExpanded = isExpanded,
//        editMode = EditMode.Edit,
//    )
//}

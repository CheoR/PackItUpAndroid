package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R

@Composable
fun ActionColumn(
    onClick: () -> Unit,
    editMode: EditMode = EditMode.NonEditable,
    cardType: CardType = CardType.NotSummaryCard,
    elementType: ElementType = ElementType.Summary,
) {
    val actionIcon = when(cardType) {
        is CardType.NotSummaryCard -> ImageVector.vectorResource(R.drawable.baseline_more_vert_24)
        else -> Icons.Default.ArrowForward
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (editMode) {
            is EditMode.Editable -> Box(modifier = Modifier
                .fillMaxHeight()
                .size(24.dp))
            else -> {
                if (elementType == ElementType.Summary) {
                    IconButton(
                        onClick = {  },
                        modifier = Modifier
                            .fillMaxHeight(),
                        content = {
                            Icon(
                                imageVector = actionIcon,
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
                                imageVector = actionIcon,
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
                        if(elementType is ElementType.Item) {
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
                            onClick = { /*TODO*/ },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "edit"
                                )
                            }
                        )
                        if(elementType == ElementType.Item) {
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
                            onClick = { /*TODO*/ },
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


@Preview(showBackground = true)
@Composable
fun PreviewActionColumnSummaryCard() {
    ActionColumn(
        onClick = { },
        editMode = EditMode.NonEditable,
        cardType = CardType.SummaryCard,
        elementType = ElementType.Summary,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionColumnViewCardIsNotEditable() {
    ActionColumn(
        onClick = { },
        editMode = EditMode.NonEditable,
        cardType = CardType.NotSummaryCard,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionColumnEditCardIsEditable() {
    ActionColumn(
        onClick = { },
        editMode = EditMode.Editable,
        cardType = CardType.NotSummaryCard,
    )
}

package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R

@Composable
fun ActionColumn(
    onClick: () -> Unit,
    editMode: EditMode = EditMode.NonEditable,
    cardType: CardType = CardType.NotSummaryCard,
) {
    val actionIcon = when(viewMode) {
        is ViewMode.NotSummaryCard -> ImageVector.vectorResource(R.drawable.baseline_more_vert_24)
        else -> Icons.Default.ArrowForward
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when (editMode) {
            // TODO: if item card to edit, add camera icon/action
            is EditMode.Editable -> Box(modifier = Modifier.fillMaxHeight().size(24.dp))
            else -> IconButton(
                onClick = { onClick() },
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
//    val actionIcon = if(viewMode == ViewMode.NotSummaryCard) ImageVector.vectorResource(R.drawable.baseline_more_vert_24) else Icons.Default.ArrowForward
//        if (editMode == EditMode.Editable) {
//            Box(modifier = Modifier.fillMaxHeight().size(24.dp))
//        } else {
//            IconButton(
//                onClick = { onClick() },
//                modifier = Modifier
//                    .fillMaxHeight(),
//                content = {
//                    Icon(
//                        imageVector = actionIcon,
//                        contentDescription = "Icon Button",
//                        modifier = Modifier
//                            .size(24.dp)
//                    )
//                }
//            )
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionColumnSummaryCard() {
    ActionColumn(
        onClick = { },
        editMode = EditMode.NonEditable,
        cardType = CardType.SummaryCard,
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

//    if (editMode == EditMode.Editable) {
//        IconButton(onClick = { onUpdate(data) }) {
//            Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
//        }
//    } else {
//        IconButton(
//            onClick = { onUpdate(data) }
//        ) {
//            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
//        }
//    }
//
//    IconButton(onClick = { onDelete(data) }) {
//        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
//    }
//}
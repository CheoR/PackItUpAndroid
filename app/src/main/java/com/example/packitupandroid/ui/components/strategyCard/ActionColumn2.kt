package com.example.packitupandroid.ui.components.strategyCard

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R

@Composable
fun ActionColumn2(
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCamera: () -> Unit,
    modifier: Modifier = Modifier,
    enableCamera: Boolean = false,
) {
    val isExpanded = remember { mutableStateOf(false) }
    IconButton(
        onClick = { isExpanded.value = !isExpanded.value },
        modifier = modifier
            .semantics {
                contentDescription = "Open Menu"
            },
        content = {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_medium))
            )
        }
    )
    ActionMenu(
        expanded = isExpanded.value,
        onDismiss = { isExpanded.value = false },
        enableCamera = enableCamera,
        onAdd = {
            onAdd()
            isExpanded.value = false
        },
        onEdit = {
            onEdit()
            isExpanded.value = false
         },
        onDelete = {
            onDelete()
            isExpanded.value = false
       },
        onCamera = {
            onCamera()
            isExpanded.value = false
       },
    )
}

@Preview(showBackground = true, heightDp = 300, widthDp = 300, backgroundColor = 0x9FFF9993)
@Composable
fun PreviewActionColumn2Default() {
    ActionColumn2(onAdd = {}, onEdit = {}, onDelete = {}, onCamera = {})
}

@Preview(showBackground = true, heightDp = 300, widthDp = 300, backgroundColor = 0x9FFF9993)
@Composable
fun PreviewActionColumn2EnableCamera() {
    ActionColumn2(enableCamera = true, onAdd = {}, onEdit = {}, onDelete = {}, onCamera = {})
}

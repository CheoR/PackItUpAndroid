package com.example.packitupandroid.ui.components.strategyCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


@Composable
fun ActionMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    enableCamera: Boolean = false,
    onDismiss: () -> Unit = {},
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCamera: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
//        offset = DpOffset(0.dp, (-150).dp),
        modifier = modifier
            .background(MaterialTheme.colorScheme.inversePrimary)
            .semantics {
                contentDescription = "Dropdown Menu"
            }
        ,
    ) {
        if(enableCamera) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dropdown_menu_item_camera)) },
                onClick = onCamera,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = stringResource(R.string.dropdown_menu_item_camera),
                    )
                }
            )
        } else {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.dropdown_menu_item_add)) },
                onClick = onAdd,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.dropdown_menu_item_add),
                    )
                },
            )
        }
        DropdownMenuItem(
            text = { Text(stringResource(R.string.dropdown_menu_item_edit)) },
            onClick = onEdit,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.dropdown_menu_item_edit),
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.dropdown_menu_item_delete)) },
            onClick = onDelete,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.dropdown_menu_item_delete),
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewActionMenu() {
    var expaneded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .size(500.dp)
            .background(MaterialTheme.colorScheme.inversePrimary),
    ) {
        ActionMenu(
            expanded = expaneded,
            onDismiss = { expaneded = false },
            onAdd = { expaneded = false },
            onEdit = { expaneded = false },
            onDelete = { expaneded = false },
            onCamera = { expaneded = false },
        )
    }
}

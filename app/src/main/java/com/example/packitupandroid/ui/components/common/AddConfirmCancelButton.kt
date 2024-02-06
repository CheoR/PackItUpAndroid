package com.example.packitupandroid.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R

//sealed class ButtonAction {
//    data class Add(
//        val label: String = "add",
//        val icon: ImageVector = Icons.Default.Add,
//        val color: Color = Color.Gray,
//    ) : ButtonAction()
//
//    data class Confirm(
//        val label: String = "confirm",
//        val icon: ImageVector = Icons.Default.Check,
//        val color: Color = Color.Green,
//    ) : ButtonAction()
//
//    data class Cancel(
//        val label: String = "cancel",
//        val icon: ImageVector = Icons.Default.Cancel,
//        val color: Color = Color.Red,
//    ) : ButtonAction()
//}

sealed class ButtonType {
    object Add : ButtonType()
    object Cancel : ButtonType()
    object Confirm : ButtonType()
}

@Composable
fun AddConfirmCancelButton(
    modifier: Modifier = Modifier,
    count: Int? = 0,
    button: ButtonType,
    onClick: (Int?) -> Unit,
    resetCount: (() -> Unit)? = null,
) {
    val (label, icon, color) = when(button) {
        is ButtonType.Add -> Triple("add", Icons.Default.Add, MaterialTheme.colorScheme.onPrimaryContainer)
        is ButtonType.Confirm ->  Triple("confirm", Icons.Default.Check, MaterialTheme.colorScheme.onSecondaryContainer)
        else ->  Triple("cancel", Icons.Default.Cancel, MaterialTheme.colorScheme.error) // is ButtonType.Cancel
    }

    Row(
        modifier = modifier
            .height(64.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = modifier
                .fillMaxWidth()
                .background(color = color),
            enabled = (button != ButtonType.Add) || (count ?: 0) > 0,
            onClick = {
                onClick(count)
                resetCount?.let {
                    resetCount()
                }
            },
            shape = RoundedCornerShape(dimensionResource(R.dimen.roundness_x_small)),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = color,
            ),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddConfirmCancelButtonAdd() {
    AddConfirmCancelButton(
        button = ButtonType.Add,
        onClick = {},
        count = 5,
        resetCount = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddConfirmCancelButtonZero() {
    AddConfirmCancelButton(
        button = ButtonType.Add,
        onClick = {},
        count = 0,
        resetCount = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddConfirmCancelButtonAddDisabled() {
    AddConfirmCancelButton(
        button = ButtonType.Add,
        onClick = {},
        count = null,
        resetCount = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddConfirmCancelButtonConfirm() {
    AddConfirmCancelButton(
        button = ButtonType.Confirm,
        onClick = {},
        resetCount = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddConfirmCancelButtonCancel() {
    AddConfirmCancelButton(
        button = ButtonType.Cancel,
        onClick = {},
        resetCount = {},
    )
}

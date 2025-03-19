package com.example.packitupandroid.ui.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


/**
 * A composable function that renders a button with a Add, Confirm, or Cancel action.
 *
 * This button displays a label, an icon, and has a specific color based on the provided [ButtonAction].
 *
 * @param action The [ButtonAction] to perform.
 * @param enabled Whether the button is enabled or not.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 */
@Composable
fun ActionButton(
    action: ButtonAction,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    //     contentPadding: Int = 8,
) {
    val (label, icon, color) = when(action) {
        is ButtonAction.Add -> Triple("add", Icons.Default.Add, MaterialTheme.colorScheme.onPrimaryContainer)
        is ButtonAction.Confirm ->  Triple("confirm", Icons.Default.Check, MaterialTheme.colorScheme.onSecondaryContainer)
        else ->  Triple("cancel", Icons.Default.Cancel, MaterialTheme.colorScheme.error)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = label },
            enabled = enabled,
            onClick = onClick,
            shape = RoundedCornerShape(dimensionResource(R.dimen.roundness_x_small)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            content = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(8.dp)) // .width(    contentPadding) instead?
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
    }
}

/**
 * Sealed class representing the different actions a button can perform.
 */
sealed class ButtonAction {
    /**
     * Represents the "Add" action.
     */
    object Add : ButtonAction()
    /**
     * Represents the "Cancel" action.
     */
    object Cancel : ButtonAction()
    /**
     * Represents the "Confirm" action.
     */
    object Confirm : ButtonAction()
}


@Preview(showBackground = true)
@Composable
fun PreviewActionButtonAddEnabled() {
    ActionButton(
        action = ButtonAction.Add,
        onClick = {},
        enabled = true,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionButtonAddDisabled() {
    ActionButton(
        action = ButtonAction.Add,
        onClick = {},
        enabled = false,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionButtonConfirmEnabled() {
    ActionButton(
        action = ButtonAction.Confirm,
        onClick = {},
        enabled = true,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionButtonConfirmDisabled() {
    ActionButton(
        action = ButtonAction.Confirm,
        onClick = {},
        enabled = false,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionButtonCancelEnabled() {
    ActionButton(
        action = ButtonAction.Cancel,
        onClick = {},
        enabled = true,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionButtonCancelDisabled() {
    ActionButton(
        action = ButtonAction.Cancel,
        onClick = {},
        enabled = false,
    )
}

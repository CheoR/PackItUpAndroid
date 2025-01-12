package com.example.packitupandroid.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.ui.components.common.ActionButton
import com.example.packitupandroid.ui.components.common.ButtonAction


/**
 * A composable function that renders a container with a confirm and cancel button.
 *
 * This container displays the provided content and two buttons at the bottom, one for confirming and one for canceling.
 *
 * @param onCancel The callback to be invoked when the cancel button is clicked.
 * @param onConfirm The callback to be invoked when the confirm button is clicked.
 * @param content The content to be displayed in the container.
 * @param modifier The modifier to be applied to the container.
 * @param confirmButtonEnabled Whether the confirm button is enabled or not.
 * @param cancelButtonEnabled Whether the cancel button is enabled or not.
 */
@Composable
fun ConfirmCancelContainer(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonEnabled: Boolean = false,
    cancelButtonEnabled: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        Spacer(modifier = Modifier.height(4.dp))
        ActionButton(action = ButtonAction.Confirm, onClick = onConfirm, enabled = confirmButtonEnabled)
        ActionButton(action = ButtonAction.Cancel, onClick = onCancel, enabled = cancelButtonEnabled)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmCancelContainer() {
    ConfirmCancelContainer(
        onCancel = {},
        onConfirm = {},
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text("Hello World")
            }
        }
    )
}

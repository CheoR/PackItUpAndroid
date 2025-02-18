package com.example.packitupandroid.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


/**
 * A composable function that displays a confirmation/cancellation dialog container.
 *
 * This function creates a dialog with a title, customizable content area, and confirm/cancel buttons.
 * It leverages Material Design principles for its appearance and structure.
 *
 * @param title The title displayed in the top app bar of the dialog.
 * @param dialogWidth The desired width of the dialog.
 * @param onCancel Callback function invoked when the user cancels the dialog (e.g., by clicking the cancel button or dismissing the dialog).
 * @param onConfirm Callback function invoked when the user confirms the action (e.g., by clicking the confirm button).
 * @param modifier Modifier for styling the overall layout of the dialog.
 * @param confirmButtonEnabled Boolean indicating whether the confirm button is enabled. Defaults to true.
 * @param cancelButtonEnabled Boolean indicating whether the cancel button is enabled. Defaults to true.
 * @param content The composable content displayed within the main area of the dialog.
 */
@Composable
fun ConfirmCancelContainer(
    title: String,
    dialogWidth: Dp,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonEnabled: Boolean = true,
    cancelButtonEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onCancel,
        modifier = modifier
//            .requiredWidth(dialogWidth)
            .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.roundness_x_small)))
            .background(MaterialTheme.colorScheme.background)
            .testTag("DialogContainer"),
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    content()
                }
                Spacer(modifier = Modifier.height(4.dp))
                ActionButton(action = ButtonAction.Confirm, onClick = onConfirm, enabled = confirmButtonEnabled)
                ActionButton(action = ButtonAction.Cancel, onClick = onCancel, enabled = cancelButtonEnabled)
            }
        }
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
        },
        title = "@Preview",
        dialogWidth = 300.dp,
        confirmButtonEnabled = true,
        cancelButtonEnabled = true,
    )
}

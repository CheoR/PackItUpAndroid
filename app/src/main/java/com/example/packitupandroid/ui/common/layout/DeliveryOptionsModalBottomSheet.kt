package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Displays a modal bottom sheet with various delivery options.
 *
 * This composable presents a sheet containing buttons for different delivery options,
 * such as shipping, summarize, and backup. Each button, when clicked, hides the sheet
 * and then invokes the provided `onDismissRequest` callback.
 *
 * @param sheetState The state of the bottom sheet. This controls whether the sheet is
 *                   visible or hidden.
 * @param coroutineScope The coroutine scope used to launch coroutines for hiding the sheet.
 * @param onDismissRequest A callback invoked when the sheet is dismissed, either by
 *                         clicking outside of the sheet or by clicking a delivery option button.
 * @param modifier Modifier to be applied to the bottom sheet.
 */
@Composable
fun DeliveryOptionsModalBottomSheet(
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            DeliveryOptionButton(
                icon = Icons.Filled.LocalShipping,
                text = stringResource(R.string.delivery_option_shipping),
                onClick = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                }
            )
            DeliveryOptionButton(
                icon = Icons.Filled.Summarize,
                text = stringResource(R.string.delivery_option_summarize),
                onClick = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                }
            )
            DeliveryOptionButton(
                icon = Icons.Filled.Backup,
                text = stringResource(R.string.delivery_option_backup),
                onClick = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun PreviewDeliveryOptionsModalBottomSheet() {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Box {
        DeliveryOptionsModalBottomSheet(
            sheetState = sheetState,
            coroutineScope = coroutineScope,
            onDismissRequest = {}
        )
    }
    DeliveryOptionsModalBottomSheet(
        sheetState = sheetState,
        coroutineScope = coroutineScope,
        onDismissRequest = {}
    )
}


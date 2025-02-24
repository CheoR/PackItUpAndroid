package com.example.packitupandroid.ui.common.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.common.component.ConfirmCancelContainer
import com.example.packitupandroid.ui.common.card.BaseCard
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.Result


/**
 * Displays a delete confirmation dialog for a selected card.
 *
 * This composable presents an alert dialog that allows the user to confirm or cancel the deletion
 * of a card. It displays the selected card within the dialog, along with confirm and cancel buttons.
 *
 * @param onCancel Callback invoked when the user cancels the deletion.
 * @param onConfirm Callback invoked when the user confirms the deletion.
 * @param iconsContent Composable lambda to define the content of icons displayed within the card.
 *                     This lambda receives a [ColumnScope] for layout purposes.
 * @param selectedCard MutableState holding the currently selected card to be deleted.
 *                     If null, nothing will be shown inside the dialog.
 * @param dropdownOptions Optional result containing a list of dropdown options for the card.
 *                        If null or an error state, no dropdown will be shown in the card.
 *
 * @param D The type of data contained within the card, extending [BaseCardData].
 */
@Composable
fun <D: BaseCardData> DeleteDialog(
    dialogWidth: Dp,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    iconsContent: @Composable ColumnScope.() -> Unit,
    selectedCard: MutableState<D?>,
    dropdownOptions: Result<List<DropdownOptions?>>? = null,
) {
    ConfirmCancelContainer(
        title = stringResource(R.string.delete_dialog_title, selectedCard.value?.name ?: ""),
        dialogWidth = dialogWidth,
        onCancel = onCancel,
        onConfirm = onConfirm,
    ) {
        selectedCard.value?.let {
            BaseCard(
                data = selectedCard.value!!,
                iconsContent = iconsContent,
                dropdownOptions = dropdownOptions,
                onCamera = {},
                onDelete = {},
                onUpdate = {},
                onAdd = {},
                modifier = Modifier
                    .testTag("DeleteCard"),
            )
        }
    }
}

package com.example.packitupandroid.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.components.card.EditCard
import com.example.packitupandroid.ui.components.layout.ConfirmCancelContainer
import com.example.packitupandroid.utils.EditFields


/**
 * Displays an edit dialog for a card.
 *
 * This composable function presents a dialog allowing the user to edit the fields of a selected card.
 * It includes a confirm and cancel button. The content of the dialog is generated based on the
 * `selectedCard`'s data and its defined editable fields.
 *
 * @param onConfirm Callback function to be executed when the user confirms the changes.
 *                  This function typically handles saving the updated card data.
 * @param onCancel Callback function to be executed when the user cancels the edit operation.
 *                 This function typically handles discarding the changes.
 * @param selectedCard MutableState holding the currently selected card data. The type of the card data must
 *                     extend `BaseCardData`. This state will be updated if the user changes the data in the Edit Dialog.
 *                     If null no EditCard is displayed.
 * @param onFieldChange Callback function invoked when a field value within the card is modified.
 *                      It receives the `selectedCard` MutableState, the `EditFields` enum representing the field,
 *                      and the new value as a String.
 * @param dialogWidth The desired width of the dialog.
 * @param D The type of the card data, which must extend `BaseCardData`.
 */
@Composable
fun <D: BaseCardData> EditDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    selectedCard: MutableState<D?>,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    dialogWidth: Dp,
) {
    BasicAlertDialog(
        onDismissRequest = onCancel,
        modifier = Modifier.Companion
            .requiredWidth(dialogWidth)
            .background(MaterialTheme.colorScheme.background)
            .testTag("BasicAlertDialog"),
    ) {
        ConfirmCancelContainer(
            onCancel = onCancel,
            onConfirm = onConfirm,
        ) {
            selectedCard.value?.let {
                EditCard(
                    selectedCard = selectedCard,
                    editableFields = it.editFields,
                    onFieldChange = onFieldChange,
                    modifier = Modifier.testTag("EditCard")
                )
            }
        }
    }
}

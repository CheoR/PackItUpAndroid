package com.example.packitupandroid.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.common.component.card.EditCard
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result


/**
 * Displays an edit dialog for a card.
 *
 * This composable function presents a dialog allowing the user to edit the fields of a selected card.
 * It includes a confirm and cancel button. The content of the dialog is generated based on the
 * `selectedCard`'s data and its defined editable fields.
 *
 * @param dialogWidth The desired width of the dialog.
 * @param onCancel Callback function to be executed when the user cancels the edit operation.
 *                 This function typically handles discarding the changes.
 * @param onConfirm Callback function to be executed when the user confirms the changes.
 *                  This function typically handles saving the updated card data.
 * @param onFieldChange Callback function invoked when a field value within the card is modified.
 *                      It receives the `selectedCard` MutableState, the `EditFields` enum representing the field,
 *                      and the new value as a String.
 * @param iconsContent A composable lambda that provides the content for the card's icon area.
 *                     It uses a [ColumnScope] to allow for column-based layout of the icons.
 * @param selectedCard MutableState holding the currently selected card data. The type of the card data must
 *                     extend `BaseCardData`. This state will be updated if the user changes the data in the Edit Dialog.
 *                     If null no EditCard is displayed.
 * @param D The type of the card data, which must extend `BaseCardData`.
 *
 * @param dropdownOptions A [Result] object representing the state of the dropdown options [DropdownOptions].
 *
 */
@Composable
fun <D: BaseCardData> EditDialog(
    dialogWidth: Dp,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    iconsContent: @Composable ColumnScope.() -> Unit,
    selectedCard: MutableState<D?>,
    dropdownOptions: Result<List<DropdownOptions?>>,
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
                    iconsContent = iconsContent,
                    onFieldChange = onFieldChange,
                    dropdownOptions = dropdownOptions,
                    modifier = Modifier.testTag("EditCard"),
                )
            }
        }
    }
}

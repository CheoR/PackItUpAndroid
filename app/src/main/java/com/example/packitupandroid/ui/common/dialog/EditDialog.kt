package com.example.packitupandroid.ui.common.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.common.card.EditCard
import com.example.packitupandroid.ui.common.component.ConfirmCancelContainer
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
 * @param dropdownOptions An optional [Result] object representing the state of the dropdown options.
 *                        If provided, a dropdown menu will be displayed, allowing the user to select from the options.
 *                        If `null`, no dropdown menu will be displayed. * @param D the type of data that the card will display. It must inherit from BaseCardData
 *
 */
@Composable
fun <D : BaseCardData> EditDialog(
    dialogWidth: Dp,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    iconsContent: @Composable ColumnScope.() -> Unit,
    selectedCard: MutableState<D?>,
    dropdownOptions: Result<List<DropdownOptions?>>? = null,
) {
    ConfirmCancelContainer(
        title = stringResource(R.string.edit_dialog_title, selectedCard.value?.name ?: ""),
        dialogWidth = dialogWidth,
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
                modifier = Modifier,
            )
        }
    }
}

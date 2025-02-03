package com.example.packitupandroid.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.components.dialog.CameraDialog
import com.example.packitupandroid.ui.components.dialog.EditDialog
import com.example.packitupandroid.ui.components.spinner.Spinner
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import kotlinx.coroutines.launch
import com.example.packitupandroid.utils.Result


/**
 * A composable function that displays a screen for managing a list of card data.
 * It handles loading, displaying, deleting, updating, and creating card data.
 * It also provides support for editing fields and displaying a camera dialog.
 *
 * @param result A [Result] object representing the state of the list of [BaseCardData].
 *               It can be [Result.Success] with the list of data, [Result.Error] indicating an error,
 *               or [Result.Loading] indicating that data is being loaded.
 * @param key A function that returns a unique string key for each item in the list.
 *            This is used by the [LazyColumn] for efficient item recomposition.
 * @param generateIconsColumn A function that generates the composable content for the icons column
 *                            for each card. It takes a [BaseCardData] object as input and returns
 *                            a lambda function that builds the icon column content within a [ColumnScope].
 * @param onDelete A lambda function that is called when the user wants to delete a card.
 *                 It takes a list of card IDs (as strings) to be deleted.
 * @param onCreate A lambda function that is called when the user wants to create a new card.
 *                 It takes an integer argument which can represent a creation type or index
 * @param onFieldChange A lambda function that is called when the user modifies
 * @param onUpdate A lambda function that is called when the user wants to update a card's properties.
 *                  It takes a card and updates the card based on the card type's update function
 * @param dropdownOptions A [Result] object representing the state of the dropdown options [DropdownOptions].
 * @param addElements A callback function that is invoked when the user clicks on the add button. User is
 *  navigated to sub element filtered by data.id e.g. User selects `add` on a Box element, user then
 *  is redirected to ItemsScreen with only items filtered by Box.id that equals the Box user clicked on.
 * */
@Composable
fun <D : BaseCardData> Screen(
    result: Result<List<D?>>,
    key: (D?) -> String,
    generateIconsColumn: (D) -> @Composable ColumnScope.() -> Unit,
    onDelete: (List<String>) -> Unit,
    onCreate: (Int) -> Unit,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    onUpdate: (D) -> Unit,
    dropdownOptions: Result<List<DropdownOptions?>> = Result.Success(emptyList()),
    modifier: Modifier = Modifier,
    addElements: (id: String) -> Unit = {},
) {
    val elements = when (result) {
        is Result.Success -> result.data
        is Result.Error -> emptyList()
        is Result.Loading -> {
            Spinner()
            emptyList()
        }
    }

    val dialogIsExpanded = remember { mutableStateOf(false) }
    val cameraDialogIsExpanded = remember { mutableStateOf(false) }
    val selectedCard = remember { mutableStateOf<D?>(null) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val dialogWidth = screenWidth * 0.95f
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .testTag("LazyColumn"),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.space_arrangement_small)
            )
        ) {
            items(
                items = elements,
                key = key
            ) { element ->
                element?.let {
                    BaseCard<D>(
                        data = it,
                        onAdd = {
                            selectedCard.value = it
                            val selection = selectedCard.value
                            selection?.let { addTo -> addElements(addTo.id) }
                        },
                        onCamera = {
                            selectedCard.value = it
                            cameraDialogIsExpanded.value = true
                        },
                        onDelete = {
                            selectedCard.value = it
                            onDelete(listOf(it.id))
                        },
                        onUpdate = {
                            selectedCard.value = it
                            dialogIsExpanded.value = true
                        },
                        iconsContent = generateIconsColumn(it),
                        dropdownOptions = dropdownOptions,
                        modifier = Modifier.testTag("BaseCard"),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_arrangement_small)))
        Counter(onCreate = onCreate)

        if (dialogIsExpanded.value) {
            selectedCard.value?.let {
                EditDialog(
                    selectedCard = selectedCard,
                    onFieldChange = onFieldChange,
                    onCancel = {
                        coroutineScope.launch {
                            dialogIsExpanded.value = false
                            selectedCard.value = null
                        }
                    },
                    onConfirm = {
                        coroutineScope.launch {
                            onUpdate(selectedCard.value!!)
                            dialogIsExpanded.value = false
                            selectedCard.value = null
                        }
                    },
                    dialogWidth = dialogWidth,
                    iconsContent = generateIconsColumn(it),
                    dropdownOptions = dropdownOptions,
                )
            }
        }

        if (cameraDialogIsExpanded.value) {
            selectedCard.value?.let {
                CameraDialog(
                    selectedCard = selectedCard,
                    onFieldChange = onFieldChange,
                    onCancel = {
                        coroutineScope.launch {
                            cameraDialogIsExpanded.value = false
                        }
                    },
                    onClick = {
                        coroutineScope.launch {
                            onUpdate(selectedCard.value!!)
                            cameraDialogIsExpanded.value = false
                            selectedCard.value = null
                        }
                    },
                    dialogWidth = dialogWidth,
                )
            }
        }
    }
}

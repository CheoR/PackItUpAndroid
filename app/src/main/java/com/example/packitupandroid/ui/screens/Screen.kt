package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.components.dialog.EditDialog
import com.example.packitupandroid.ui.components.spinner.Spinner
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.launch


/**
- * A composable function that displays a screen with a list of items and a counter at the bottom.
+ * A composable function that displays a screen containing a list of cards and a counter.
+ * The screen handles displaying a list of data fetched asynchronously, and provides a modal dialog
+ * for viewing and editing individual card details.
 *
- * @param result The result of the data fetching operation.
- * @param key The key to be used for the items in the list.
- * @param onCreate A callback that is invoked when the counter is created.
+ * @param result A [Result] object representing the outcome of a data fetching operation. It can be
+ *               [Result.Success] with the list of data, [Result.Error] indicating an error, or
+ *               [Result.Loading] to show a loading state.
+ * @param key A lambda function that takes an item of type [D] and returns a unique [String] key for
+ *            that item. This key is used by the [LazyColumn] for efficient list rendering.
+ * @param onCreate A callback function that is invoked when the counter is displayed. It receives
+ *                 the number of items in the list.
+ * @param onUpdate A callback function that is invoked when a card's data is confirmed updated in the dialog.
+ *                 It receives the updated [D] data.
+ * @param onFieldChange A callback function updates field changes to the [D] data when editing.
+ * @see Screen
+ * @see BaseScreenViewModel
 */
@Composable
fun <D : BaseCardData> Screen(
    result: Result<List<D?>>,
    key: (D?) -> String,
    onCreate: (Int) -> Unit,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    onUpdate: (D) -> Unit,
    modifier: Modifier = Modifier,
//    onDestroy: (T) -> Unit,
//    getDropdownOptions: (() -> List<QueryDropdownOptions>)? = null,
//    filterElements:( (id: String) -> Unit)? = null,
//    cardType: CardType = CardType.Default,
    //    viewModel: Screen2ViewModel<T> = viewModel(factory = PackItUpViewModelProvider.screen2ViewModelFactory<T>()),
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
                        onExpandCard = {
                            selectedCard.value = it
                            dialogIsExpanded.value = true
                        },
                        onCloseCard = {
                            dialogIsExpanded.value = false
                            selectedCard.value = null
                        },
                        modifier = Modifier.testTag("BaseCard")
                            .clickable {
                                selectedCard.value = it
                                dialogIsExpanded.value = true
                            }
                        //            onDestroy = onDestroy,
//            getDropdownOptions = getDropdownOptions,
//            filterElements = filterElements,
//            cardType = cardType,
//            modifier = Modifier.testTag("Screen BaseCard ${element.id}")
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
                )
            }
        }
    }
}


/**
 * Displays a generic error message screen.
 *
 * This composable is intended to be used when an error occurs that prevents
 * the proper rendering of a specific screen or content. It displays a simple
 * "Screen Error" message centered on the screen.
 *
 *  TODO: move to utils - This function should be moved to a more appropriate
 *  location, such as a dedicated `utils` package, for better organization and
 *  reusability. Consider adding more customizable parameters to allow for varying
 *  error messages and styling.
 *
 * Example Usage:
 * ```kotlin
 *  if (hasError) {
 *      Error()
 *  } else {
 *      // Display normal content
 *  }
 * ```
 */
@Composable
private fun Error() {
    // TODO: move to utils
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Screen Error")
    }
}

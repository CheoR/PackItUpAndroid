package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.card.BaseCard
import com.example.packitupandroid.ui.common.component.Counter
import com.example.packitupandroid.ui.common.component.Spinner
import com.example.packitupandroid.ui.common.dialog.CameraDialog
import com.example.packitupandroid.ui.common.dialog.DeleteDialog
import com.example.packitupandroid.ui.common.dialog.EditDialog
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
 * @param snackbarHostState The [SnackbarHostState] used to display snackbar messages.
 * @param coroutineScope The [CoroutineScope] used to launch coroutines.
 * @param addElements A callback function that is invoked when the user clicks on the add button. User is
 *  navigated to sub element filtered by data.id e.g. User selects `add` on a Box element, user then
 *  is redirected to ItemsScreen with only items filtered by Box.id that equals the Box user clicked on.
 * @param emptyListPlaceholder The string to display when the list is empty.
 * @param defaultDispatcher The default dispatcher used for coroutines.
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
    emptyListPlaceholder: String,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    dropdownOptions: Result<List<DropdownOptions?>>? = null,
    addElements: (id: String) -> Unit = {},
    defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    lazyListState: LazyListState = rememberLazyListState()
) {
    var elements by remember { mutableStateOf(emptyList<D?>()) }
    var filteredElements by remember { mutableStateOf(emptyList<D?>()) }

    val cameraDialogIsExpanded = remember { mutableStateOf(false) }
    val editDialogIsExpanded = remember { mutableStateOf(false) }
    val deleteDialogIsExpanded = remember { mutableStateOf(false) }
    val selectedCard = remember { mutableStateOf<D?>(null) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val dialogWidth = screenWidth * 0.95f
    val emptyListContentDescription = stringResource(R.string.empty_list)

    var searchQuery by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val debounce = remember {
        Debounce<String>(300L, coroutineScope) { query ->
            filteredElements = if (query.isBlank()) {
                elements
            } else {
                elements.filter {
                    it?.name?.contains(query, ignoreCase = true) == true
                }
            }
        }
    }

    LaunchedEffect(result) {
        when (result) {
            is Result.Success -> {
                elements = result.data
                filteredElements = result.data
            }

            is Result.Error -> {
                elements = emptyList()
                filteredElements = emptyList()
            }

            is Result.Loading -> {
                elements = emptyList()
                filteredElements = emptyList()
            }
        }
    }

    Column(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragEnd = {
                    if (!showBottomSheet) {
                        showBottomSheet = true
                    }
                },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    if (dragAmount < 0) {
                        // Dragging up
                        showBottomSheet = true
                    } else {
                        // Dragging down
                        showBottomSheet = false
                    }
                }
            )
        }
    ) {
        SearchField(
            searchQuery = searchQuery,
            onValueChange = {
                searchQuery = it
                debounce.invoke(it)
            },
        )
        when(result) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Spinner()
                }
            }
            is Result.Error -> {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "error: " + stringResource(R.string.empty_list, emptyListPlaceholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            is Result.Success -> {
                if(filteredElements.isEmpty()) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .semantics { contentDescription = emptyListContentDescription },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.no_element_found, emptyListPlaceholder),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .semantics { contentDescription = "$emptyListPlaceholder list" },
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_arrangement_small)),
                        state = lazyListState,
                    ) {
                        items(
                            items = filteredElements,
                            key = key
                        ) { element ->
                            element?.let {
                                BaseCard(
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
                                        deleteDialogIsExpanded.value = true
                                    },
                                    onUpdate = {
                                        selectedCard.value = it
                                        editDialogIsExpanded.value = true
                                    },
                                    iconsContent = generateIconsColumn(it),
                                    dropdownOptions = dropdownOptions,
                                    modifier = Modifier,
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier
            .height(dimensionResource(R.dimen.space_arrangement_small))
            .fillMaxWidth()
        )
        Counter(onCreate = onCreate)

        if (showBottomSheet) {
            DeliveryOptionsModalBottomSheet(
                sheetState = sheetState,
                coroutineScope = coroutineScope,
                onDismissRequest = {
                    showBottomSheet = false
                }
            )
        }

        if (editDialogIsExpanded.value) {
            selectedCard.value?.let {
                EditDialog(
                    selectedCard = selectedCard,
                    onFieldChange = onFieldChange,
                    onCancel = {
                        coroutineScope.launch {
                            editDialogIsExpanded.value = false
                            selectedCard.value = null
                        }
                    },
                    onConfirm = {
                        val name = selectedCard.value!!.name
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                onUpdate(selectedCard.value!!)
                            }
                            editDialogIsExpanded.value = false
                            selectedCard.value = null
                            snackbarHostState.showSnackbar("$name Updated")
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
                            withContext(Dispatchers.IO) {
                                onUpdate(selectedCard.value!!)
                            }
                            cameraDialogIsExpanded.value = false
                            selectedCard.value = null
                            snackbarHostState.showSnackbar("Image Updated")
                        }
                    },
                    dialogWidth = dialogWidth,
                )
            }
        }

        if (deleteDialogIsExpanded.value) {
            selectedCard.value?.let {
                DeleteDialog(
                    selectedCard = selectedCard,
                    onCancel = {
                        coroutineScope.launch {
                            deleteDialogIsExpanded.value = false
                            selectedCard.value = null
                            snackbarHostState.showSnackbar("Delete Canceled")
                        }
                    },
                    onConfirm = {
                        coroutineScope.launch {
                            val name = selectedCard.value!!.name
                            withContext(Dispatchers.IO) {
                                onDelete(listOf(selectedCard.value!!.id))
                            }
                            deleteDialogIsExpanded.value = false
                            selectedCard.value = null
                            snackbarHostState.showSnackbar("$name Deleted")
                        }
                    },
                    dialogWidth = dialogWidth,
                    iconsContent = generateIconsColumn(it),
                    dropdownOptions = dropdownOptions,
                )
            }
        }
    }
}


/**
 * Debounce class allows you to delay the execution of a given action until a certain amount of time has passed since the last invocation.
 * This is useful for scenarios like filtering user input, where you don't want to react to every keystroke, but only after the user has stopped typing for a while.
 *
 * @param T The type of the value that will be passed to the `onDebounce` function.
 * @param delayMillis The delay in milliseconds before the `onDebounce` function is executed.
 * @param coroutineScope The CoroutineScope within which the debounced action will be executed.
 * @param onDebounce The function to be executed after the delay has passed. This function receives the last emitted value of type T.
 */
private class Debounce<T>(private val delayMillis: Long, private val coroutineScope: CoroutineScope, private val onDebounce: (T) -> Unit) {
    private var debounceJob: Job? = null

    fun invoke(value: T) {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(delayMillis)
            onDebounce(value)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScreenLoadingPreview() {
    Screen<Item>(
        result = Result.Loading,
        key = { it?.id as String },
        generateIconsColumn = { {} },
        onDelete = {},
        onCreate = {},
        onFieldChange = { _, _, _ -> },
        onUpdate = {},
        emptyListPlaceholder = "Items",
        snackbarHostState = SnackbarHostState(),
        coroutineScope = CoroutineScope(Dispatchers.Main),
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenSuccessEmptyListPreview() {
    Screen<Item>(
        result = Result.Success(emptyList()),
        key = { it?.id as String },
        generateIconsColumn = { {} },
        onDelete = {},
        onCreate = {},
        onFieldChange = { _, _, _ -> },
        onUpdate = {},
        emptyListPlaceholder = "Items",
        snackbarHostState = SnackbarHostState(),
        coroutineScope = CoroutineScope(Dispatchers.Main),
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenSuccessNonEmptyListPreview() {
    val item = Item(
        id = "moo",
        name = "cow",
        value = 100.25,
    )
    val items = listOf(item)
    Screen(
        result = Result.Success(items),
        key = { it?.id as String },
        generateIconsColumn = { {} },
        onDelete = {},
        onCreate = {},
        onFieldChange = { _, _, _ -> },
        onUpdate = {},
        emptyListPlaceholder = "Items",
        snackbarHostState = SnackbarHostState(),
        coroutineScope = CoroutineScope(Dispatchers.Main),
    )
}

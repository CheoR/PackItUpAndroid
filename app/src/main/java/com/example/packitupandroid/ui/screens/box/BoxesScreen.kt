package com.example.packitupandroid.ui.screens.box

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.ViewModelProvider
import com.example.packitupandroid.ui.common.layout.Screen
import kotlinx.coroutines.CoroutineScope


/**
 * Composable function that displays a screen with a list of boxes.
 *
- * This function uses a LazyColumn to display the boxes managed by the [BoxesScreenViewModel].
- * It observes the boxes StateFlow from the ViewModel and updates the UI whenever the data changes.
+ * This function serves as the main entry point for displaying a list of boxes.
+ * It leverages the `BoxesScreenViewModel` to manage the box data and state, and uses the `Screen` composable
+ * to render the UI. It automatically updates the display when the underlying data changes.
+ *
+ * The function performs the following:
+ * 1. **Data Collection:** Collects the list of boxes from the `viewModel.elements` StateFlow using `collectAsState()`.
+ * 2. **UI Rendering:**  Utilizes the generic `Screen` composable to efficiently render the list of `Box` objects.
+ * 3. **Key Management:** Provides a unique key (`it?.id as String`) for each box to ensure efficient recomposition by Compose.
+ * 4. **Creation Handler:** Passes the `viewModel::create` function to the `Screen` composable to handle the creation of new boxes.
+ * 4. **FieldChange Handler:** Passes the `viewModel::onFieldChange` function to the `Screen` composable to handle changes to edit fields in [EditCard].
+ * 5. **State Observation:**  Reactively updates the UI whenever the `viewModel.elements` StateFlow emits a new list of boxes.
 *
 * @param addElements A callback function that is invoked when the user clicks on the add button.
 * @param snackbarHostState The [SnackbarHostState] used to display snackbar messages.
 * @param coroutineScope The [CoroutineScope] used to launch coroutines.
 * @param viewModel The [BoxesScreenViewModel] used to manage the data and state. Defaults to a ViewModel provided by the [ViewModelProvider].Factory.
 * @see Screen
 * @see BoxesScreenViewModel
 */
@Composable
fun BoxesScreen(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    viewModel: BoxesScreenViewModel = viewModel(factory = ViewModelProvider.Factory),
    addElements: ((id: String) -> Unit) = {},
) {
    val result by viewModel.elements.collectAsState()
    val dropdownOptions by viewModel.dropdownOptions.collectAsState()

    val onCreate = viewModel::create
    val onDelete = viewModel::delete
    val onFieldChange = viewModel::onFieldChange
    val onUpdate = viewModel::update
    val generateIconsColumn = viewModel::generateIconsColumn

    Screen(
        result = result,
        key = { it?.id as String },
        addElements = addElements,
        generateIconsColumn = generateIconsColumn,
        onCreate = onCreate,
        onDelete = onDelete,
        onFieldChange = onFieldChange,
        onUpdate = onUpdate,
        dropdownOptions = dropdownOptions,
        emptyListPlaceholder = stringResource(R.string.boxes),
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
    )
}

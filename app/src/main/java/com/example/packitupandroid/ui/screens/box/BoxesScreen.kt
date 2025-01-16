package com.example.packitupandroid.ui.screens.box

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.ui.screens.Screen
import com.example.packitupandroid.utils.Result


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
 * @param viewModel The [BoxesScreenViewModel] used to manage the data and state. Defaults to a ViewModel provided by the [PackItUpViewModelProvider].Factory.
+ * @see Screen
+ * @see BoxesScreenViewModel
 */
@Composable
fun BoxesScreen(
//    filterItemsByBoxId:((id: String) -> Unit)? = null,
//    getDropdownOptions: () -> List<QueryDropdownOptions>,
    viewModel: BoxesScreenViewModel = viewModel(factory = PackItUpViewModelProvider.Factory)
) {
    val result by viewModel.elements.collectAsState()
    val onCreate = viewModel::create
    val onFieldChange = viewModel::onFieldChange
    val onUpdate = viewModel::update

    Screen<Box>(
        result = result,
        key = { it?.id as String },
        onCreate = onCreate,
        onFieldChange = onFieldChange,
        onUpdate = onUpdate,
//        onDestroy = viewModel::destroy,
//        getDropdownOptions = getDropdownOptions,
//        filterElements = filterItemsByBoxId,
//        cardType = CardType.Box,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemsScreen(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val boxes = localDataSource.loadBoxes()
    Screen<Box>(
        key = { it?.id as String },
        result = Result.Success(boxes),
        onCreate = {},
        onUpdate = {},
        onFieldChange = {_, _, _ -> Unit},
//        onDestroy = {},
//        getDropdownOptions = { emptyList() },
    )
}

package com.example.packitupandroid.ui.components.strategyCard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.ui.PackItUpViewModelProvider
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditMode


//@Composable
//fun EditCard2(
//    modifier: Modifier = Modifier,
//    id: String? = null,
//    cardType: CardType = CardType.Default,
//    viewModel: EditCardViewModel = viewModel(factory = PackItUpViewModelProvider.editCardViewModelFactory(cardType, id)),
//) {
//    val uiState = viewModel.uiState.collectAsState().value
//    val onConfirm = viewModel::updateElement
////    val iconsContent: @Composable ColumnScope.() -> Unit = viewModel::iconsContent
//
//    var name by remember { mutableStateOf(uiState.name) }
//    var currentSelection by remember { mutableStateOf<String?>(uiState.currentSelection) }
//    var description by remember { mutableStateOf(uiState.description) }
//    var isFragile by remember { mutableStateOf(uiState.isFragile) }
//    var value by remember { mutableDoubleStateOf(uiState.value) }
//    val editableFields = uiState.editFields
//    val dropdownSelections = uiState.dropdownSelections
//
//    BaseCard2(
//        name = name,
//        modifier = modifier,
//        currentSelection = currentSelection,
//        dropdownSelections = dropdownSelections,
//        description = description,
//        isFragile = isFragile,
//        value = value,
//        editMode = EditMode.Edit,
//        editableFields = editableFields,
//        onFieldChange = viewModel::onFieldChange,
//        showActions = false,
//        iconsContent = { uiState.iconsContent },
////        iconsContent = { uiState.iconsContent },
////        iconsContent = {
////            IconBadge(
////                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
////                badgeCount = 1,
////                badgeContentDescription = "Number of items"
////            )
////        }
//    )
//}


//// TODO: REDO BELOW
//@Composable
//fun EditCard(
//    uiState: EditCardUiState,
//    onCancel: () -> Unit,
//    onConfirm: (BaseCardData) -> Unit,
//    modifier: Modifier = Modifier,
//    editMode: EditMode = EditMode.NoEdit,
//    iconsContent: @Composable ColumnScope.() -> Unit = {}
//) {
//
////    val image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
//
//    ElevatedCard(
//        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
//        modifier = modifier
//            .height(dimensionResource(R.dimen.card_height)),
//    ) {
//        Row {
//            Column {
//                iconsContent()
//            }
//            Column(
//                modifier = Modifier
//                    .weight(1f),
//            ) {
//                DataColumn2(
//                    name = uiState.name,
//                    description = uiState.description ?: "",
//                    isFragile = uiState.isFragile,
//                    value = uiState.value,
//                    editableFields =  uiState.editFields,
//                    onFieldChange = {_, _ -> Unit },
//                    editMode = editMode,
//                    currentSelection = null,
//                )
//            }
//            Column(
//                modifier = modifier
//                    .fillMaxHeight(),
//            ) {
//                Box(modifier = Modifier
//                    .fillMaxHeight()
//                    .size(24.dp)){}
//            }
//        }
//    }
//}

//@Preview
//@Composable
//fun PreviewEditCardNoEditItem() {
//    val uiState = EditCardUiState(
//        id = "id",
//        name = "name",
//        description = "description",
//        isFragile = true,
//        value = 1.0,
//        editFields = setOf(EditFields.Name, EditFields.Description, EditFields.IsFragile, EditFields.Value)
//    )
//    val onCancel = {}
//    val onConfirm: (BaseCardData) -> Unit = { println() }
//    EditCard(
//        uiState,
//        onCancel,
//        onConfirm,
//        editMode = EditMode.NoEdit,
//        iconsContent = {
//        IconBadge(
//            image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
//            badgeCount = 0,
//            badgeContentDescription = ""
//        )
//    },)
//}
//
//@Preview
//@Composable
//fun PreviewEditCardEditItem() {
//    val uiState = EditCardUiState(
//        id = "id",
//        name = "name",
//        description = "description",
//        isFragile = true,
//        value = 1.0,
//        editFields = setOf(EditFields.Name, EditFields.Description, EditFields.IsFragile, EditFields.Value)
//    )
//    val onCancel = {}
//    val onConfirm: (BaseCardData) -> Unit = { println() }
//    EditCard(
//        uiState,
//        onCancel,
//        onConfirm,
//        editMode = EditMode.NoEdit,
//        iconsContent = {
//            IconBadge(
//                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
//                badgeCount = 0,
//                badgeContentDescription = ""
//            )
//        },)
//}
//
//@Preview
//@Composable
//fun PreviewEditCardEditBox() {
//    val uiState = EditCardUiState(
//        id = "id",
//        name = "name",
//        description = "description",
//        isFragile = true,
//        value = 1.0,
//        editFields = setOf(EditFields.Name, EditFields.Description, EditFields.IsFragile, EditFields.Value)
//    )
//    val onCancel = {}
//    val onConfirm: (BaseCardData) -> Unit = { println() }
//    EditCard(
//        uiState,
//        onCancel,
//        onConfirm,
//        editMode = EditMode.NoEdit,
//        iconsContent = {
//            IconBadge(
//                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
//                badgeCount = 0,
//                badgeContentDescription = ""
//            )
//        },)
//}

package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditMode

sealed class ElementType {
    object Summary : ElementType()
    object Collection : ElementType()
    object Box : ElementType()
    object Item : ElementType()
}

sealed class ColumnIcon {
    // since can’t have property be either String or ImageVector type because Kotlin's
    // statically typed language, to know variable type at compile time.
    data class VectorIcon(val imageVector: ImageVector) : ColumnIcon()
    data class UriIcon(val uri: Int?) : ColumnIcon()
    data class UriStringIcon(val uri: String?) : ColumnIcon()
}

sealed class ActionColumnState(val icon: ImageVector) {
    object RightArrow : ActionColumnState(Icons.Default.ArrowForward)
    object ThreeDots : ActionColumnState(Icons.Default.MoreVert)
    object None : ActionColumnState(Icons.Default.CheckBoxOutlineBlank)
}

//sealed class IconColumnState {
//    data object Default : IconColumnState()
//    data class OneIcon(val icon: ColumnIcon, val badgeCount: Int? = null) : IconColumnState()
//    data class TwoIcons(
//        val icon1: ColumnIcon,
//        val badgeCount1: Int? = null,
//        val icon2: ColumnIcon,
//        val badgeCount2: Int? = null,
//    ) : IconColumnState()
//}

//sealed class CombinedMode {
//    object SummaryNonEditable : CombinedMode()
//    object SummaryEditable : CombinedMode()
//    object NotSummaryNonEditable : CombinedMode()
//    object NotSummaryEditable : CombinedMode()
//
//    companion object {
//        fun combine(viewMode: ViewMode, editMode: EditMode): CombinedMode {
//            return when (viewMode) {
//                ViewMode.SummaryCard -> when (editMode) {
//                    EditMode.NonEditable -> SummaryNonEditable
//                    EditMode.Editable -> SummaryEditable
//                }
//                ViewMode.NotSummaryCard -> when (editMode) {
//                    EditMode.NonEditable -> NotSummaryNonEditable
//                    EditMode.Editable -> NotSummaryEditable
//                }
//            }
//        }
//    }
//}

@Composable
fun<T: BaseCardData> BaseCard(
    data: T,
    onUpdate: (T) -> Unit,
    onDestroy: (T) -> Unit,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
    // TODO: add dropdown field
) {
    val expanded = remember { mutableStateOf(false) }
    val showEditCard = remember { mutableStateOf(false) }
    val showCameraCard = remember { mutableStateOf(false) }
    val showDeleteCard = remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(dimensionResource(R.dimen.card_height))
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small))),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.image_size_medium)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small)),
        ) {
            IconsColumn(
                icon1 = icon1,
                icon2 = icon2,
                badgeCount1 = badgeCount1,
                badgeCount2 = badgeCount2,
                isShowBadgeCount = cardType !is CardType.Item,
            )
            DataColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(horizontal = 4.dp),
                data = data,
                editMode = editMode,
                cardType = cardType,
            )
            ActionColumn(
                data = data,
                onClick = {},
                editMode = editMode,
                onCancel = {
                    expanded.value = false
                    showEditCard.value = false
                    showCameraCard.value = false
                    showDeleteCard.value = false
                },
                onEdit = {
                    onUpdate(it)
                    expanded.value = false
                    showCameraCard.value = false
                    showEditCard.value = false
                },
                onDelete = {
                    expanded.value = false
                    showCameraCard.value = false
                    showDeleteCard.value = false
                    onDestroy(data)
                },
                isExpanded = expanded,
                isShowEditCard = showEditCard,
                isShowCameraCard = showCameraCard,
                isShowDeleteCard = showDeleteCard,
                cardType = cardType,
            )
        }
    }
}

@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewItemBaseCardWithImage(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
    BaseCard(
        data = item,
        onUpdate = {},
        onDestroy = {},
    )
}

@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewItemBaseCardWithoutImage(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems()[1]
    BaseCard(
        data = item,
        onUpdate = {},
        onDestroy = {},
    )
}

@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewBoxBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val box = localDataSource.loadBoxes().first()
    BaseCard(
        data = box,
        onUpdate = {},
        onDestroy = {},
    )
}
// TODO: Fix

//@Preview(
//    showBackground = true,
//    group = "Default",
//)
//@Composable
//fun PreviewCollectionBaseCard(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    BaseCard(
//        data = collection,
//        onUpdate = {},
//        onDestroy = {},
//    )
//}

// Summary
@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewSummaryItemBaseCardWithImage(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
    BaseCard(
        data = item,
        cardType = CardType.Summary,
        onUpdate = {},
        onDestroy = {},
    )
}

@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewSummaryItemBaseCardWithoutImage(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems()[1]
    BaseCard(
        data = item,
        cardType = CardType.Summary,
        onUpdate = {},
        onDestroy = {},
    )
}

@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewSummaryBoxBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val box = localDataSource.loadBoxes().first()
    BaseCard(
        data = box,
        cardType = CardType.Summary,
        onUpdate = {},
        onDestroy = {},
    )
}

// TODO: Fix
//@Preview(
//    showBackground = true,
//    group = "Summary",
//)
//@Composable
//fun PreviewSummaryCollectionBaseCard(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    BaseCard(
//        data = collection,
//        cardType = CardType.Summary,
//        onUpdate = {},
//        onDestroy = {},
//    )
//}

// Editable

@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditItemBaseCardWithImage(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
    BaseCard(
        data = item,
        editMode = EditMode.Edit,
        onUpdate = {},
        onDestroy = {},
    )
}

@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditItemBaseCardWithoutImage(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems()[1]
    BaseCard(
        data = item,
        editMode = EditMode.Edit,
        onUpdate = {},
        onDestroy = {},
    )
}

@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditBoxBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val box = localDataSource.loadBoxes().first()
    BaseCard(
        data = box,
        editMode = EditMode.Edit,
        onUpdate = {},
        onDestroy = {},
    )
}

// TODO: Fix
//@Preview(
//    showBackground = true,
//    group = "Edit",
//)
//@Composable
//fun PreviewEditCollectionBaseCard(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    BaseCard(
//        data = collection,
//        editMode = EditMode.Edit,
//        onUpdate = {},
//        onDestroy = {},
//    )
//}

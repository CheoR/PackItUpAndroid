package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Summary

sealed class CardType {
    object Default : CardType()
    object Summary : CardType()
    object Collection : CardType()
    object Box : CardType()
    object Item : CardType()
}

sealed class EditMode {
    object NoEdit : EditMode()
    object Edit : EditMode()
}

sealed class EditFields {
    object None : EditFields()
    object Name : EditFields()
    object Description : EditFields()
    object IsFragile : EditFields()
    object Value : EditFields()
    object Dropdown: EditFields()
}

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
fun BaseCard(
    data: BaseCardData,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit? = {},
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
    editFields: Set<EditFields> = emptySet(),
) {
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
            when(data) {
                is BaseCardData.ItemData -> {
                    val image = data.item.imageUri
                    IconsColumn(
                        icon1 = if (image != null) ColumnIcon.UriIcon(image) else ColumnIcon.VectorIcon(Icons.Default.Label),
                        isShowBadgeCount = cardType is CardType.Summary
                    )
                }
                is BaseCardData.BoxData -> {
                    IconsColumn(
                        icon1 = ColumnIcon.VectorIcon(Icons.Default.Label),
                        badgeCount1 = data.box.items.size,
                    )
                }
                is BaseCardData.CollectionData -> {
                    IconsColumn(
                        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
                        icon2 = ColumnIcon.VectorIcon(Icons.Default.Label),
                        badgeCount1 = data.collection.boxes.size,
                        badgeCount2 = data.collection.boxes.sumOf { it.items.size },
                    )
                }
                is BaseCardData.SummaryData -> {
                    val (count, icon) = when(data.summary.id) {
                        "collections" -> Pair(data.summary.collections.size, Icons.Default.Category)
                        "boxes" -> Pair(data.summary.boxes.size, ImageVector.vectorResource(R.drawable.ic_launcher_foreground))
                        else -> Pair(data.summary.items.size, Icons.Default.Label)
                    }
                    IconsColumn(
                        icon1 = ColumnIcon.VectorIcon(icon),
                        badgeCount1 = count,
                    )
                }
            }

            // Call DataColumn with Modifier.weight(2f).fillMaxHeight()
            DataColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(horizontal = 4.dp),
                data = data,
                onUpdate = onUpdate,
                editFields = editFields,
                editMode = editMode,
                cardType = cardType,
            )

            ActionColumn(
                data = data,
                onClick = {},
                editMode = editMode,
                cardType = cardType,
                onUpdate = onUpdate,
                editFields = editFields,
                onDelete = onDelete,
            )
        }
    }
}

@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewItemBaseCardWithImage() {
    val item = Item(
        id = "884e104a-f6f5-45e9-9f40-4a4cf8ea8c3a",
        name = "oin oink ",
        description = "hola cola",
        value = 123.45,
        isFragile = true,
        imageUri = R.drawable.pug,
    )

    val editFields = setOf(
        EditFields.Name,
        EditFields.Description,
        EditFields.IsFragile,
        EditFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        onUpdate = {},
        onDelete = {},
        editFields = editFields,
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
    val item = localDataSource.loadItems().first()
    val editFields = setOf(
        EditFields.Name,
        EditFields.Description,
        EditFields.IsFragile,
        EditFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        onUpdate = {},
        onDelete = {},
        editFields = editFields,
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
    val editFields = setOf(
        EditFields.Name,
        EditFields.Description,
    )

    BaseCard(
        data = BaseCardData.BoxData(
            box = box,
        ),
        onUpdate = {},
        onDelete = {},
        editFields = editFields,
    )
}
@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewCollectionBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
    BaseCard(
        data = BaseCardData.CollectionData(
                collection = collection,
        ),
        onUpdate = {},
        onDelete = {},
        editFields = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.IsFragile,
            EditFields.Value,
        ),
    )
}

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
        data = BaseCardData.ItemData(
            item = item,
        ),
        cardType = CardType.Summary,
        onUpdate = {},
        onDelete = {},
        editFields = Item.editFields,
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
    val item = localDataSource.loadItems().first()

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        cardType = CardType.Summary,
        onUpdate = {},
        onDelete = {},
        editFields = Item.editFields,
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
        data = BaseCardData.BoxData(
            box = box
        ),
        cardType = CardType.Summary,
        onUpdate = {},
        onDelete = {},
        editFields = Box.editFields,
    )
}
@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewSummaryCollectionBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()

    BaseCard(
        data = BaseCardData.CollectionData(
            collection = collection,
        ),
        cardType = CardType.Summary,
        onUpdate = {},
        onDelete = {},
        editFields = Collection.editFields,
    )
}

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
        data = BaseCardData.ItemData(
            item = item,
        ),
        editMode = EditMode.Edit,
        onUpdate = {},
        onDelete = {},
        editFields = Item.editFields,
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
    val item = localDataSource.loadItems().first()

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        editMode = EditMode.Edit,
        onUpdate = {},
        onDelete = {},
        editFields = Item.editFields,
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
        data = BaseCardData.BoxData(
            box = box,
        ),
        editMode = EditMode.Edit,
        onUpdate = {},
        onDelete = {},
        editFields = Box.editFields,
    )
}
@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditCollectionBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
    BaseCard(
        data = BaseCardData.CollectionData(
            collection = collection,
        ),
        editMode = EditMode.Edit,
        onUpdate = {},
        onDelete = {},
        editFields = Collection.editFields,
    )
}

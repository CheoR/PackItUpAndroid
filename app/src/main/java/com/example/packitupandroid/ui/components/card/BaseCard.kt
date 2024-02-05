package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.screens.Summary

sealed class BaseCardData {
    data class ItemData(val item: Item) : BaseCardData()
    data class BoxData(val box: Box) : BaseCardData()
    data class CollectionData(val collection: Collection) : BaseCardData()

    data class SummaryData(val summary: Summary): BaseCardData()
}

sealed class CardType {
    object SummaryCard : CardType()
    object NotSummaryCard : CardType()
}
sealed class EditMode {
    object NonEditable : EditMode()
    object Editable : EditMode()
}

sealed class EditableFields {
    object Name : EditableFields()
    object Description : EditableFields()
    object IsFragile : EditableFields()
    object Value : EditableFields()
    object Dropdown: EditableFields()
}

sealed class ElementType {
    object Summary : ElementType()
    object Collection : ElementType()
    object Box : ElementType()
    object Item : ElementType()
}

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
    actionIcon: ImageVector,
    onCardClick: () -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isShowBadgeCount: Boolean = true,
    imageVector1: ImageVector? =  null,
    imageVector2: ImageVector? =  null,
    editMode: EditMode = EditMode.NonEditable,
    cardType: CardType = CardType.NotSummaryCard,
    editableFields: Set<EditableFields> = emptySet(),
) {
    Card(
        modifier = modifier
            .height(dimensionResource(R.dimen.card_height))
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small)))
            .clickable { onDelete() } , // onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.image_size_medium)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small)),
        ) {
            when (data) {
                is BaseCardData.ItemData -> {
                    IconsColumn(
                        imageUri = data.item.imageUri,
                        isShowBadgeCount = isShowBadgeCount,
                    )
                }
                is BaseCardData.BoxData -> {
                    IconsColumn(
                        imageVector1 = imageVector1,
                        firstBadgeCount = data.box.items.size,
                    )
                }
                is BaseCardData.CollectionData -> {
                    IconsColumn(
                        imageVector1 = imageVector1,
                        imageVector2 = imageVector2,
                        firstBadgeCount = data.collection.boxes.size,
                        secondBadgeCount = data.collection.boxes.sumOf { it.items.size }
                    )

                }
                is BaseCardData.SummaryData -> {
                    val count = when(data.summary.id) {
                        "collections" -> data.summary.collections.size
                        "boxes" -> data.summary.boxes.size
                        else -> data.summary.items.size
                    }
                    IconsColumn(
                        imageVector1 = imageVector1,
                        firstBadgeCount = count,
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
                onCheckedChange = {},
                onUpdate = onUpdate,
                editableFields = editableFields,
                editMode = editMode,
                cardType = cardType,
            )

            ActionColumn(
                onClick = {},
                elementType = when (data) {
                    is BaseCardData.ItemData -> ElementType.Item
                    is BaseCardData.BoxData -> ElementType.Box
                    is BaseCardData.CollectionData -> ElementType.Collection
                    else -> ElementType.Summary
                },
                editMode = editMode,
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
fun PreviewItemBaseCardWithImage() {
    val item = Item(
        id = "884e104a-f6f5-45e9-9f40-4a4cf8ea8c3a",
        name = "oin oink ",
        description = "hola cola",
        value = 123.45,
        isFragile = true,
        imageUri = R.drawable.pug,
    )

    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
        EditableFields.IsFragile,
        EditableFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {}
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
        EditableFields.IsFragile,
        EditableFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {}
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
    )

    BaseCard(
        data = BaseCardData.BoxData(
            box = box,
        ),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
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
        editableFields = setOf(
            EditableFields.Name,
            EditableFields.Description,
            EditableFields.IsFragile,
            EditableFields.Value,
        ),
        onCardClick = { },
        onUpdate = {},
        onDelete = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24)
    )
}

// Summary
@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewSummaryItemBaseCardWithImage() {
    val item = Item(
        id = "075b4463-c4cd-478a-8370-396712ad1ae7",
        name = "oin oink ",
        description = "hola cola",
        value = 123.45,
        isFragile = true,
        imageUri = R.drawable.pug,
    )

    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
        EditableFields.IsFragile,
        EditableFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        cardType = CardType.SummaryCard,
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
        EditableFields.IsFragile,
        EditableFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        cardType = CardType.SummaryCard,
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
    )

    BaseCard(
        data = BaseCardData.BoxData(
            box = box
        ),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        cardType = CardType.SummaryCard,
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
    )
    BaseCard(
        data = BaseCardData.CollectionData(
            collection = collection,
        ),
        editableFields = editableFields,
        onCardClick = { },
        onUpdate = {},
        onDelete = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        cardType = CardType.SummaryCard,
    )
}

// Editable

@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditItemBaseCardWithImage() {
    val item = Item(
        id = "aaacb24d-8aae-4822-81bd-3600b2d127f1",
        name = "oin oink ",
        description = "hola cola",
        value = 123.45,
        isFragile = true,
        imageUri = R.drawable.pug,
    )

    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
        EditableFields.IsFragile,
        EditableFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        editMode = EditMode.Editable,
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
        EditableFields.IsFragile,
        EditableFields.Value,
    )

    BaseCard(
        data = BaseCardData.ItemData(
            item = item,
        ),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        editMode = EditMode.Editable,
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
    val editableFields = setOf(
        EditableFields.Name,
        EditableFields.Description,
    )

    BaseCard(
        data = BaseCardData.BoxData(
            box = box,
        ),
        editableFields = editableFields,
        onCardClick = {},
        onUpdate = {},
        onDelete = {},
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        editMode = EditMode.Editable,
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
        editableFields = setOf(
            EditableFields.Name,
            EditableFields.Description,
            EditableFields.IsFragile,
            EditableFields.Value,
        ),
        onCardClick = { },
        onUpdate = {},
        onDelete = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        editMode = EditMode.Editable,
    )
}

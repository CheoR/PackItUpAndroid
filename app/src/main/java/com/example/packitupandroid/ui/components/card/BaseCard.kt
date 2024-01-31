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

sealed class ViewMode {
    object SummaryCard : ViewMode()
    object NotSummaryCard : ViewMode()
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
    modifier: Modifier = Modifier,
    data: BaseCardData,
    onCardClick: () -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: () -> Unit,
    editMode: EditMode = EditMode.Editable, //  .NonEditable,
    editableFields: Set<EditableFields> = emptySet(),
    imageVector1: ImageVector? =  null,
    imageVector2: ImageVector? =  null,
    actionIcon: ImageVector,
    viewMode: ViewMode = ViewMode.NotSummaryCard,
    isShowBadgeCount: Boolean = true,
) {
    Card(
        modifier = modifier
            .height(148.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onDelete() } , // onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
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
                        firstBadgeCount = count
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
                viewMode = viewMode,
            )

            ActionColumn(
                onClick = {},
                editMode = editMode,
                viewMode = viewMode,
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
        viewMode = ViewMode.SummaryCard,
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
        viewMode = ViewMode.SummaryCard,
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
        viewMode = ViewMode.SummaryCard,
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
        viewMode = ViewMode.SummaryCard,
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

//@Composable
//fun BaseCard(
//    modifier: Modifier = Modifier,
//    title: String,
//    description: String = "",
//    onCardClick: () -> Unit,
//    imageId: Int? = null,
//    ImageVector11: ImageVector1,
//    ImageVector12: ImageVector1? = null,
//    actionIcon: ImageVector1,
//    onactionIconClick: () -> Unit,
//    dropdownOptions: List<String>? = null,
//    value: Double = 0.00,
//    isFragile: Boolean = false,
//    onCheckedChange: () -> Unit,
//    firstBadgeCount: Int? = 0,
//    secondBadgeCount: Int? = 0,
//    isShowFragileAndValue: Boolean = true,
//) {
//    Card(
//        modifier = modifier
//            .height(148.dp)
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(8.dp))
////            .padding(dimensionResource(R.dimen.padding_small))
//            .clickable { onCardClick() },
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(dimensionResource(R.dimen.padding_small))
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(horizontal = 4.dp)
//                    .fillMaxHeight(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                when {
//                    imageId != null -> {
//                        Image(
//                            painter = painterResource(imageId),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(48.dp)
//                                .clip(RoundedCornerShape(8.dp))
//                        )
//                    }
//                    // Condition 'ImageVector11 != null' is always 'true'
//                    // but can't just do ImageVector11
//                    ImageVector11 != null -> {
////                        if(firstBadgeCount != 0) {
//                            BadgedBox(
//                                modifier = Modifier,
//                                badge = {
//                                    Badge(
//                                        modifier = Modifier
//                                            .offset(
//                                                dimensionResource(R.dimen.badge_x_offset),
//                                                dimensionResource(R.dimen.badge_y_offset)
//                                            ),
//                                    ) {
//                                        Text(firstBadgeCount.toString())
//                                    }
//                                }
//                            ) {
//                                Icon(
//                                    ImageVector1 = ImageVector11,
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(48.dp)
//                                        .clip(RoundedCornerShape(8.dp))
//                                )
//                            }
////                        } else {
////                            Icon(
////                                ImageVector1 = ImageVector11,
////                                contentDescription = null,
////                                modifier = Modifier
////                                    .size(48.dp)
////                                    .clip(RoundedCornerShape(8.dp))
////                            )
////                        }
//                    }
//                }
//
//                // Optional Image or icon2
//                ImageVector12?.let {
////                    if(secondBadgeCount != 0) {
//                        BadgedBox(
//                            modifier = Modifier,
//                            badge = {
//                                Badge(
//                                    modifier = Modifier
//                                    .offset(
//                                        dimensionResource(R.dimen.badge_x_offset),
//                                        dimensionResource(R.dimen.badge_y_offset)
//                                    ),
//                                ) {
//                                    Text(secondBadgeCount.toString())
//                                }
//                            }
//                        ) {
//                            Icon(
//                                ImageVector1 = it,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(48.dp)
//                                    .clip(RoundedCornerShape(8.dp)),
//                            )
//                        }
////                    } else {
////                        Icon(
////                            ImageVector1 = it,
////                            contentDescription = null,
////                            modifier = Modifier
////                                .size(48.dp)
////                                .clip(RoundedCornerShape(8.dp)),
////                        )
////                    }
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .weight(2f)
//                    .fillMaxHeight()
//            ) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.NameSmall,
//                    color = MaterialTheme.colorScheme.primary
//                )
//                dropdownOptions?.let {
//                    BasicTextField(
//                        value = dropdownOptions.first(),
//                        onValueChange = { },
//                        textStyle = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(4.dp)
//                    )
//                }
//                Text(
//                    text = description,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth(),
//                    maxLines = 3,
//                    overflow = TextOverflow.Ellipsis,
//                )
//
//                if(isShowFragileAndValue) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                    ) {
//                        Checkbox(
//                            checked = isFragile,
//                            onCheckedChange = { onCheckedChange() },
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text("Fragile")
//
//                        Spacer(modifier = Modifier.weight(1f))
//
//                        Text(
//                            text = value.formatValue(),
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.secondary
//                        )
//                    }
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                IconButton(
//                    onClick = { onactionIconClick() },
//                    modifier = Modifier
//                        .fillMaxHeight(),
//                    content = {
//                        Icon(
//                            ImageVector1 = actionIcon,
//                            contentDescription = "Icon Button",
//                            modifier = Modifier
//                                .size(24.dp)
//                        )
//                    }
//                )
//            }
//        }
//    }
//}
//
//

//@Preview(showBackground = true)
//@Composable
//fun PreviewCollectionCard() {
//    BaseCard(
//        title = "PreviewCollectionCard",
//        description = "individusal descrition for this Collections",
//        onCardClick = {},
//        ImageVector11 = ImageVector1.vectorResource(R.drawable.ic_launcher_foreground),
//        ImageVector12 = ImageVector1.vectorResource(R.drawable.baseline_label_24),
//        actionIcon = ImageVector1.vectorResource(R.drawable.baseline_more_vert_24),
//        onactionIconClick = { },
//        value = LocalDataSource().loadCollections().first().totalValue,
//        isFragile = true,
//        onCheckedChange = {},
//        firstBadgeCount = LocalDataSource().loadCollections().first().boxes.size,
//        secondBadgeCount = LocalDataSource().loadCollections().first().boxes.sumOf { it.items.size }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewBoxCard() {
//    BaseCard(
//        title = "PreviewBoxCard",
//        dropdownOptions = LocalDataSource().loadCollections().map { it.name },
//        description = "individusal descrition for this box",
//        onCardClick = {},
//        ImageVector11 = ImageVector1.vectorResource(R.drawable.baseline_label_24),
//        actionIcon =  ImageVector1.vectorResource(R.drawable.baseline_more_vert_24),
//        onactionIconClick = { },
//        value = LocalDataSource().loadBoxes().first().totalValue,
//        onCheckedChange = {},
//        firstBadgeCount = LocalDataSource().loadBoxes().first().items.size,
//    )
//}
//

//
//@Preview(showBackground = true)
//@Composable
//fun PreviewCollectionEditCard() {
//    BaseCard(
//        title = "PreviewCollectionEditCard",
//        description = "individusal descrition for this Collections",
//        onCardClick = {},
//        ImageVector11 = ImageVector1.vectorResource(R.drawable.ic_launcher_foreground),
//        ImageVector12 = ImageVector1.vectorResource(R.drawable.baseline_label_24),
//        actionIcon =  ImageVector1.vectorResource(R.drawable.baseline_more_vert_24),
//        onactionIconClick = { },
//        value = LocalDataSource().loadCollections().first().totalValue,
//        onCheckedChange = {},
//        firstBadgeCount = LocalDataSource().loadCollections().first().boxes.size,
//        secondBadgeCount = LocalDataSource().loadCollections().first().boxes.sumOf { it.items.size }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewBoxEditCard() {
//    BaseCard(
//        title = "PreviewBoxEditCard",
//        dropdownOptions = LocalDataSource().loadCollections().map { it.name },
//        description = "individusal descrition for this box individusal descrition for this box individusal descrition for this box",
//        onCardClick = {},
//        ImageVector11 = ImageVector1.vectorResource(R.drawable.baseline_label_24),
//        actionIcon =  ImageVector1.vectorResource(R.drawable.baseline_more_vert_24),
//        onactionIconClick = { },
//        value = LocalDataSource().loadBoxes().first().totalValue,
//        onCheckedChange = {},
//        firstBadgeCount = LocalDataSource().loadBoxes().first().items.size,
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewItemEditCard() {
//    BaseCard(
//        title = "PreviewItemEditCard",
//        dropdownOptions = LocalDataSource().loadBoxes().map { it.name },
//        description = "descriptions optional",
//        onCardClick = {},
//        ImageVector11 = Icons.Default.Home,
//        imageId = R.drawable.pug,
//        actionIcon =  ImageVector1.vectorResource(R.drawable.baseline_more_vert_24),
//        onactionIconClick = { },
//        value = LocalDataSource().loadCollections().first().totalValue,
//        isFragile = true,
//        onCheckedChange = {},
//    )
//}
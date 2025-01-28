package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.data.source.local.LocalDataSource
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

sealed class ActionColumnIcon(val icon: ImageVector) {
    object RightArrow : ActionColumnIcon(Icons.AutoMirrored.Filled.ArrowForward)
    object ThreeDots : ActionColumnIcon(Icons.Default.MoreVert)
    object None : ActionColumnIcon(Icons.Default.CheckBoxOutlineBlank)
}

typealias IconPair = Pair<ColumnIcon, ColumnIcon?>
typealias BadgeCountPair = Pair<Int, Int>

/**
 * A base composable card that displays information from a [BaseCardData] object.
 *
 * This card is designed to be a foundation for displaying various types of data in a card format.
 * It includes basic UI elements like ID and name, and provides callbacks for expanding and closing the card.
 *
 * @param data The [BaseCardData] object containing the data to display in the card.
 * @param onExpandCard A callback function that is invoked when the user clicks on the card to expand it.
 * @param onCloseCard A callback function that is invoked when the user clicks on the card to close it. (Not implemented in this example but included for potential future use).
 * @param modifier Modifier to be applied to the card. Defaults to filling the maximum width and setting a fixed height.
 *
 * @param D the type of data that the card will display. It must inherit from BaseCardData
 *
 * Example usage:
 * ```
 * data class MyCardData(override val id: String, override val name: String, val extraInfo: String) : BaseCardData
 *
 * @Composable
 * fun MyCard(data: MyCardData) {
 *     BaseCard(
 *         data = data,
 *         onExpandCard = { println("Card expanded") },
 *         onCloseCard = { println("Card closed") }
 */
@Composable
fun <D: BaseCardData>BaseCard(
    data: D,
    onExpandCard: () -> Unit,
    onCloseCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.card_height)),
    ) {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
        ) {
            Column(
                modifier = columnModifier,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = data.id,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Column(
                modifier = columnModifier,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = data.id,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

// TODO: FIx - look into compound pattern to hide/display bottom part of data column
// and to display/hide right column icon
@Composable
fun<T: BaseCardData> BaseCard(
    element: T,
    onUpdate: (T) -> Unit,
    onDestroy: (T) -> Unit,
    modifier: Modifier = Modifier,
//    getDropdownOptions: (() -> List<QueryDropdownOptions>)? = null,
    filterElements:( (id: String) -> Unit)? = null,
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
) {
    val expanded = remember { mutableStateOf(false) }
    val showEditCard = remember { mutableStateOf(false) }
    val showCameraCard = remember { mutableStateOf(false) }
    val showDeleteCard = remember { mutableStateOf(false) }

    val (icons, badgeCounts) = getIconsAndBadges(element)
    val icon1 = icons.first
    val icon2 = icons.second
    val badgeCount1 = badgeCounts.first
    val badgeCount2 = badgeCounts.second

    Card(
        modifier = modifier
            .height(dimensionResource(R.dimen.card_height))
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small)))
            .testTag("BaseCard ${element.id}"),
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
                element = element,
//                getDropdownOptions = getDropdownOptions,
                editMode = editMode,
                cardType = cardType,
            )
            ActionColumn(
                element = element,
                onClick = {},
                editMode = editMode,
                onCancel = {
                    expanded.value = false
                    showEditCard.value = false
                    showCameraCard.value = false
                    showDeleteCard.value = false
                },
                filterElements = filterElements,
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
                    onDestroy(element)
                },
                isExpanded = expanded,
                isShowEditCard = showEditCard,
                isShowCameraCard = showCameraCard,
                isShowDeleteCard = showDeleteCard,
                cardType = cardType,
//                getDropdownOptions = getDropdownOptions,
            )
        }
    }
}



@Composable
fun <T: BaseCardData> getIconsAndBadges(data: T): Pair<IconPair,BadgeCountPair> {
   return when(data) {
        is Item -> {
            // TODO: FIX
//            val icon = when (val imageUri = data.imageUri) {
//                is ImageUri.StringUri -> ColumnIcon.UriStringIcon(imageUri.uri)
//                is ImageUri.ResourceUri -> ColumnIcon.UriIcon(imageUri.resourceId)
//                null -> ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label)
//            }
            val icon1 = if(data.imageUri != null) ColumnIcon.UriStringIcon(data.imageUri) else  ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label)
            Pair(Pair(icon1, null), Pair(0, 0))
        }
        is Box -> {
            val icon1 = ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label)
            val badgeCount1 = data.item_count
            Pair(Pair(icon1, null), Pair(badgeCount1, 0))
        }
        is Collection -> {
            val icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground))
            val icon2 = ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label)
            val badgeCount1 = data.box_count
            val badgeCount2 = data.item_count
            Pair(Pair(icon1, icon2), Pair(badgeCount1, badgeCount2))
        }
        is Summary -> {
            val (icon1, badgeCount1) = when(data.id) {
                "collections" -> Pair(ColumnIcon.VectorIcon(Icons.Default.Category), data.collectionCount)
                "boxes" -> Pair(ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)), data.boxCount)
                else -> Pair(ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label), data.itemCount)
            }
            Pair(Pair(icon1, null), Pair(badgeCount1, 0))
        }

       else -> Pair(Pair(ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label), null), Pair(0, 0))
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
        element = item,
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
        element = item,
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
        element = box,
        onUpdate = {},
        onDestroy = {},
    )
}

// TODO: Fix - look up previews and how to get variations
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
        element = collection,
        onUpdate = {},
        onDestroy = {},
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
        element = item,
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
        element = item,
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
        element = box,
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
fun PreviewSummaryCollectionBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
    BaseCard(
        element = collection,
        cardType = CardType.Summary,
        onUpdate = {},
        onDestroy = {},
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
        element = item,
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
        element = item,
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
        element = box,
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
fun PreviewEditCollectionBaseCard(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
    BaseCard(
        element = collection,
        editMode = EditMode.Edit,
        onUpdate = {},
        onDestroy = {},
    )
}

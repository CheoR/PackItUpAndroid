package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.CollectionCard
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.ViewMode
import com.example.packitupandroid.ui.components.formatValue

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    collections: List<Collection> = emptyList(),
    boxes: List<Box> = emptyList(),
    items: List<Item> = emptyList(),
) {
    val collections = LocalDataSource().loadCollections()
    val boxes = LocalDataSource().loadBoxes()
    val items = LocalDataSource().loadItems()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small))
    ) {

        BaseCard(
            data = BaseCardData.CollectionData(
                collection = Collection(
                    id = "collection",
                    name = "collection",
                    description = "consist of boxes . . ",
                    boxes = boxes,
                    )
            ),
            onCardClick = {},
            onUpdate = {},
            onDelete = {},
            imageVector1 = ImageVector.vectorResource(R.drawable.baseline_category_24),
            actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24)
        )
//        BaseCard(
//            data = ,
//            onCardClick = {},
//            onUpdate = {},
//            onDelete = {},
//            actionIcon = Icons.Filled.ArrowForward,
//            viewMode = ViewMode.SummaryCard,
//        )
//       BaseCard(
//            name = stringResource(R.string.collections),
//            description = "Group boxes into collections . . ",
//            onCardClick = {},
//            imageVector1 = ImageVector.vectorResource(R.drawable.baseline_category_24),
//            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
//            onButtonIconClick = { },
//            value = LocalDataSource().loadItems().sumOf { it.value },
//            onCheckedChange = {},
//            firstBadgeCount = LocalDataSource().loadCollections().size,
//            isFragile = LocalDataSource().loadItems().any{ it.isFragile },
//            isShowFragileAndValue = false,
//        )
//        BaseCard(
//            title = stringResource(R.string.boxes),
//            description = "Group items into boxes . . ",
//            onCardClick = {},
//            imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
//            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
//            onButtonIconClick = { },
//            value = LocalDataSource().loadItems().sumOf { it.value },
//            onCheckedChange = {},
//            firstBadgeCount = LocalDataSource().loadBoxes().size,
//            isFragile = LocalDataSource().loadItems().any{ it.isFragile },
//            isShowFragileAndValue = false,
//        )
//        BaseCard(
//            title = stringResource(R.string.items),
//            dropdownOptions = LocalDataSource().loadBoxes().map { it.name },
//            description = "This is  your ",
//            onCardClick = {},
//            imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
//            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
//            onButtonIconClick = { },
//            value = LocalDataSource().loadItems().sumOf { it.value },
//            onCheckedChange = {},
//            firstBadgeCount = LocalDataSource().loadItems().size,
//            isFragile = LocalDataSource().loadItems().any{ it.isFragile },
//            isShowFragileAndValue = false,
//        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Checkbox(
                checked = LocalDataSource().loadItems().any{ it.isFragile },
                onCheckedChange = { },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Fragile")

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Total: ${LocalDataSource().loadItems().sumOf { it.value }.formatValue()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen() {
    val collections = LocalDataSource().loadCollections()
    val boxes = LocalDataSource().loadBoxes()
    val items = LocalDataSource().loadItems()
    SummaryScreen(
        collections = collections,
        boxes = boxes,
        items = items,
    )
}
package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.repository.LocalDataRepository
import com.example.packitupandroid.ui.components.formatValue

@Composable
fun DataColumn(
    modifier: Modifier = Modifier,
    data: BaseCardData,
    dropdownOptions: List<String>? = listOf(""),
    onCheckedChange: () -> Unit,
    editableFields: Set<EditableFields> = emptySet(),
    viewMode: ViewMode = ViewMode.NotSummaryCard,
) {
    var isFragile: Boolean = false
    var value: Double = 0.00
    var description: String = ""
    var name: String

    when (data) {
        is BaseCardData.ItemData -> {
            name = data.item.name
            description = data.item.description
            isFragile = data.item.isFragile
            value = data.item.value
        }

        is BaseCardData.BoxData -> {
            name = data.box.name
            description = data.box.description
            isFragile = data.box.isFragile
            value = data.box.value
        }

        is BaseCardData.CollectionData -> {
            name = data.collection.name
            description = data.collection.description
            isFragile = data.collection.isFragile
            value = data.collection.value
        }

        is BaseCardData.SummaryData -> {
            name = data.summary.name
            description = data.summary.description
            isFragile = data.summary.isFragile
            value = data.summary.value
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        dropdownOptions?.let {
            BasicTextField(
                value = dropdownOptions.first(), // Todo: display selected value if available else first
                onValueChange = { },
                textStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )

        if(viewMode == ViewMode.NotSummaryCard) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Checkbox(
                    checked = isFragile,
                    onCheckedChange = { onCheckedChange() },
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fragile")

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = value.formatValue(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewItemViewCardDataColumn(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    val item = localDataRepository.loadItems().first()
    DataColumn(
        data = BaseCardData.ItemData(
            item = item,
        ),
        onCheckedChange = {},
    )
}

@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewBoxViewCardDataColumn(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    val box = localDataRepository.loadBoxes().first()
    DataColumn(
        data = BaseCardData.BoxData(
            box = box,
        ),
        onCheckedChange = {}
    )
}
@Preview(
    showBackground = true,
    group = "Default",
)
@Composable
fun PreviewCollectionViewCardDataColumn(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    val collection = localDataRepository.loadCollections().first()
    DataColumn(
        data = BaseCardData.CollectionData(
            collection = collection,
        ),
        onCheckedChange = {},
    )
}

@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewItemSummaryCardDataColumn(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    val item = localDataRepository.loadItems().first()
    DataColumn(
        data = BaseCardData.ItemData(
            item = item,
        ),
        onCheckedChange = {},
        viewMode = ViewMode.SummaryCard,
    )
}

@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewBoxSummaryCardDataColumn(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    val box = localDataRepository.loadBoxes().first()
    DataColumn(
        data = BaseCardData.BoxData(
            box = box,
        ),
        onCheckedChange = {},
        viewMode = ViewMode.SummaryCard,
    )
}
@Preview(
    showBackground = true,
    group = "Summary",
)
@Composable
fun PreviewCollectionSummaryCardDataColumn(
    localDataRepository : LocalDataRepository = LocalDataRepository()
) {
    val collection = localDataRepository.loadCollections().first()
    DataColumn(
        data = BaseCardData.CollectionData(
            collection = collection,
        ),
        onCheckedChange = {},
        viewMode = ViewMode.SummaryCard,
    )
}






//    when (data) {
//        is BaseCardData.ItemData -> {
//            // Handle Item-specific data
//            // Access data.item for Item-specific properties
//            // Call onUpdate, onDelete as needed
//            if (editableFields.contains(EditableFields.Title)) {
//                // Title is editable
//                BasicTextField(
//                    value = data.item.name,
//                    onValueChange = {
//                        // Update logic for title
//                        onUpdate(data.copy(item = data.item.copy(name = it)))
//                    }
//                )
//            } else {
//                // Title is non-editable
//                IconsColumn(
//                    imageUri = data.item.imageUri,
//                    imageVector1 = imageVector1,
//                )
//                Text(text = data.item.name, style = MaterialTheme.typography.titleLarge)
//            }
//        }
//        is BaseCardData.BoxData -> {
//            // Handle Box-specific data
//            // Access data.box for Box-specific properties
//            // Call onUpdate, onDelete as needed
//        }
//        is BaseCardData.CollectionData -> {
//            // Handle Collection-specific data
//            // Access data.collection for Collection-specific properties
//            // Call onUpdate, onDelete as needed
//            if (editableFields.contains(EditableFields.Title)) {
//                // Title is editable
//                BasicTextField(
//                    value = data.collection.name,
//                    onValueChange = {
//                        // Update logic for title
//                        onUpdate(data.copy(collection = data.collection.copy(name = it)))
//                    }
//                )
//            } else {
//                // Title is non-editable
//                Text(text = data.collection.name, style = MaterialTheme.typography.titleLarge)
//            }
//        }
//
//        else -> {
//            Row {
//                Text(text="oopsies")
//            }
//        }
//    }
//}
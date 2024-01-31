package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.asCurrencyString
import com.example.packitupandroid.ui.components.parseCurrencyToDouble

private fun extractData(data: BaseCardData): List<Any> {
    var id: String
    var name: String
    var description: String
    var isFragile: Boolean
    var value: Double

    when (data) {
        is BaseCardData.ItemData -> {
            id = data.item.id
            name = data.item.name
            description = data.item.description
            isFragile = data.item.isFragile
            value = data.item.value
        }

        is BaseCardData.BoxData -> {
            id = data.box.id
            name = data.box.name
            description = data.box.description
            isFragile = data.box.isFragile
            value = data.box.value
        }

        is BaseCardData.CollectionData -> {
            id = data.collection.id
            name = data.collection.name
            description = data.collection.description
            isFragile = data.collection.isFragile
            value = data.collection.value
        }

        is BaseCardData.SummaryData -> {
            id = data.summary.id
            name = data.summary.name
            description = data.summary.description
            isFragile = data.summary.isFragile
            value = data.summary.value
        }
    }

    return listOf(id, name, description, isFragile, value)
}

@Composable
fun DataColumn(
    data: BaseCardData,
    onCheckedChange: () -> Unit,
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
    dropdownOptions: List<String>? = listOf(""),
    editableFields: Set<EditableFields> = emptySet(),
    editMode: EditMode = EditMode.Editable,
    viewMode: ViewMode = ViewMode.NotSummaryCard,
) {
    fun isEditable(field: EditableFields) = editMode == EditMode.Editable && editableFields.contains(field)

    val (_id, _name, _description, _isFragile, _value) = extractData(data)

    val id by remember { mutableStateOf(_id as String) }
    var name by remember { mutableStateOf(_name as String) }
    var description by remember { mutableStateOf(_description as String) }
    var isFragile by remember { mutableStateOf(_isFragile as Boolean) }
    var value by remember { mutableDoubleStateOf(_value as Double) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            onUpdate(BaseCardData.BoxData(
                Box(
                    id = id,
                    name = name,
                    description = description,
                )
            ))
        }) {
            Text(text="Update")
        }
        TextField(
            value = name,
            onValueChange = { name = it },
            textStyle = MaterialTheme.typography.titleSmall,
            enabled = isEditable(EditableFields.Name),
            modifier = Modifier
                .fillMaxWidth(),
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
//                Checkbox(
//                    checked = isFragile,
//                    onCheckedChange = { isFragile = it }, // onCheckedChange() },
//                    enabled = isEditable(EditableFields.IsFragile),
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Fragile")
//                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    value = value.asCurrencyString().toString(), //  as String,
//                    onValueChange = { value = it.toDouble() },
                    onValueChange = {
                        // Handle the case where the user enters an empty string
                        Log.i("VALUE CHANGE", "VALUE: ${it}")
                        value = if (it.isEmpty()) 0.0 else it.toDoubleOrNull() ?: value.parse // toDouble()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    enabled = isEditable(EditableFields.Value),
//                    keyboardActions = KeyboardType.Number,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,                )
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val box = localDataSource.loadBoxes().first()
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val box = localDataSource.loadBoxes().first()
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
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
package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.local.LocalDataSource
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
    onUpdate: (BaseCardData) -> Unit,
    modifier: Modifier = Modifier,
    dropdownOptions: List<String>? = listOf(""),
    editFields: Set<EditFields> = emptySet(),
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
) {
    fun isEditable(field: EditFields) = editMode == EditMode.Edit && editFields.contains(field)

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
//        Button(onClick = {
//            onUpdate(BaseCardData.CollectionData(
//                Collection(
//                    id = id,
//                    name = name,
//                    description = description,
//                )
//            ))
//        }) {
//            Text(text="Update")
//        }
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            textStyle = MaterialTheme.typography.titleSmall,
            enabled = isEditable(EditFields.Name),
            modifier = Modifier
                .fillMaxWidth(),
        )
        dropdownOptions?.let {
            BasicTextField(
                value = dropdownOptions.first(), // Todo: display selected value if available else first
                onValueChange = { description = it },
                textStyle = MaterialTheme.typography.bodySmall,
                enabled = isEditable(EditFields.Dropdown),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        BasicTextField(
            value = description,
            onValueChange = { description = it },
            textStyle = MaterialTheme.typography.bodySmall,
            enabled = isEditable(EditFields.Description),
            maxLines = 3,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        if(cardType != CardType.Summary) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Checkbox(
                    checked = isFragile,
                    onCheckedChange = { isFragile = it },
                    enabled = isEditable(EditFields.IsFragile),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fragile")
                Spacer(modifier = Modifier.weight(1f))
                BasicTextField(
                    value = value.asCurrencyString(),
                    onValueChange = {
                        // Handle the case where the user enters an empty string
                        value = if (it.isEmpty()) 0.0 else it.parseCurrencyToDouble()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    enabled = isEditable(EditFields.Value),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
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
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
    DataColumn(
        data = BaseCardData.ItemData(
            item = item,
        ),
        onUpdate = {},
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
        onUpdate = {},
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
        onUpdate = {},
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
        onUpdate = {},
        cardType = CardType.Summary,
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
        onUpdate = {},
        cardType = CardType.Summary,
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
        onUpdate = {},
        cardType = CardType.Summary,
    )
}

@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditableItemViewCardDataColumn(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val item = localDataSource.loadItems().first()
    DataColumn(
        data = BaseCardData.ItemData(
            item = item,
        ),
        onUpdate = {},
        editMode = EditMode.Edit,
        editFields = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
            EditFields.IsFragile,
            EditFields.Value,
        ),
    )
}

@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditableBoxViewCardDataColumn(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val box = localDataSource.loadBoxes().first()
    DataColumn(
        data = BaseCardData.BoxData(
            box = box,
        ),
        onUpdate = {},
        editMode = EditMode.Edit,
        editFields = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
        ),
    )
}
@Preview(
    showBackground = true,
    group = "Edit",
)
@Composable
fun PreviewEditableCollectionViewCardDataColumn(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val collection = localDataSource.loadCollections().first()
    DataColumn(
        data = BaseCardData.CollectionData(
            collection = collection,
        ),
        onUpdate = {},
        editMode = EditMode.Edit,
        editFields = setOf(
            EditFields.Name,
            EditFields.Description,
        ),
    )
}

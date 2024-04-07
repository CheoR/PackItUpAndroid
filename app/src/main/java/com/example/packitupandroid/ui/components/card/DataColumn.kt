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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.utils.asCurrencyString

@Composable
fun DataColumn(
    data: BaseCardData,
    modifier: Modifier = Modifier,
    dropdownOptions: List<String>? = listOf(""),
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = data.name,
            onValueChange = {},
            textStyle = MaterialTheme.typography.titleSmall,
            enabled = editMode == EditMode.Edit,
            modifier = Modifier
                .fillMaxWidth(),
        )
        dropdownOptions?.let {
            BasicTextField(
                value = dropdownOptions.first(), // Todo: display selected value if available else first
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodySmall,
                enabled = editMode == EditMode.Edit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        BasicTextField(
            value = data.description,
            onValueChange = {},
            textStyle = MaterialTheme.typography.bodySmall,
            enabled = editMode == EditMode.Edit,
            maxLines = 3,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        if(cardType !is CardType.Summary) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Checkbox(
                    checked = data.isFragile,
                    onCheckedChange = {},
                    enabled = editMode == EditMode.Edit,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fragile")
                Spacer(modifier = Modifier.weight(1f))
                BasicTextField(
                    value = data.value.asCurrencyString(),
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodySmall,
                    enabled = editMode == EditMode.Edit,
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
        data = item,
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
        data = box,
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
        data = collection,
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
        data = item,
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
        data = box,
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
        data = collection,
        cardType = CardType.Summary,
    )
}

package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditMode
import com.example.packitupandroid.utils.asCurrencyString

@Composable
fun<T: BaseCardData> DataColumn(
    element: T,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
) {

    val expanded = remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = element.name,
            onValueChange = {},
            textStyle = MaterialTheme.typography.titleSmall,
            enabled = editMode == EditMode.Edit,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                // TODO - fix - create func to get object name from id
                Text(
                    maxLines = 1,
                    text = when(element) {
                        is Item -> element.boxId.toString()
                        is Box -> element.collectionId.toString()
                        else -> ""
                    }
                )
            }
        }
        BasicTextField(
            // TODO: fix
            value = element.description ?: "missing",
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
                    checked = element.isFragile,
                    onCheckedChange = {},
                    enabled = editMode == EditMode.Edit,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fragile")
                Spacer(modifier = Modifier.weight(1f))
                BasicTextField(
                    value = element.value.asCurrencyString(),
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodySmall,
                    enabled = editMode == EditMode.Edit,
                    singleLine = true,
                )
            }
        }
    }
}

// TODO: fix

//@Preview(
//    showBackground = true,
//    group = "Default",
//)
//@Composable
//fun PreviewItemViewCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val item = localDataSource.loadItems().first()
//    DataColumn(
//        element = item,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group = "Default",
//)
//@Composable
//fun PreviewBoxViewCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val box = localDataSource.loadBoxes().first()
//    DataColumn(
//        element = box,
//    )
//}
//@Preview(
//    showBackground = true,
//    group = "Default",
//)
//@Composable
//fun PreviewCollectionViewCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    DataColumn(
//        element = collection,
//    )
//}

//@Preview(
//    showBackground = true,
//    group = "Summary",
//)
//@Composable
//fun PreviewItemSummaryCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val item = localDataSource.loadItems().first()
//    DataColumn(
//        element = item,
//        cardType = CardType.Summary,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group = "Summary",
//)
//@Composable
//fun PreviewBoxSummaryCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val box = localDataSource.loadBoxes().first()
//    DataColumn(
//        element = box,
//        cardType = CardType.Summary,
//    )
//}
//@Preview(
//    showBackground = true,
//    group = "Summary",
//)
//@Composable
//fun PreviewCollectionSummaryCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    DataColumn(
//        element = collection,
//        cardType = CardType.Summary,
//    )
//}

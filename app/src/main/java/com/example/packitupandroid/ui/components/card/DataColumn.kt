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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditMode
import com.example.packitupandroid.utils.asCurrencyString

@Composable
fun<T: BaseCardData> DataColumn(
    element: T,
    modifier: Modifier = Modifier,
    getDropdownOptions: (() -> List<QueryDropdownOptions>)? = null,
    editMode: EditMode = EditMode.NoEdit,
    cardType: CardType = CardType.Default,
) {

    val options: List<QueryDropdownOptions> = getDropdownOptions?.invoke() ?: emptyList()
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
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Name Field"
                    this[SemanticsProperties.Text] = listOf(AnnotatedString(element.name))
                },
        )
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    maxLines = 1,
                    text = when(element) {
                        is Item -> options.firstOrNull { it.id == element.boxId.toString() }?.name ?: " "
                        is Box -> options.firstOrNull { it.id == element.collectionId.toString() }?.name ?: " "
                        else -> ""
                    }
                )
            }
        }
        BasicTextField(
            // TODO: fix
            value = element.description ?: "",
            onValueChange = {},
            textStyle = MaterialTheme.typography.bodySmall,
            enabled = editMode == EditMode.Edit,
            maxLines = 3,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Description Field"
                    this[SemanticsProperties.Text] = listOf(AnnotatedString(element.description ?: ""))
                },
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
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Fragile Checkbox"
                        },
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
                    modifier = Modifier
                        .semantics {
                        contentDescription = "Value Field"
                        this[SemanticsProperties.Text] = listOf(AnnotatedString(element.value.asCurrencyString() ?: ""))
                    },
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

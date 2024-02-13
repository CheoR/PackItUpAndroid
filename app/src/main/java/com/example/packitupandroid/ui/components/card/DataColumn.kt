package com.example.packitupandroid.ui.components.card

import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Summary
import com.example.packitupandroid.ui.components.asCurrencyString
import com.example.packitupandroid.ui.components.parseCurrencyToDouble

@Composable
fun DataColumn(
//    data: BaseCardData,
    data: MutableState<BaseCardData>,
    modifier: Modifier = Modifier,
    onUpdate: (BaseCardData) -> Unit = {},
//    dropdownOptions: List<String>? = listOf(""),
    editMode: EditMode = EditMode.NoEdit,
) {
//    fun isEditable(field: EditFields) = editMode == EditMode.Edit && data.editFields.contains(field)

    fun isEditable(field: EditFields) = editMode == EditMode.Edit && data.value.editFields.contains(field)
//    val localData = remember { mutableStateOf(data) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = data.value.name,
            onValueChange = {
                Log.i("DC: NAME", it.toString())
                Log.i("DC: NAME BEFORE DATA", data.value.name.toString())
                data.value.name = it
                onUpdate(data.value)
                Log.i("DC: NAME AFTER DATA", data.value.name.toString())
            },
            textStyle = MaterialTheme.typography.titleSmall,
            enabled = isEditable(EditFields.Name),
            modifier = Modifier
                .fillMaxWidth(),
        )
//        BasicTextField(
//            value = data.name,
//            onValueChange = {
//                Log.i("DC: NAME", it.toString())
//                Log.i("DC: NAME BEFORE DATA", data.name.toString())
//                Log.i("DC: NAME BEFORE LOCAL", localData.value.name.toString())
//                localData.value.name = it
//                data.name = it
//                onUpdate(data)
//                Log.i("DC: NAME AFTER DATA", data.name.toString())
//                Log.i("DC: NAME AFTER AFTER", localData.value.name.toString())
//            },
//            textStyle = MaterialTheme.typography.titleSmall,
//            enabled = isEditable(EditFields.Name),
//            modifier = Modifier
//                .fillMaxWidth(),
//        )
//        dropdownOptions?.let {
//            BasicTextField(
//                value = dropdownOptions.first(), // Todo: display selected value if available else first
//                onValueChange = {
//                    data.description = it
//                                },
//                textStyle = MaterialTheme.typography.bodySmall,
//                enabled = isEditable(EditFields.Dropdown),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(4.dp)
//            )
//        }
//        BasicTextField(
//            value = data.description,
//            onValueChange = {
//                data.description = it
//                            },
//            textStyle = MaterialTheme.typography.bodySmall,
//            enabled = isEditable(EditFields.Description),
//            maxLines = 3,
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth(),
//        )

        if(data.value !is Summary) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Checkbox(
//                    checked = localData.value.isFragile , // data.isFragile,
//                    checked = data.value.isFragile,
                    checked = data.value.isFragile,
                    onCheckedChange = {
                        Log.i("DC: CHECKBOX BOOLEAN", it.toString())
//                        Log.i("DC: CHECKBOX BEFORE DATA", data.isFragile.toString())
                        Log.i("DC: CHECKBOX BEFORE DATA", data.value.isFragile.toString())
//                        Log.i("DC: CHECKBOX BEFORE LOCAL", localData.value.isFragile.toString())
                        data.value.isFragile = it
//                        data.isFragile = it
//                        localData.value.isFragile = it
//                        onUpdate(data.value)
                        onUpdate(data.value)
//                        Log.i("DC: CHECKBOX AFTER DATA", data.isFragile.toString())
                        Log.i("DC: CHECKBOX AFTER DATA", data.value.isFragile.toString())
//                        Log.i("DC: CHECKBOX AFTER AFTER", localData.value.isFragile.toString())
                    },
                    enabled = isEditable(EditFields.IsFragile),
                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Fragile")
//                Spacer(modifier = Modifier.weight(1f))
//                BasicTextField(
//                    value = data.value.asCurrencyString(),
//                    onValueChange = {
//                        // Handle the case where the user enters an empty string
//                        val value = if (it.isEmpty()) 0.0 else it.parseCurrencyToDouble()
//                        data.value = value // if (it.isEmpty()) 0.0 else it.parseCurrencyToDouble()
//                    },
//                    textStyle = MaterialTheme.typography.bodySmall,
//                    enabled = isEditable(EditFields.Value),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Number,
//                        imeAction = ImeAction.Done
//                    ),
//                    singleLine = true,
//                )
            }
        }
    }
}

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
//        data = item,
//        onUpdate = {},
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
//        data = box,
//        onUpdate = {},
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
//        data = collection,
//        onUpdate = {},
//    )
//}
//
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
//        data = item,
//        onUpdate = {},
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
//        data = box,
//        onUpdate = {},
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
//        data = collection,
//        onUpdate = {},
//        cardType = CardType.Summary,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group = "Edit",
//)
//@Composable
//fun PreviewEditableItemViewCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val item = localDataSource.loadItems().first()
//    DataColumn(
//        data = item,
//        onUpdate = {},
//        editMode = EditMode.Edit,
//        editFields = item.editFields,
//    )
//}
//
//@Preview(
//    showBackground = true,
//    group = "Edit",
//)
//@Composable
//fun PreviewEditableBoxViewCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val box = localDataSource.loadBoxes().first()
//    DataColumn(
//        data = box,
//        onUpdate = {},
//        editMode = EditMode.Edit,
//        editFields = box.editFields,
//    )
//}
//@Preview(
//    showBackground = true,
//    group = "Edit",
//)
//@Composable
//fun PreviewEditableCollectionViewCardDataColumn(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val collection = localDataSource.loadCollections().first()
//    DataColumn(
//        data = collection,
//        onUpdate = {},
//        editMode = EditMode.Edit,
//        editFields = collection.editFields,
//    )
//}

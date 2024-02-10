package com.example.packitupandroid.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditFields
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType

@Composable
fun EditCard(
    data: BaseCardData,
    onUpdate: (BaseCardData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.Edit,
    cardType: CardType = CardType.Default,
    editFields: Set<EditFields> = emptySet(),
    // TODO: add dropdown
) {
    var updatedData by remember { mutableStateOf(data) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
        verticalArrangement = Arrangement.Center,
    ){
        Spacer(modifier = Modifier.weight(2f))
        BaseCard(
            data = updatedData,
            onUpdate = { updatedData2 ->
                Log.i("EDITITEMCARD", updatedData.toString())
                Log.i("EDITITEMCARD2", updatedData2.toString())
            },
            editMode = editMode,
            cardType = cardType,
            editFields = editFields,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            Log.i("EDIT ITEM CARD", updatedData.toString())
            onUpdate(updatedData)
        }) {
            Text(text = "Update")
        }
        AddConfirmCancelButton(button = ButtonType.Cancel, onClick = onCancel)
    }
}
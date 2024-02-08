package com.example.packitupandroid.ui.components

import androidx.compose.runtime.Composable
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditMode

@Composable
fun ItemCard(
    item: Item,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: (String) -> Unit,
    editMode: EditMode = EditMode.NoEdit,
    // TODO: add dropdown
) {
    BaseCard(
        data = BaseCardData.ItemData(item),
        onUpdate = onUpdate,
        onDelete = { onDelete(item.id) },
        editMode = editMode,
        cardType = CardType.Item,
        editFields = Item.editFields,
    )
}



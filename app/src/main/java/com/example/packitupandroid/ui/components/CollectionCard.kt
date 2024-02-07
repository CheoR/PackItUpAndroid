package com.example.packitupandroid.ui.components

import androidx.compose.runtime.Composable
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditFields
import com.example.packitupandroid.ui.components.card.EditMode

@Composable
fun CollectionCard(
    collection: Collection,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: (String) -> Unit,
    editMode: EditMode = EditMode.NoEdit,
) {
    BaseCard(
        data = BaseCardData.CollectionData(collection),
        editMode = editMode,
        onUpdate = onUpdate,
        onDelete = { onDelete(collection.id) },
        cardType = CardType.Collection,
        editFields = setOf(
            EditFields.Name,
            EditFields.Description,
        ),
    )
}

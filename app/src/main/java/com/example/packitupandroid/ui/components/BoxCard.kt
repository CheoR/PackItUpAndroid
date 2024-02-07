package com.example.packitupandroid.ui.components

import androidx.compose.runtime.Composable
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditFields
import com.example.packitupandroid.ui.components.card.EditMode

@Composable
fun BoxCard(
    box: Box,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: (String) -> Unit,
    editMode: EditMode = EditMode.NoEdit,
    // TODO: add dropdown
) {
    BaseCard(
        data = BaseCardData.BoxData(box),
        onUpdate = onUpdate,
        onDelete = { onDelete(box.id) },
        editMode = editMode,
        cardType = CardType.Box,
        editFields = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
        ),
    )
}

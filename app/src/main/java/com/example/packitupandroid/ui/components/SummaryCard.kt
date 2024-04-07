package com.example.packitupandroid.ui.components

import androidx.compose.runtime.Composable
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.utils.CardType
import com.example.packitupandroid.utils.EditMode

@Composable
fun SummaryCard(
    summary: Summary,
    onUpdate: (BaseCardData) -> Unit,
    onDestroy: () -> Unit,
    editMode: EditMode = EditMode.NoEdit,
) {
    BaseCard(
        data = summary,
        onUpdate = onUpdate,
        onDestroy = onDestroy,
        editMode = editMode,
        cardType = CardType.Summary,
    )
}

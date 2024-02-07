package com.example.packitupandroid.ui.components

import androidx.compose.runtime.Composable
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.screens.Summary

@Composable
fun SummaryCard(
    summary: Summary,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: () -> Unit,
    editMode: EditMode = EditMode.NoEdit,
) {
    BaseCard(
        data = BaseCardData.SummaryData(summary),
        onUpdate = onUpdate,
        onDelete = onDelete,
        editMode = editMode,
        cardType = CardType.Summary,
        editFields = emptySet(),
    )
}

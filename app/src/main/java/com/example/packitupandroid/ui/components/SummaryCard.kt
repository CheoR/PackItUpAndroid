package com.example.packitupandroid.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Summary
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditMode

@Composable
fun SummaryCard(
    summary: Summary,
    onUpdate: (BaseCardData) -> Unit,
    onDestroy: () -> Unit,
    editMode: EditMode = EditMode.NoEdit,
) {
    Text(text = "summary")
//    BaseCard(
//        data = summary,
//        onUpdate = onUpdate,
//        onDestroy = onDestroy,
//        editMode = editMode,
//        cardType = CardType.Summary,
//    )
}

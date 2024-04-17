package com.example.packitupandroid.ui.components.card

import androidx.compose.runtime.Composable
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.utils.EditMode

@Composable
fun <T: BaseCardData> SummaryCard(
    summary: T,
    onUpdate: (T) -> Unit,
    onDestroy: (T) -> Unit,
    editMode: EditMode = EditMode.NoEdit,
) {
    BaseCard(
        data = summary,
        onUpdate = onUpdate,
        onDestroy = onDestroy,
    )
}

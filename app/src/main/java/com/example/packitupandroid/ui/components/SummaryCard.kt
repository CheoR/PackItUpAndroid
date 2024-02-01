package com.example.packitupandroid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.components.card.ViewMode
import com.example.packitupandroid.ui.screens.Summary

@Composable
fun SummaryCard(
    summary: Summary,
    onUpdate: (Summary) -> Unit,
    onDelete: () -> Unit,
    onCardClick: () -> Unit,
    editMode: EditMode = EditMode.NonEditable,
) {
    BaseCard(
        data = BaseCardData.SummaryData(summary),
        editMode = editMode,
        onCardClick = onCardClick,
        editableFields = setOf(),
        onUpdate = {},
        onDelete = onDelete,
        actionIcon = Icons.Filled.ArrowForward,
        // TODO: make imgageVectors into enum?
        imageVector1 = when(summary.id) {
            "collections" -> ImageVector.vectorResource(R.drawable.baseline_category_24)
            "boxes" -> ImageVector.vectorResource(R.drawable.ic_launcher_foreground)
            else -> ImageVector.vectorResource(R.drawable.baseline_label_24)
        },
        viewMode = ViewMode.SummaryCard,
    )
}

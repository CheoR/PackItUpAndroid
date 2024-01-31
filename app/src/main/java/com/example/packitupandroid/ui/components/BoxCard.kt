package com.example.packitupandroid.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.packitupandroid.R
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.components.card.EditableFields

@Composable
fun BoxCard(
    box: Box,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: (String) -> Unit,
    onCardClick: () -> Unit,
    editMode: EditMode = EditMode.Editable,
    // TODO: add dropdown
) {
    BaseCard(
        data = BaseCardData.BoxData(box),
        // TODO: add dropdown
        editMode = editMode,
        onCardClick = onCardClick,
        editableFields = setOf(
            EditableFields.Name,
            EditableFields.Description,
            EditableFields.Dropdown,
        ),
        onUpdate = onUpdate,
        onDelete = { onDelete(box.id) },
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        // TODO: make imgageVectors into enum?
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
    )
}

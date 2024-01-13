package com.example.packitupandroid.ui.components

import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.EditMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.packitupandroid.R
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.EditableFields

@Composable
fun BoxCard(
    box: Box,
    onUpdate: (Box) -> Unit,
    onDelete: (Box) -> Unit,
    onCardClick: () -> Unit,
    editMode: EditMode = EditMode.NonEditable,
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
        onUpdate = { data ->
            if (data is BaseCardData.BoxData) onUpdate(data.box) else {}
        },
        onDelete = { data ->
            if (data is BaseCardData.BoxData) onDelete(data.box) else {}
        },
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        // TODO: make imgageVectors into enum?
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
    )
}

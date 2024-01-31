package com.example.packitupandroid.ui.components

import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.components.card.EditableFields
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.packitupandroid.R
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.card.BaseCardData

@Composable
fun ItemCard(
    item: Item,
    onUpdate: (BaseCardData) -> Unit,
    onDelete: (String) -> Unit,
    onCardClick: () -> Unit,
    editMode: EditMode = EditMode.NonEditable,
    // TODO: add dropdown
) {
    BaseCard(
        data = BaseCardData.ItemData(item),
        // TODO: add dropdown
        editMode = editMode,
        onCardClick = onCardClick,
        editableFields = setOf(
            EditableFields.Name,
            EditableFields.Description,
            EditableFields.Dropdown,
            EditableFields.IsFragile,
            EditableFields.Value,
        ),
        onUpdate = onUpdate,
        onDelete = { onDelete(item.id) },
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        // TODO: make imgageVectors into enum?
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
    )
    // TODO: add different base case if in editmode here?
    // TODO: derive some properties so would not need to pass others?
}


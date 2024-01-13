package com.example.packitupandroid.ui.components

import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.EditMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.packitupandroid.R
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.EditableFields

@Composable
fun CollectionCard(
    collection: Collection,
    onUpdate: (Collection) -> Unit,
    onDelete: (Collection) -> Unit,
    onCardClick: () -> Unit,
    editMode: EditMode = EditMode.NonEditable,
) {
    BaseCard(
        data = BaseCardData.CollectionData(collection),
        editMode = editMode,
        onCardClick = onCardClick,
        editableFields = setOf(
            EditableFields.Name,
            EditableFields.Description,
        ),
        onUpdate = { data ->
            if (data is BaseCardData.CollectionData) onUpdate(data.collection) else {}
        },
        onDelete = { data ->
            if (data is BaseCardData.CollectionData) onDelete(data.collection) else {}
        },
        actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        // TODO: make imgageVectors into enum?
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
    )
}

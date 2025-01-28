package com.example.packitupandroid.ui.components.strategyCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.EditMode

@Composable
fun BaseCard2(
    name: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    currentSelection: String? = null,
    dropdownSelections: List<String?> = emptyList(),
    isFragile: Boolean = false,
    value: Double = 0.0,
    editMode: EditMode = EditMode.NoEdit,
//    iconsContent: ColumnIcon.() -> Unit, // @Composable ColumnScope.() -> Unit = {},
    iconsContent: @Composable ColumnScope.() -> Unit = {},
    showActions: Boolean = true,
    onFieldChange: (EditFields, String) -> Unit = {_, _ -> Unit},
    editableFields: Set<EditFields> = emptySet(),
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
            .height(dimensionResource(R.dimen.card_height)),
    ) {
        Row {
            Column {
                iconsContent()
            }
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                DataColumn2(
                    name,
                    modifier,
                    description,
                    isFragile,
                    value,
                    onFieldChange,
                    editMode,
                    currentSelection,
                    dropdownSelections,
                    editableFields
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                if(showActions) {
                    ActionColumn2(
                        onAdd = {},
                        onEdit = {},
                        onDelete = {},
                        onCamera = {}
                    )
                } else {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .size(24.dp)){}
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    group = "Item",
)
@Composable
fun PreviewItemBaseCardCanEdit() {
    var name by remember { mutableStateOf("Item1") }
    var currentSelection by remember { mutableStateOf("Box1") }
    var description by remember { mutableStateOf("Item 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }
    val dropdownSelections = listOf("Box1", "Box2", "Box3")
    val editableFields = setOf(
        EditFields.Name,
        EditFields.Dropdown,
        EditFields.Description,
        EditFields.IsFragile,
        EditFields.Value
    )

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BaseCard2(
            name = name,
            modifier = modifier,
            currentSelection = currentSelection,
            dropdownSelections = dropdownSelections,
            description = description,
            isFragile = isFragile,
            value = value,
            editMode = EditMode.Edit,
            editableFields = editableFields,
            onFieldChange = onFieldChange,
            showActions = false,
            iconsContent = {
                // TODO: CHANGE TO DB IMAGE, DEFAULT BADGE WITH NO COUNT
                IconBadge(
                    image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                    badgeCount = 1,
                    badgeContentDescription = "Number of items"
                )
            }
        )
    }
}

@Preview(
    showBackground = true,
    group = "Item",
)
@Composable
fun PreviewItemBaseCardNoEdit() {
    var name by remember { mutableStateOf("Item1") }
    var currentSelection by remember { mutableStateOf("Box1") }
    var description by remember { mutableStateOf("Item 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }
    val dropdownSelections = listOf("Box1", "Box2", "Box3")
    val editableFields = setOf(
        EditFields.Name,
        EditFields.Dropdown,
        EditFields.Description,
        EditFields.IsFragile,
        EditFields.Value
    )

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BaseCard2(
            name = name,
            modifier = modifier,
            currentSelection = currentSelection,
            dropdownSelections = dropdownSelections,
            description = description,
            isFragile = isFragile,
            value = value,
            editMode = EditMode.NoEdit,
            editableFields = editableFields,
            onFieldChange = onFieldChange,
            showActions = false,
            iconsContent = {
                IconBadge(
                    image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                    badgeCount = 1,
                    badgeContentDescription = "Number of items"
                )
            }
        )
    }
}

@Preview(
    showBackground = true,
    group = "Box",
)
@Composable
fun PreviewBoxBaseCardCanEdit() {
    val badgeCount1 = 1
    val badgeCount2 = 2
    val vectorImage1 = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
    val vectorImage2 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
    val drawableImage = ImageContent.DrawableImage(R.drawable.pug)

    var name by remember { mutableStateOf("Box1") }
    var currentSelection by remember { mutableStateOf("Box1") }
    var description by remember { mutableStateOf("Box 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }
    val dropdownSelections = listOf("Collection1", "Collection2", "Collection3")
    val editableFields = setOf(
        EditFields.Name,
        EditFields.Dropdown,
        EditFields.Description,
    )

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BaseCard2(
            name = name,
            modifier = modifier,
            currentSelection = currentSelection,
            dropdownSelections = dropdownSelections,
            description = description,
            isFragile = isFragile,
            value = value,
            editMode = EditMode.Edit,
            editableFields = editableFields,
            onFieldChange = onFieldChange,
            showActions = false,
            iconsContent = {
                IconBadge(
                    image = vectorImage2,
                    badgeCount = badgeCount2,
                    badgeContentDescription = "Number of items"
                )
            }
        )
    }
}


@Preview(
    showBackground = true,
    group = "Box",
)
@Composable
fun PreviewBoxBaseCardNoEdit() {
    val badgeCount1 = 1
    val badgeCount2 = 2
    val vectorImage1 = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
    val vectorImage2 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
    val drawableImage = ImageContent.DrawableImage(R.drawable.pug)

    var name by remember { mutableStateOf("Box1") }
    var currentSelection by remember { mutableStateOf("Box1") }
    var description by remember { mutableStateOf("Box 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }
    val dropdownSelections = listOf("Collection1", "Collection2", "Collection3")
    val editableFields = setOf(
        EditFields.Name,
        EditFields.Dropdown,
        EditFields.Description,
    )

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BaseCard2(
            name = name,
            modifier = modifier,
            currentSelection = currentSelection,
            dropdownSelections = dropdownSelections,
            description = description,
            isFragile = isFragile,
            value = value,
            editMode = EditMode.NoEdit,
            editableFields = editableFields,
            onFieldChange = onFieldChange,
            showActions = false,
            iconsContent = {
                IconBadge(
                    image = vectorImage2,
                    badgeCount = badgeCount2,
                    badgeContentDescription = "Number of items"
                )
            }
        )
    }
}


@Preview(
    showBackground = true,
    group = "Collection",
)
@Composable
fun PreviewCollectionBaseCardCanEdit() {
    val badgeCount1 = 1
    val badgeCount2 = 2
    val vectorImage1 = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
    val vectorImage2 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
    val drawableImage = ImageContent.DrawableImage(R.drawable.pug)

    var name by remember { mutableStateOf("Collection1") }
    var currentSelection by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("Collection 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }
    val dropdownSelections = emptyList<String>()
    val editableFields = setOf(
        EditFields.Name,
        EditFields.Description,
    )

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BaseCard2(
            name = name,
            modifier = modifier,
            currentSelection = currentSelection,
            dropdownSelections = dropdownSelections,
            description = description,
            isFragile = isFragile,
            value = value,
            editMode = EditMode.Edit,
            editableFields = editableFields,
            onFieldChange = onFieldChange,
            showActions = false,
            iconsContent = {
                IconBadge(
                    image = vectorImage1,
                    badgeCount = badgeCount1,
                    badgeContentDescription = "Number of boxes"
                )
                IconBadge(
                    image = vectorImage2,
                    badgeCount = badgeCount2,
                    badgeContentDescription = "Number of items"
                )
            }
        )
    }
}

@Preview(
    showBackground = true,
    group = "Collection",
)
@Composable
fun PreviewCollectionBaseCardNoEdit() {
    val badgeCount1 = 1
    val badgeCount2 = 2
    val vectorImage1 = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
    val vectorImage2 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
    val drawableImage = ImageContent.DrawableImage(R.drawable.pug)

    var name by remember { mutableStateOf("Collection1") }
    var currentSelection by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("Collection 1 description") }
    var isFragile by remember { mutableStateOf(false) }
    var value by remember { mutableDoubleStateOf(1.97) }
    val dropdownSelections = emptyList<String>()
    val editableFields = setOf(
        EditFields.Name,
        EditFields.Description,
    )

    val onFieldChange: (EditFields, String) -> Unit = { field, newValue ->
        when (field) {
            EditFields.Name -> name = newValue
            EditFields.Dropdown -> currentSelection = newValue
            EditFields.Description -> description = newValue
            EditFields.IsFragile -> isFragile = newValue.toBoolean()
            EditFields.Value -> value = newValue.toDoubleOrNull() ?: 0.0
            EditFields.ImageUri -> TODO()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BaseCard2(
            name = name,
            modifier = modifier,
            currentSelection = currentSelection,
            dropdownSelections = dropdownSelections,
            description = description,
            isFragile = isFragile,
            value = value,
            editMode = EditMode.NoEdit,
            editableFields = editableFields,
            onFieldChange = onFieldChange,
            showActions = false,
            iconsContent = {
                IconBadge(
                    image = vectorImage1,
                    badgeCount = badgeCount1,
                    badgeContentDescription = "Number of boxes"
                )
                IconBadge(
                    image = vectorImage2,
                    badgeCount = badgeCount2,
                    badgeContentDescription = "Number of items"
                )
            }
        )
    }
}





//@Preview(
//    showBackground = true,
//    backgroundColor = 0x9FFF9993,
//    device = "id:pixel_5",
//    showSystemUi = true,
//    widthDp = 500,
//    heightDp = 500,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Preview
//@Composable
//fun PreviewCollectionBaseCard()  {
//    val badgeCount1 = 1
//    val badgeCount2 = 2
//    val vectorImage1 = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground)
//    val vectorImage2 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
//    val drawableImage = ImageContent.DrawableImage(R.drawable.pug)
//
//    BaseCard(
//        name = "CollectionName",
//        description = "Collection contains boxes and thier items sg sfgdf. Descriptiondfdf Descriptionaa Description Description Description Description Description Description Description Description Description Description Description ",
//        iconsContent = {
//            IconBadge(
//                image = vectorImage1,
//                badgeCount = badgeCount1,
//                badgeContentDescription = "Number of boxes"
//            )
//            IconBadge(
//                image = vectorImage2,
//                badgeCount = badgeCount2,
//                badgeContentDescription = "Number of items"
//            )
//        },
//        isFragile = true,
//        value = 10.0,
//    )
//}
//
//@Preview
//@Composable
//fun PreviewCollectionBaseCard()  {
//    val badgeCount1 = 1
//    val vectorImage1 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
//
//    BaseCard(
//        name = "Box Name",
//        currentSelection = "Collection Name",
//        description = "Box contains thier items sg sfgdf. Descriptiondfdf Descriptionaa Description Description Description Description Description Description Description Description Description Description Description ",
//        iconsContent = {
//            IconBadge(
//                image = vectorImage1,
//                badgeCount = badgeCount1,
//                badgeContentDescription = "Number of items"
//            )
//        }
//    )
//}
//
//@Preview
//@Composable
//fun PreviewBoxBaseCardWithoutDBImage()  {
//    val vectorImage1 = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label)
//
//    BaseCard(
//        name = "Item Name",
//        currentSelection = "Box Name",
//        description = "Item is for sg sfgdf. Descriptiondfdf Descriptionaa Description Description Description Description Description Description Description Description Description Description Description ",
//        iconsContent = {
//            IconBadge(
//                image = vectorImage1,
//                badgeContentDescription = "Number of items"
//            )
//        }
//    )
//}
//
//@Preview
//@Composable
//fun PreviewItemBaseCardWithDBImage()  {
//    val bitmapString24x24 = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAQAAAD3EAaaAAAAYUlEQVR42mP8z8AARwiPr6+rv3//A8qk4eX//7jJADwIBAP9NBwEhoGOwL9qVCz/wbMYwOiBVgw3NmD9GJReQ7EbFjYCFhgApAyAAQnQAHA1AAEBAAAAAQABAgQBAw=="
//    val bitmapImage = ImageContent.BitmapStringImage(bitmapString24x24)
//
//    BaseCard(
//        name = "Item Name",
//        currentSelection = "Box Name",
//        description = "Item is for sg sfgdf. Descriptiondfdf Descriptionaa Description Description Description Description Description Description Description Description Description Description Description ",
//        iconsContent = {
//            IconBadge(
//                image = bitmapImage,
//                badgeContentDescription = "Number of items"
//            )
//        }
//    )
//}

package com.example.packitupandroid.ui.common.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.card.elements.IconBadge
import com.example.packitupandroid.ui.common.card.elements.ImageContent
import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.Result
import com.example.packitupandroid.utils.asCurrencyString
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


private enum class HorizontalDragValue { START, END }

/**
 * A base composable card that displays information from a [BaseCardData] object.
 *
 * This card is designed to be a foundation for displaying various types of data in a card format.
 * It includes basic UI elements like ID and name, and provides callbacks for expanding, closing, deleting, and updating the card.
 *
 * @param data The [BaseCardData] object containing the data to display in the card.
 * @param iconsContent A composable function to display icons within the card.
 * @param onDelete A callback function that is invoked when the user clicks on the delete button.
 * @param onUpdate A callback function that is invoked when the user clicks on the edit button.
 * @param modifier Modifier to be applied to the card. Defaults to filling the maximum width and setting a fixed height.
 * @param onAdd A callback function that is invoked when the user clicks on the add button. User is
 *  navigated to sub element filtered by data.id e.g. User selects `add` on a Box element, user then
 *  is redirected to ItemsScreen with only items filtered by Box.id that equals the Box user clicked on.
 * @param dropdownOptions A [Result] object representing the state of the dropdown options [DropdownOptions].
 * @param D the type of data that the card will display. It must inherit from BaseCardData
 *
 * Example usage:
 * ```
 * data class MyCardData(override val id: String, override val name: String, val extraInfo: String) : BaseCardData
 *
 * @Composable
 * fun MyCard(data: MyCardData) {
 *     BaseCard(
 *         data = data,
 *         onExpandCard = { println("Card expanded") },
 *         onCloseCard = { println("Card closed") }
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <D: BaseCardData>BaseCard(
    data: D,
    iconsContent: @Composable ColumnScope.() -> Unit,
    onCamera: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier,
    dropdownOptions: Result<List<DropdownOptions?>>? = null,
    onAdd: () -> Unit = {},
) {
    val fragileCheckboxContentDescription = stringResource(R.string.fragile_checkbox)
    val valueFieldContentDescription = stringResource(R.string.value)
    val editButtonContentDescription = stringResource(R.string.edit)
    val deleteButtonContentDescription = stringResource(R.string.delete)
    val addButtonContentDescription = stringResource(R.string.add)
    val cameraButtonContentDescription = stringResource(R.string.camera)
    val favoriteButtonContentDescription = stringResource(R.string.favorite)
    val baseCardContentDescription = stringResource(R.string.base_card)

    val dropdownOptionsList = if (dropdownOptions != null) {
        when (dropdownOptions) {
            is Result.Success -> dropdownOptions.data
            is Result.Error -> emptyList()
            is Result.Loading -> emptyList()
        }
    } else { emptyList() }

    val selectedBox: DropdownOptions? = if (dropdownOptions != null) {
        // TODO: try to clean this up so don't need to do checks against model data class
        when (data) {
            is Item -> dropdownOptionsList.find { it?.id == data.boxId }
            is Box -> dropdownOptionsList.find { it?.id == data.collectionId }
            else -> null
        }
    } else { null }

    val scope = rememberCoroutineScope()
    var boxSize by remember { mutableFloatStateOf(0F) }
    val density = LocalDensity.current

    val anchors = DraggableAnchors {
        HorizontalDragValue.START at 0f
        HorizontalDragValue.END at -boxSize / 100
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = HorizontalDragValue.START,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(durationMillis = 500),
            decayAnimationSpec = exponentialDecay(),
        )
    }

    val iconsBackgroundColor by animateColorAsState(
        when (state.targetValue) {
            HorizontalDragValue.START -> Color.Transparent
            HorizontalDragValue.END -> MaterialTheme.colorScheme.primary
        },
        label = "change color"
    )

    SideEffect {
        state.updateAnchors(anchors)
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.card_height))
            .semantics { contentDescription = baseCardContentDescription },
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inversePrimary),
        ) {
            Column {
               iconsContent()
             }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
            ) {
                // TODO: combine with [EditCard] to remove duplication and use flag to
                // display [DataColumn] in edit mode or not
                BasicTextField(
                    value = data.name,
                    onValueChange = {},
                    maxLines = 1,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.displayMedium,
                )
                if (dropdownOptions != null) {
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {},
                    ) {
                        TextField(
                            readOnly = true,
                            value = selectedBox?.name ?: "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                BasicTextField(
                    value = data.description ?: "",
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyMedium,
                    minLines = 3,
                    maxLines = 3,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = data.isFragile,
                        onCheckedChange = {},
                        enabled = false,
                        modifier = Modifier
                            .semantics {
                                contentDescription = fragileCheckboxContentDescription
                            },
                    )
                    Text(stringResource(R.string.fragile))
                    Spacer(modifier = Modifier.weight(1f))
                    BasicTextField(
                        value = data.value.asCurrencyString(),
                        onValueChange = {},
                        textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Right, color = MaterialTheme.colorScheme.onSurfaceVariant),
                        enabled = false,
                        modifier = Modifier.semantics { contentDescription = valueFieldContentDescription },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .graphicsLayer { boxSize = size.width }
                    .offset {
                        IntOffset(
                            x = state.requireOffset().roundToInt(),
                            y = 0,
                        )
                    }
                    .anchoredDraggable(state, Orientation.Horizontal)
                    .background(iconsBackgroundColor),
                verticalArrangement = Arrangement.Center,
            ) {
                when (state.targetValue) {
                    HorizontalDragValue.START -> {
                        IconButton(onClick = {} ) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = favoriteButtonContentDescription,
                                tint = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                    }
                    HorizontalDragValue.END -> {
                        if (data is Item) {
                            IconButton(onClick = {
                                scope.launch {
                                    onCamera()
                                    state.animateTo(HorizontalDragValue.START)
                                } }) {
                                Icon(Icons.Default.CameraAlt, contentDescription = cameraButtonContentDescription, tint = Color.White)
                            }
                        } else {
                            IconButton(onClick = {
                                scope.launch {
                                    onAdd()
                                    state.animateTo(HorizontalDragValue.START)
                                } }) {
                                Icon(Icons.Default.Add, contentDescription = addButtonContentDescription, tint = Color.White)
                            }
                        }
                        IconButton(onClick = {
                            scope.launch {
                                onUpdate()
                                state.animateTo(HorizontalDragValue.START)
                            } }) {
                            Icon(Icons.Default.Edit, contentDescription = editButtonContentDescription, tint = Color.White)
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    onDelete()
                                    state.animateTo(HorizontalDragValue.START)
                                }
                            }) {
                            Icon(Icons.Default.Delete, contentDescription = deleteButtonContentDescription, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBaseCard() {
    val card = Item(
        id = "1",
        name = "Box 1",
        description = "Box  moo 1",
        value = 10.0,
        isFragile = true,
    )

    BaseCard(
        data = card,
        iconsContent = {
            IconBadge(
                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
                badgeContentDescription = "Default Item Badge",
                badgeCount = 0,
            )
        },
        onCamera = {},
        onDelete = {},
        onUpdate = {},
        dropdownOptions = Result.Success(emptyList()),
    )
}

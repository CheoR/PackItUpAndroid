package com.example.packitupandroid.ui.common.component.card

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
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
    dropdownOptions: Result<List<DropdownOptions?>>,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit = {},
) {
    val dropdownOptionsList = when (dropdownOptions) {
        is Result.Success -> dropdownOptions.data
        is Result.Error -> emptyList()
        is Result.Loading -> emptyList()
    }
    val selectedBox: DropdownOptions? = when(data) {
        is Item -> dropdownOptionsList.find { it?.id == data.boxId }
        is Box -> dropdownOptionsList.find { it?.id == data.collectionId }
        else -> null
    }


    val scope = rememberCoroutineScope()
    var boxSize by remember { mutableFloatStateOf(0F) }
    val density = LocalDensity.current

    val anchors = DraggableAnchors {
        HorizontalDragValue.START at 0f
        HorizontalDragValue.END at -boxSize /100
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
            HorizontalDragValue.START -> MaterialTheme.colorScheme.surfaceBright
            HorizontalDragValue.END -> MaterialTheme.colorScheme.tertiary
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
            .height(dimensionResource(R.dimen.card_height)),
    ) {
        Row(
            modifier = Modifier,
        ) {
            Column {
                iconsContent()
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                // TODO: combine with [EditCard] to remove duplication and use flag to
                // display [DataColumn] in edit mode or not
                BasicTextField(
                    value = data.name,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = {},
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedBox?.name ?: "",
                        onValueChange = {},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                        modifier = Modifier.menuAnchor(),
                    )

                    ExposedDropdownMenu(
                        expanded = false,
                        onDismissRequest = {},
                    ) {
                        dropdownOptionsList.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    if (option != null) {
                                        Text(text = option.name)
                                    }
                                },
                                onClick = {},
                            )
                        }
                    }
                }
                BasicTextField(
                    value = data.description ?: "",
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyMedium,
                    minLines = 3,
                    maxLines = 3,
                    enabled = false,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = data.isFragile,
                        onCheckedChange = {},
                        enabled = false,
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Fragile Checkbox"
                            },
                    )
                    Text(stringResource(R.string.fragile))
                    Spacer(modifier = Modifier.weight(1f))
                    BasicTextField(
                        value = data.value.asCurrencyString(),
                        onValueChange = {},
                        // TODO: merge MaterialTheme.typeograph and TextAlign.Right
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Right,
                        ),
//                        textStyle = MaterialTheme.typography.bodySmall,
                        enabled = false,
                        modifier = Modifier.semantics { contentDescription = "Value Field" },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .graphicsLayer { boxSize = size.width }
                    .offset {
                        IntOffset(
                            x = state
                                .requireOffset()
                                .roundToInt(),
                            y = 0,
                        )
                    }
                    .anchoredDraggable(state, Orientation.Horizontal)
                    .background(iconsBackgroundColor),
                verticalArrangement = Arrangement.Center,
            ) {
                when(state.targetValue) {
                    HorizontalDragValue.START -> {
                        IconButton(onClick = {} ) {
                            Icon(
                                Icons.Default.ArrowBackIosNew,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                    }
                    HorizontalDragValue.END -> {
                        if(data is Item) {
                            IconButton(onClick = {
                                scope.launch {
                                    onCamera()
                                    state.animateTo(HorizontalDragValue.START)
                                } }) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White)
                            }
                        } else {
                            IconButton(onClick = {
                                scope.launch {
                                    onAdd()
                                    state.animateTo(HorizontalDragValue.START)
                                } }) {
                                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                            }
                        }
                        IconButton(onClick = {
                            scope.launch {
                                onUpdate()
                                state.animateTo(HorizontalDragValue.START)
                            } }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    onDelete()
                                    state.animateTo(HorizontalDragValue.START)
                                }
                            }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

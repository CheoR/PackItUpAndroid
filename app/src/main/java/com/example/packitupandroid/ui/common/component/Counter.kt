package com.example.packitupandroid.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


/**
 * A composable function that displays a counter with increment, decrement, and reset functionality.
 *
 * @param onCreate A callback that is invoked when the counter is created.
 * @param modifier The modifier to be applied to the counter.
 * @param initialCount The initial value of the counter.
 * @param buttonSize The size of the increment and decrement buttons.
 * @param buttonShape The shape of the increment and decrement buttons.
 * @param buttonColor The color of the increment and decrement buttons.
 */
@Composable
fun Counter(
    onCreate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    initialCount: Int = 0,
    buttonSize: Dp = 40.dp,
    buttonShape: RoundedCornerShape = RoundedCornerShape(dimensionResource(R.dimen.roundness_small)),
    buttonColor: Color = MaterialTheme.colorScheme.primary,
) {
    var count by remember { mutableIntStateOf(initialCount) }
    val counterValue = stringResource(R.string.counter_value, count)
    val buttonModifier = Modifier
        .size(buttonSize)
        .clip(buttonShape)
        .background(buttonColor)

    fun resetCount() {
        count = 0
    }

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                modifier = buttonModifier,
                onClick = { count = maxOf(0, count - 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = stringResource(R.string.decrement),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Box(
                modifier = buttonModifier
                    .size(buttonSize)
                    .semantics { contentDescription = counterValue },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            IconButton(
                modifier = buttonModifier,
                onClick = { count++ },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.increment),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        ActionButton(
            action = ButtonAction.Add,
            enabled = count > 0,
            onClick = {
                onCreate(count)
                resetCount()
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCounter() {
    Counter(
        onCreate = {}
    )
}

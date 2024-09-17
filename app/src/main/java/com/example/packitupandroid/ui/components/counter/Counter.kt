package com.example.packitupandroid.ui.components.counter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType

@Composable
fun Counter(
    onCreate: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var count by remember { mutableIntStateOf(0) }
    val buttonModifier = Modifier
        .size(40.dp)
        .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small)))
        .background(Color.Gray)

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
                Icon(imageVector = Icons.Default.Remove, contentDescription = "decrement")
            }
            TextButton(
                modifier = buttonModifier
                    .semantics {
                        contentDescription = "Counter Value"
                    },
                onClick = {},
                enabled = false,
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            IconButton(
                modifier = buttonModifier,
                onClick = { count++ },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "increment")
            }
        }
        AddConfirmCancelButton(
            button = ButtonType.Add,
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

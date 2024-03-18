package com.example.packitupandroid.ui.components.counter

import android.annotation.SuppressLint
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType

@Composable
fun <T: BaseCardData> Counter(
    onClick: (T, Int?) -> Unit,
    modifier: Modifier = Modifier,
    type : T? = null,
) {
    var count by remember { mutableIntStateOf(0) }
    val buttonModifier = Modifier
        .size(40.dp)
        .clip(RoundedCornerShape(8.dp))
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
                Icon(imageVector = Icons.Default.Remove, contentDescription = "subtract")
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
                modifier = buttonModifier
                    .testTag("ADD"),
                onClick = { count++ },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
        AddConfirmCancelButton(
            button = ButtonType.Add,
            enabled = type != null && count > 0,
            onClick = {
                if(type != null) {
                    onClick(type, count)
                    resetCount()
                }
                      },
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewCounter() {
    Counter<Item>(
        type = Item(
            name = "Sample Item",
            description = "This is a sample item",
            value = 10.0,
            isFragile = false
        ),
        onClick = { item, _ -> println("Clicked: ${item.name}") }
    )
}

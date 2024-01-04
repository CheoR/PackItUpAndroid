package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box

@Composable
fun BoxesScreen(
    modifier: Modifier = Modifier,
    cards: List<Box> = LocalDataSource().loadBoxes(),
) {
    LazyColumn {
        items(cards) {
            Row {
                Text(
                    text = "id: ${it.id}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(4.dp)
                )
            }
            Row {
                Text(
                    text = "name: ${it.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(4.dp)
                )
            }
            Row {
                Text(
                    text = "items: ${it.items.size}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(4.dp)
                )
            }
            Row {
                Text(
                    text = "value: ${it.totalValue}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxesScreen() {
    BoxesScreen()
}

@Composable
fun Box() {
    Text(text = stringResource(id = R.string.boxes))
}

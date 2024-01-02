package com.example.packitupandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PackItUpAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(
    cards: List<Collection> = LocalDataSource().loadCollections(),
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .background(color = MaterialTheme.colorScheme.error)
        .padding(4.dp)
        .clip(MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ){
        LazyColumn() {
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
                        text = "boxes: ${it.boxes.size}",
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PackItUpAndroidTheme {
        Greeting()
    }
}
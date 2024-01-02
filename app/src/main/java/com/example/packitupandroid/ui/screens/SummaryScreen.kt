package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.ui.PackItUpApp
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .background(color = MaterialTheme.colorScheme.error)
        .padding(4.dp)
        .clip(MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Summary Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen() {
    SummaryScreen()
}
package com.example.packitupandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.ui.screens.SummaryScreen

@Composable
fun PackItUpListOnlyContent(
    modifier: Modifier = Modifier
) {
    Column() {
        SummaryScreen()
    }
}

@Preview
@Composable
fun PreviewPackItUpListOnlyContent() {
    PackItUpListOnlyContent()
}
package com.example.packitupandroid.ui.screens

import BaseCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier
) {
        BaseCard(
            title = stringResource(R.string.collections),
            description = "Group boxes into collections . . ",
            onCardClick = {},
            imageVector1 = ImageVector.vectorResource(R.drawable.baseline_category_24),
            imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
            onButtonIconClick = { },
            value = LocalDataSource().loadItems().first().value,
            onCheckedChange = {},
        )
        BaseCard(
                title = stringResource(R.string.boxes),
                description = "Group items into boxes . . ",
                onCardClick = {},
                imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
                onButtonIconClick = { },
                value = LocalDataSource().loadItems().first().value,
                onCheckedChange = {},
        )
        BaseCard(
                title = stringResource(R.string.items),
                dropdownOptions = LocalDataSource().loadBoxes().map { it.name },
                description = "This is  your ",
                onCardClick = {},
                imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
                buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
                onButtonIconClick = { },
                value = LocalDataSource().loadItems().first().value,
                onCheckedChange = {},
        )
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen() {
    SummaryScreen()
}
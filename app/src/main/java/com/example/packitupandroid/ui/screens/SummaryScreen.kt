package com.example.packitupandroid.ui.screens

import BaseCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import formatValue

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        BaseCard(
            title = stringResource(R.string.collections),
            description = "Group boxes into collections . . ",
            onCardClick = {},
            imageVector1 = ImageVector.vectorResource(R.drawable.baseline_category_24),
            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
            onButtonIconClick = { },
            value = LocalDataSource().loadItems().sumOf { it.value },
            onCheckedChange = {},
            firstBadgeCount = LocalDataSource().loadCollections().size,
            isFragile = LocalDataSource().loadItems().any{ it.isFragile },
            isShowFragileAndValue = false,
        )
        BaseCard(
            title = stringResource(R.string.boxes),
            description = "Group items into boxes . . ",
            onCardClick = {},
            imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
            onButtonIconClick = { },
            value = LocalDataSource().loadItems().sumOf { it.value },
            onCheckedChange = {},
            firstBadgeCount = LocalDataSource().loadBoxes().size,
            isFragile = LocalDataSource().loadItems().any{ it.isFragile },
            isShowFragileAndValue = false,
        )
        BaseCard(
            title = stringResource(R.string.items),
            dropdownOptions = LocalDataSource().loadBoxes().map { it.name },
            description = "This is  your ",
            onCardClick = {},
            imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
            buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
            onButtonIconClick = { },
            value = LocalDataSource().loadItems().sumOf { it.value },
            onCheckedChange = {},
            firstBadgeCount = LocalDataSource().loadItems().size,
            isFragile = LocalDataSource().loadItems().any{ it.isFragile },
            isShowFragileAndValue = false,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Checkbox(
                checked = LocalDataSource().loadItems().any{ it.isFragile },
                onCheckedChange = { },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Fragile")

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Total: ${LocalDataSource().loadItems().sumOf { it.value }.formatValue()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSummaryScreen() {
    SummaryScreen()
}
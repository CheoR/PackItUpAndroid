package com.example.packitupandroid.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.card.BaseCardData
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.EditFields
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType

@Composable
fun EditCard(
    data: BaseCardData,
    onUpdate: (BaseCardData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.Edit,
    cardType: CardType = CardType.Default,
    editFields: Set<EditFields> = emptySet(),
    // TODO: add dropdown
) {
    var updatedData by remember { mutableStateOf(data) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
        verticalArrangement = Arrangement.Center,
    ){
        Spacer(modifier = Modifier.weight(2f))
        Card(
            modifier = modifier
                .height(dimensionResource(R.dimen.card_height))
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small))),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.image_size_medium)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
            ) {
                when (data.value) {
                    is Item -> {
                        val image = (data.value as Item).imageUri
                        ProjectIcons(
                            icon1 = if (image != null) ColumnIcon.UriIcon(image) else ColumnIcon.VectorIcon(
                                Icons.Default.Label
                            ),
                            isShowBadgeCount = cardType is CardType.Summary,
                        )
                    }

                    is Box -> {
                        ProjectIcons(
                            icon1 = ColumnIcon.VectorIcon(Icons.Default.Label),
                            badgeCount1 = (data.value as Box).items.size,
                        )
                    }

                    is Collection -> {
                        ProjectIcons(
                            icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
                            icon2 = ColumnIcon.VectorIcon(Icons.Default.Label),
                            badgeCount1 = (data.value as Collection).boxes.size,
                            badgeCount2 = (data.value as Collection).boxes.sumOf { it.items.size },
                        )
                    }

                    is Summary -> {
                        val (count, icon) = when (data.value.id) {
                            "collections" -> Pair(
                                (data.value as Summary).collections.size,
                                Icons.Default.Category
                            )

                            "boxes" -> Pair(
                                (data.value as Summary).boxes.size,
                                ImageVector.vectorResource(R.drawable.ic_launcher_foreground)
                            )

                            else -> Pair((data.value as Summary).items.size, Icons.Default.Label)
                        }
                        ProjectIcons(
                            icon1 = ColumnIcon.VectorIcon(icon),
                            badgeCount1 = count,
                        )
                    }
                }
                DataColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .padding(horizontal = 4.dp),
                    data = data.value,
                    onUpdate = onSave,
                    editMode = editMode,
                )
                Column(
                    modifier = modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .size(24.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            Log.i("EDIT ITEM CARD", updatedData.toString())
            onUpdate(updatedData)
        }) {
            Text(text = "Update")
        }
        AddConfirmCancelButton(button = ButtonType.Cancel, onClick = onCancel)
    }
}
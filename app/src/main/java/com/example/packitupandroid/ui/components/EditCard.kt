package com.example.packitupandroid.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.R
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.PackItUpViewModel
import com.example.packitupandroid.ui.components.card.CardType
import com.example.packitupandroid.ui.components.card.DataColumn
import com.example.packitupandroid.ui.components.card.EditMode
import com.example.packitupandroid.ui.components.card.IconsColumn
import com.example.packitupandroid.ui.components.common.AddConfirmCancelButton
import com.example.packitupandroid.ui.components.common.ButtonType

@Composable
fun EditCard(
    data: BaseCardData,
    onEdit: (BaseCardData) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.Edit,
    cardType: CardType = CardType.Default,
    // TODO: add dropdown
) {
    val vm: PackItUpViewModel = viewModel(factory = PackItUpViewModel.Factory)
    val vmOnUpdate = vm::updateElement

    Column(
        modifier = modifier
            .height(300.dp)
//            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary),
        verticalArrangement = Arrangement.Center,
    ) {
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
                IconsColumn(data = data, cardType = cardType)
                DataColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .padding(horizontal = 4.dp),
                    data = data,
                    onUpdate =  {
                        vmOnUpdate(it)
                        onEdit(it)
                    },
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
        Button(
            onClick = {
                Log.i("EDIT ITEM CARD", data.toString())
//                onSave()
                vmOnUpdate(Item(
                    id = data.id,
                    name =  "peggy pug look at updated",
                    isFragile = false,
                ))
            }) {
            Text(text = "Update Moo Cow opinkfdfd")
        }
        AddConfirmCancelButton(button = ButtonType.Cancel, onClick = onCancel, enabled = true)
    }
}

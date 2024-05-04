package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.packitupandroid.PackItUpUiState
import com.example.packitupandroid.R
import com.example.packitupandroid.Result
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.components.card.BaseCard
import com.example.packitupandroid.ui.components.counter.Counter
import com.example.packitupandroid.ui.components.spinner.Spinner

@Composable
fun <T: BaseCardData> Screen(
    uiState: PackItUpUiState,
    onCreate: (Int) -> Unit,
    onUpdate: (T) -> Unit,
    onDestroy: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val result = uiState.result

    val rememberedElements: List<T> = remember(result) {
        when (result) {
            is Result.Complete -> {
                result.elements as List<T>
            }
            else -> emptyList()
        }
    }
    Column(modifier = modifier) {
        when (result) {
            is Result.Loading -> Spinner()
            is Result.Error -> Error()
            is Result.Complete -> {
                LazyColumn(
                    modifier = modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.space_arrangement_small)
                    )
                ) {
                    items(
                        items = rememberedElements,
                        key = { it.id }
                    ) { element ->
                        BaseCard(
                            element = element,
                            onUpdate = onUpdate,
                            onDestroy = onDestroy,
//                            getParentContainer = ,
//                            getDropdownOptions = viewModel::getDropdownOptions,
                        )
                    }
                }
            }
        }
        Counter(onCreate = onCreate)
    }
}

@Composable
private fun Error() {
    // TODO: move to utils
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Screen Error")
    }
}

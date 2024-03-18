package com.example.packitupandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.packitupandroid.R
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.ui.components.counter.Counter

@Composable
fun <T: BaseCardData> Screen(
    elements: List<T>,
    onClick: (T, Int?) -> Unit,
    updateElement: (T) -> Unit,
    destroyElement: (T) -> Unit,
    card: @Composable (T, (T) -> Unit, (T) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
//    val cachedElements = rememberSaveable { mutableStateOf(elements) }
//    var cachedElements by remember { mutableStateOf(elements) }
//
//    LaunchedEffect(elements) {
//        cachedElements = elements
//    }
    Column(modifier = modifier) {
        LazyColumn(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_small)
            )
        ) {
            items(
//                items = cachedElements,
                items = elements,
                key = { it.id }
            ) { element ->
                card(element, updateElement, destroyElement)
            }
        }
//        Counter(type = cachedElements.firstOrNull(), onClick = onClick)
        Counter(type = elements.firstOrNull(), onClick = onClick)
    }
}


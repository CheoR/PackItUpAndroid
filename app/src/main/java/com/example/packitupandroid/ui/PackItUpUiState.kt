package com.example.packitupandroid.ui

import com.example.packitupandroid.data.ScreenType
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item

data class PackItUpUiState(
    val currentScreen: ScreenType = ScreenType.Summary,
    val items: List<Item> = emptyList(),
    val boxes: List<Box> = emptyList(),
    val collections: List<Collection> = emptyList(),
)

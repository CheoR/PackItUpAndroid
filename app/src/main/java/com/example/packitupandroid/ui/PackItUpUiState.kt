package com.example.packitupandroid.ui

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.navigation.PackItUpRoute

data class PackItUpUiState(
    val currentScreen: String = PackItUpRoute.SUMMARY,
    val items: List<Item> = emptyList(),
    val boxes: List<Box> = emptyList(),
    val collections: List<Collection> = emptyList(),
)

package com.example.packitupandroid.model

data class Box(
    val id: String,
    val name: String,
    val items: List<Item>,
    val totalValue: Double // Computed property, sum of values of items in the box
)

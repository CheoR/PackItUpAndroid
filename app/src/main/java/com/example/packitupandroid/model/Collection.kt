package com.example.packitupandroid.model

data class Collection(
    val id: String,
    val name: String,
    val boxes: List<Box>,
    val totalValue: Double // Computed property, sum of values of items in all boxes in the collection
)

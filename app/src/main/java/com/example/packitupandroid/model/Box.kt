package com.example.packitupandroid.model

data class Box(
    val id: Long,
    val name: String,
    val description: String = "",
    val items: List<Item>,
) {
    // These properties are read-only and will be computed when the `Box` object is created.
    // If the `items` list changes, create a new `Box` object to update these properties.
    val isFragile: Boolean = items.any { it.isFragile }
    val value: Double = items.sumOf { it.value }
}

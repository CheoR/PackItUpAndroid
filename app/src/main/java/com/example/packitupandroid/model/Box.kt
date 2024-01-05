package com.example.packitupandroid.model

data class Box(
    val id: String,
    val name: String,
    val description: String = "",
//    val isFragile: Boolean = false, // Computed property, check if any items in box are fragile
//    val isFragile: (Box) -> Boolean,
    val items: List<Item>,
//    val totalValue: Double = 0.00, // Computed property, sum of values of items in the box
) {
    // These properties are read-only and will be computed when the `Box` object is created.
    // If the `items` list changes, create a new `Box` object to update these properties.
    val isFragile: Boolean = items.any { it.isFragile }
    val totalValue: Double = items.sumOf { it.value }
}

//fun Box.hasFragileItem() : Boolean {
//    /**
//     * Check if any [Item] in [Box]'s items list are marked as fragile.
//     */
//    return this.items.any { it.isFragile }
//}

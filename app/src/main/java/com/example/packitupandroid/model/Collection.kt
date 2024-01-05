package com.example.packitupandroid.model

data class Collection(
    val id: String,
    val name: String,
    val description: String = "",
//    val isFragile: Boolean = false, // Computed property, check if any boxes in collection are fragile
    val boxes: List<Box>,
//    val totalValue: Double = 0.00  // Computed property, sum of values of items in all boxes in the collection
) {
    val isFragile: Boolean = boxes.any { it.isFragile }
    val totalValue: Double = boxes.sumOf { it.totalValue }
}

//fun Collection.hasFragileBox() : Boolean {
//    /**
//     * Check if any [Box] in [Collection]'s box list are marked as fragile.
//     */
//    return this.boxes.any { it.isFragile }
//}

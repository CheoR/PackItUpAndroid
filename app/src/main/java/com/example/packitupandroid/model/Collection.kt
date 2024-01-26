package com.example.packitupandroid.model

data class Collection(
    val id: String,
    val name: String,
    val description: String = "",
    val boxes: List<Box>,
) {
    // TODO: FIX THIS
    val isFragile: Boolean
        get() = boxes.any { it.isFragile }
    val value: Double
        get() = boxes.sumOf { it.value }
}

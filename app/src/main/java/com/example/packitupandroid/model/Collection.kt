package com.example.packitupandroid.model

data class Collection(
    val id: Long,
    val name: String,
    val description: String = "",
    val boxes: List<Box>,
) {
    val isFragile: Boolean = boxes.any { it.isFragile }
    val value: Double = boxes.sumOf { it.value }
}

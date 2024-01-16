package com.example.packitupandroid.model

data class Item(
    val id: Long,
    val name: String,
    val description: String = "",
    val isFragile: Boolean = false,
    val imageUri: Int? = null,
    val value: Double = 0.00
)

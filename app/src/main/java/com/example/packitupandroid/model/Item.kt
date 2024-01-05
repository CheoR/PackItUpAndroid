package com.example.packitupandroid.model

data class Item(
    val id: String,
    val name: String,
    val description: String = "",
    val isFragile: Boolean = false,
    val value: Double = 0.00
)

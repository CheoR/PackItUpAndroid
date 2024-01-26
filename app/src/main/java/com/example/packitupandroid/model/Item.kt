package com.example.packitupandroid.model

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val isFragile: Boolean = false,
    val imageUri: Int? = null,
    val value: Double = 0.00,
)

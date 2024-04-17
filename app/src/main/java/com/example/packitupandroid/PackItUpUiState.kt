package com.example.packitupandroid

import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.navigation.PackItUpRoute

sealed class Result {
    data class Complete(val elements: List<BaseCardData>) : Result()
    object Loading : Result()
    data class Error(val message: String) : Result()
}

data class PackItUpUiState (
    val currentScreen: String = PackItUpRoute.SUMMARY,
    val result: Result = Result.Loading
)

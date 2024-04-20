package com.example.packitupandroid

import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.ui.navigation.PackItUpRoute

sealed class Result {
    // TODO: Fix - look into making this into a Map<String, List<BaseCardData>>) : Result()
    data class Complete(
        val elements: List<BaseCardData>? = null,
        val summary: Summary? = null,
    ) : Result()
    object Loading : Result()
    data class Error(val message: String) : Result()
}

data class PackItUpUiState (
    val currentScreen: String = PackItUpRoute.SUMMARY,
    val result: Result = Result.Loading
)

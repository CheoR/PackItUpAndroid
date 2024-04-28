package com.example.packitupandroid

import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Summary

sealed class Result {
    // TODO: Fix - look into making this into a Map<String, List<BaseCardData>>) : Result()
    data class Complete(
        val elements: List<BaseCardData>? = null,
        val summary: Summary? = null,
    ) : Result()
    object Loading : Result()
    data class Error(val message: String) : Result()
}

interface PackItUpUiState {
    val elements: List<BaseCardData>
    val result: Result
}

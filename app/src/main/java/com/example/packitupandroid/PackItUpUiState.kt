package com.example.packitupandroid

import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.utils.Result


interface PackItUpUiState {
    val elements: List<BaseCardData>
    val result: Result<List<BaseCardData>>
}

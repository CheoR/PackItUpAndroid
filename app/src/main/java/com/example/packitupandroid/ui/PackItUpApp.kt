package com.example.packitupandroid.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.utils.PackItUpContentType
import com.example.packitupandroid.ui.utils.PackItUpNavigationType

@Composable
fun PackItUpApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val viewModel: PackItUpViewModel = viewModel()
    val packItUpUiState = viewModel.uiState.collectAsState().value

    /*
        To implement navigation drawer, determine navigation type based on app's window size.
     */
    val navigationType: PackItUpNavigationType

    /*
        For various window sizes to help determine appropriate content type selection,
        depending on screen size
     */
    val contentType: PackItUpContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = PackItUpNavigationType.BOTTOM_NAVIGATION
            contentType = PackItUpContentType.LIST_ONLY
        }
        else -> {
            navigationType = PackItUpNavigationType.BOTTOM_NAVIGATION
            contentType = PackItUpContentType.LIST_ONLY
        }
    }

    SummaryScreen(
        navigationType = navigationType,
        contentType = contentType,
        packItUpUiState = packItUpUiState,
        onTabPressed = {},
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPackItUpApp() {
    PackItUpAndroidTheme {
        Surface {
            PackItUpApp(
                // windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
                windowSize = WindowWidthSizeClass.Compact,
            )
        }
    }
}
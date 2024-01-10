package com.example.packitupandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.ui.PackItUpApp
import com.example.packitupandroid.ui.PackItUpViewModel
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    private val viewModel: PackItUpViewModel by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PackItUpAndroidTheme {
                val windowSize = calculateWindowSizeClass(this)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PackItUpApp(
                        windowSize = windowSize,
//                        packItUpUiState = uiState,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewPackItUp() {
    PackItUpAndroidTheme {
        Surface {
            PackItUpApp(
                windowSize = WindowSizeClass.calculateFromSize(DpSize(412.dp, 732.dp)),
//                packItUpUiState= PackItUpUiState()
            )
        }
    }
}
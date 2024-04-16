package com.example.packitupandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.packitupandroid.ui.PackItUpApp
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme

class MainActivity : ComponentActivity() {
//    private val viewModel: ItemsScreenViewModel by viewModels {
//        PackItUpViewModelProvider.Factory
//    }
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }

        setContent {
            PackItUpAndroidTheme {
                val windowSize = calculateWindowSizeClass(this)
//                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    PackItUpApp(
                        windowSize = windowSize,
//                        uiState = uiState,
                    )
                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
        )
    }
}

// TODO: fix
//@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
//@Preview(showBackground = true)
//@Composable
//fun PreviewPackItUp(
//    localDataSource: LocalDataSource = LocalDataSource(),
//) {
//    val items = localDataSource.loadItems()
//    val boxes = localDataSource.loadBoxes()
//    val collections = localDataSource.loadCollections()
//    val currentScreen = PackItUpRoute.SUMMARY
//
//    PackItUpAndroidTheme {
//        Surface {
//            PackItUpApp(
//                windowSize = WindowSizeClass.calculateFromSize(DpSize(412.dp, 732.dp)),
//                uiState = PackItUpUiState(
//                    items = items,
//                    boxes = boxes,
//                    collections = collections,
//                    currentScreen = currentScreen,
//                ),
//            )
//        }
//    }
//}

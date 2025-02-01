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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpApp
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme


class MainActivity : ComponentActivity() {

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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    PackItUpApp(
                        windowSize = windowSize,
                    )
                }
            }
        }
    }

    /**
     * Checks if the application has all the required permissions defined in [CAMERAX_PERMISSIONS].
     *
     * This function iterates through each permission in the [CAMERAX_PERMISSIONS] array and checks if it's
     * granted using [ContextCompat.checkSelfPermission]. It returns true only if all permissions are granted;
     * otherwise, it returns false.
     *
     * @return `true` if all required permissions are granted, `false` otherwise.
     *
     * @see CAMERAX_PERMISSIONS
     * @see ContextCompat.checkSelfPermission
     * @see PackageManager.PERMISSION_GRANTED
     */
    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        /**
         * An array of permissions required for CameraX functionality.
         *
         * This array contains the `Manifest.permission.CAMERA` permission, which is essential
         * for accessing the device's camera and capturing images or videos.
         *
         * When using CameraX, you must request these permissions from the user before
         * attempting to initialize or use the camera. Failure to do so will result in
         * security exceptions.
         */
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
        )
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewPackItUp(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    PackItUpAndroidTheme {
        Surface {
            PackItUpApp(
                windowSize = WindowSizeClass.calculateFromSize(DpSize(412.dp, 732.dp)),
            )
        }
    }
}

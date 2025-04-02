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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.packitupandroid.data.source.local.LocalDataSource
import com.example.packitupandroid.ui.PackItUpApp
import com.example.packitupandroid.ui.theme.PackItUpAndroidTheme
import com.example.packitupandroid.ui.theme.rememberThemeManager


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        requestPermissionsIfNeeded()

        setContent {
            val context = LocalContext.current
            val themeManager = rememberThemeManager(context)

            PackItUpAndroidTheme(themeManager) {
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
     * Requests necessary permissions if they are not already granted.
     *
     * Currently requests the `CAMERA` permission.  If any of the required permissions
     * are not granted, it will trigger a permission request dialog to the user.  The
     * result of the permission request will be handled in the `onRequestPermissionsResult`
     * callback of the activity.
     *
     * **Note:** This function should be called within an Activity context, as it uses
     * `ActivityCompat.requestPermissions` which requires an Activity.  It also assumes
     * that the calling Activity has overridden `onRequestPermissionsResult` to handle the
     * permission grant/denial.
     */
    private fun requestPermissionsIfNeeded() {
        if (CAMERAX_PERMISSIONS.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, PERMISSION_REQUEST_CODE
            )
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

        /**
         * The request code used for permission requests.
         */
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewPackItUp(
    localDataSource: LocalDataSource = LocalDataSource(),
) {
    val context = LocalContext.current
    val themeManager = rememberThemeManager(context)
    PackItUpAndroidTheme(themeManager) {
        Surface {
            PackItUpApp(
                windowSize = WindowSizeClass.calculateFromSize(DpSize(412.dp, 732.dp)),
            )
        }
    }
}

package com.example.packitupandroid.ui.common.component.card

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView


/**
 * A composable function that displays a camera preview using the provided [LifecycleCameraController].
 *
 * This function utilizes the [AndroidView] composable to integrate a [PreviewView] from the CameraX library
 * into the Compose UI. It handles the lifecycle binding of the camera controller to ensure proper camera
 * initialization and cleanup.
 *
 * @param controller The [LifecycleCameraController] instance that manages the camera and its lifecycle.
 *                   This controller is responsible for setting up the camera, handling image capture,
 *                   and managing the preview stream.
 * @param modifier A [Modifier] to be applied to the underlying [PreviewView]. This allows you to customize
 *                 the layout and appearance of the camera preview, such as setting its size, padding,
 *                 or background color. Defaults to [Modifier].
 */
@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}

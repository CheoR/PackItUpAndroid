package com.example.packitupandroid.ui.common.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.card.elements.CameraPreview
import com.example.packitupandroid.ui.common.component.ConfirmCancelContainer
import com.example.packitupandroid.utils.EditFields
import java.io.ByteArrayOutputStream


/**
 * A composable function that displays a camera preview dialog, allowing the user to take a photo.
 *
 * This dialog provides a live camera preview and buttons to capture a photo or cancel.
 * When a photo is taken, it updates the `imageUri` field of the `selectedCard` with the
 * URI of the captured image and triggers the `onClick` callback.
 * @param dialogWidth The desired width of the dialog.
 * @param onClick Callback invoked when a photo is successfully taken and processed.
 * @param onCancel Callback invoked when the user cancels the camera operation.
 * @param onFieldChange Callback invoked to update a specific field in the `selectedCard` data.
 *                    It takes the `selectedCard` mutable state, the `EditFields` enum value
 *                    representing the field to change and the new value.
 * @param selectedCard MutableState holding the currently selected card data of type `D`.
 *                     This state will be updated with the captured image URI.
 * @param modifier Modifier for styling and layout adjustments of the dialog.
 * @param D The type of the card data, which must inherit from [BaseCardData].
 * */
@Composable
fun <D: BaseCardData>CameraDialog(
    dialogWidth: Dp,
    onClick: () -> Unit,
    onCancel: () -> Unit,
    onFieldChange: (MutableState<D?>, EditFields, String) -> Unit,
    selectedCard: MutableState<D?>,
    modifier: Modifier = Modifier,
) {
    val imageUri = remember(selectedCard.value) {
        // to avoid error: Smart cast to '{D & Any & Item}' is impossible, because 'selectedCard. value'
        // is a mutable property that could have been changed by this time
        // use currentCard to tempoarily store value. This creates a read-only copy of value, ensuring
        // that it wont' change during execution of the remember {} block
        val currentCard = selectedCard.value

        if (currentCard is Item) {
            mutableStateOf(currentCard.imageUri)
        } else {
            null
        }
    }

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    ConfirmCancelContainer(
        title = stringResource(R.string.camera_dialog_title, selectedCard.value?.name ?: ""),
        dialogWidth = dialogWidth,
        onCancel = onCancel,
        onConfirm = {
            takePhoto(
                context = context,
                controller = controller,
                onPhotoTaken = {
                    imageUri?.value = it
                    onFieldChange(selectedCard, EditFields.ImageUri, it)
                    onClick()
                }
            )
        },
        modifier = modifier,
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

/**
 * Takes a photo using the camera and returns the base64 encoded image data.
 *
 * This function utilizes the CameraX library to capture an image. It then converts
 * the captured image to a Bitmap and subsequently encodes it into a Base64 string.
 * The resulting Base64 string is then passed to the provided callback function.
 *
 * @param context The application context. Required for accessing the main executor and other resources.
 * @param controller The LifecycleCameraController instance that manages the camera.
 * @param onPhotoTaken A callback function that is invoked when the photo is successfully taken.
 *                     It receives the Base64 encoded image string as a parameter.
 *                     If an error occurs during capture, the callback will still be invoked, but the string will be the result of `toString()` on null, often "null".
 *
 * @throws IllegalStateException If the camera is not properly initialized or if there are issues accessing the camera hardware.
 *
 * @see LifecycleCameraController
 * @see ImageCapture
 * @see ImageProxy
 * @see ImageCapture.OnImageCapturedCallback
 * @see ContextCompat
 */
private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (String) -> Unit,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val bitmap = imageProxyToBitmap(image)
                val base64Image = bitmap?.let { bitmapToBase64(it) }
                onPhotoTaken(base64Image.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Camera Error: ", exception)
            }
        }
    )
}

/**
 * Converts an [ImageProxy] to a [Bitmap].
 *
 * This function extracts the image data from the first plane of an [ImageProxy]
 * and decodes it as a [Bitmap]. It assumes the image data is in a format that
 * can be directly decoded by [BitmapFactory.decodeByteArray], such as JPEG or PNG.
 *
 * Note: This function currently only handles the first plane of the [ImageProxy].
 *       It might not be suitable for images with multiple planes (e.g., YUV).
 *
 * @param imageProxy The [ImageProxy] to convert.
 * @return A [Bitmap] representation of the image, or `null` if decoding fails.
 *
 * @throws IllegalArgumentException if imageProxy.planes is empty
 * @throws IndexOutOfBoundsException if imageProxy.planes.get(0) fails
 * @throws NullPointerException if imageProxy.planes.get(0).buffer is null
 */
private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
    val planeProxy = imageProxy.planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

/**
 * Converts a Bitmap object to a Base64 encoded string.
 *
 * This function takes a Bitmap image as input, compresses it as a JPEG with 100% quality,
 * and then encodes the resulting byte array into a Base64 string representation.
 *
 * @param bitmap The Bitmap object to be converted to a Base64 string.
 * @return A Base64 encoded string representing the input Bitmap.
 * @throws IllegalArgumentException if the input bitmap is null
 */
private fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

package com.example.packitupandroid.ui.common.component.card

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.core.content.ContextCompat
import com.example.packitupandroid.R
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.common.component.CameraPreview
import com.example.packitupandroid.ui.common.component.ConfirmCancelContainer
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.EditMode
import java.io.ByteArrayOutputStream

@Composable
fun<T: BaseCardData> CameraCard(
    element: T,
    onClick: (T) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    editMode: EditMode = EditMode.Edit,
) {
    fun isEditable(field: EditFields) = editMode == EditMode.Edit && element.editFields.contains(field)
    var localData by remember { mutableStateOf(element) }
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    ConfirmCancelContainer(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inversePrimary)
            .semantics {
                contentDescription = "Camera Preview"
            },
        onConfirm = {
            takePhoto(
                context = context,
                controller = controller,
                onPhotoTaken = {
                    if (isEditable(EditFields.ImageUri)) {
                        localData = (localData as Item).copy(
                            imageUri = it,
                        ) as T
                    }
                    onClick(localData)
                }
            )
        },
        onCancel = onCancel,
        content = {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.roundness_x_small)))
                    .background(color = Color.Gray),
            ) {
                CameraPreview(
                    controller = controller,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    )
}

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
                Log.i("IMAGE: ", base64Image.toString())
                onPhotoTaken(base64Image.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Camera Error: ", exception)
            }
        }
    )
}

private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
    val planeProxy = imageProxy.planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

private fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

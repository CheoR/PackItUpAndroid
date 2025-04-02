package com.example.packitupandroid.ui.common.card.elements

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import java.io.File


/**
 * Sealed class representing different types of content that can be displayed as an image.
 *
 * This class provides a way to handle various image sources in a type-safe and organized manner.
 * It allows for flexibility in displaying images from different origins, such as Bitmaps,
 * drawable resources, encoded strings, or vector graphics.
 */
sealed class ImageContent {
    data class BitmapImage(val bitmap: Bitmap) : ImageContent()
    data class BitmapStringImage(val bitmapString: String) : ImageContent()
    data class DrawableImage(val drawableResId: Int) : ImageContent()
    data class VectorImage(val imageVector: ImageVector) : ImageContent()
    data class FileImage(val filePath: String) : ImageContent()
}

/**
 * Converts a Base64 encoded string to a Bitmap image.
 *
 * @param base64Image The Base64 encoded string representing the image.
 * @return A Bitmap object representing the decoded image, or null if decoding fails or the input is invalid.
 * @throws IllegalArgumentException if the input `base64Image` is null or empty.
 */
private fun base64ToBitmap(base64Image: String): Bitmap? {
    val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

/**
 * Displays an image based on the provided [ImageContent].
 *
 * This composable function handles different types of image sources, including:
 * - [ImageContent.BitmapImage]: Displays an image from a [Bitmap].
 * - [ImageContent.DrawableImage]: Displays an image from a drawable resource ID.
 * - [ImageContent.VectorImage]: Displays an image from a [ImageVector].
 * - [ImageContent.BitmapStringImage]: Displays an image from a Base64 encoded string.
 *
 * If a [BitmapStringImage] is provided and the Base64 decoding fails, a placeholder image
 * (R.drawable.baseline_label_24) will be displayed.
 *
 * @param image The [ImageContent] representing the image to be displayed.
 * @param modifier Modifier to be applied to the image.
 * @param contentDescription Text used by accessibility services to describe what this image represents */
@Composable
fun IconImage(
    selectedIcon: ImageContent,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tintIcon: Boolean = false,
) {
    val color = if (tintIcon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    // colorFilter masks image if left as colorFilter = ColorFilter.tint(color) below
    val colorFilter = if (selectedIcon is ImageContent.FileImage && File(selectedIcon.filePath).exists()) {
        null
    } else {
        ColorFilter.tint(color)
    }

    val painter = when (selectedIcon) {
        is ImageContent.BitmapImage -> BitmapPainter(selectedIcon.bitmap.asImageBitmap())
        is ImageContent.DrawableImage -> painterResource(id = selectedIcon.drawableResId)
        is ImageContent.VectorImage -> rememberVectorPainter(image = selectedIcon.imageVector)
        is ImageContent.BitmapStringImage -> {
            val bitmap = base64ToBitmap(selectedIcon.bitmapString)
            if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                // Handle null bitmap, e.g., show a placeholder
                painterResource(id = R.drawable.baseline_label_24)
            }
        }
        is ImageContent.FileImage -> {
            val bitmap = remember(selectedIcon.filePath) {
                try {
                    val file = File(selectedIcon.filePath)

                    if (file.exists()) {
                        BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    Log.e("MOO", "Error decoding file: ${e.message}")
                    null // decoding fails
                }
            }

            if (bitmap != null) {
                BitmapPainter(bitmap)
            } else {
                painterResource(id = R.drawable.baseline_label_24)
            }
        }
    }

    return Image(
        modifier = modifier.size(40.dp),
        painter = painter,
        contentDescription = contentDescription,
        colorFilter = colorFilter,
    )
}

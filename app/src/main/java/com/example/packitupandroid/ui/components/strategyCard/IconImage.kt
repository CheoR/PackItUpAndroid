package com.example.packitupandroid.ui.components.strategyCard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.example.packitupandroid.R


sealed class ImageContent {
    data class BitmapImage(val bitmap: Bitmap) : ImageContent()
    data class BitmapStringImage(val bitmapString: String) : ImageContent()
    data class DrawableImage(val drawableResId: Int) : ImageContent()
    data class VectorImage(val imageVector: ImageVector) : ImageContent()
}

private fun base64ToBitmap(base64Image: String): Bitmap? {
    val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

@Composable
fun IconImage(
    image: ImageContent,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val painter = when (image) {
        is ImageContent.BitmapImage -> BitmapPainter(image.bitmap.asImageBitmap())
        is ImageContent.DrawableImage -> painterResource(id = image.drawableResId)
        is ImageContent.VectorImage -> rememberVectorPainter(image = image.imageVector)
        is ImageContent.BitmapStringImage -> {
            val bitmap = base64ToBitmap(image.bitmapString)
            if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                // Handle null bitmap, e.g., show a placeholder
                painterResource(id = R.drawable.baseline_label_24)
            }
        }
    }

    return Image(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription
    )
}

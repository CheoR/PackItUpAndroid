package com.example.packitupandroid.ui.common.component

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


/**
 * A composable function that displays a rotating spinner.
 *
 * @param modifier The modifier to be applied to the spinner.
 * @param drawableRes The drawable resource to be used for the spinner.
 * @param animationDuration The duration of the rotation animation in milliseconds.
 * @param animationEasing The easing function to be used for the rotation animation.
 * @param rotationAngle The angle of rotation in degrees.
 * @param imageSize The size of the spinner image.
 * @param imageShape The shape of the spinner image.
 * @param contentScale The content scale of the spinner image.
 * @param contentDescription The content description of the spinner image.
 * @param backgroundColor The background color of the spinner image.
 */
@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    drawableRes: Int = R.drawable.foreground_background,
    animationDuration: Int = 4000,
    animationEasing: Easing = LinearEasing,
    rotationAngle: Float = 365f,
    imageSize: Dp = 200.dp,
    imageShape: Shape = CircleShape,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "Loading Content",
    backgroundColor: Color = MaterialTheme.colorScheme.background,
//    imageTint: Color = MaterialTheme.colorScheme.primary,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = rotationAngle,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = animationEasing),
            repeatMode = RepeatMode.Restart
        ), label = "progress"
    )

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier
                .size(imageSize)
                .clip(imageShape)
                .background(backgroundColor)
                .graphicsLayer { rotationZ = progress }
                .then(modifier)
                .testTag("LoadingIndicator"),
//            colorFilter = ColorFilter.tint(imageTint)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpinner() {
    Spinner()
}

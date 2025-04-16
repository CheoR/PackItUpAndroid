package com.example.packitupandroid.ui.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R


/**
 * A composable button representing a delivery option.
 *
 * This button displays an icon and text label, centered vertically and horizontally.
 * It's typically used to represent different delivery choices in a UI.
 *
 * @param icon The [androidx.compose.ui.graphics.vector.ImageVector] to display as the icon.
 * @param text The text label to display below the icon.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param modifier The [androidx.compose.ui.Modifier] to apply to the button's layout.
 *
 * @sample
 * ```kotlin
 * DeliveryOptionButton(
 *     icon = Icons.Filled.LocalShipping,
 *     text = "Standard Delivery",
 *     onClick = { println("Standard Delivery clicked") }
 * )
 * ```
 */
@Composable
fun DeliveryOptionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DeliveryOptionButtonPreview() {
    DeliveryOptionButton(
        icon = ImageVector.vectorResource(id = R.drawable.baseline_category_24),
        text = "Collection",
        onClick = { println("Collection clicked") }
    )
}

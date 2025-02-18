package com.example.packitupandroid.ui.common.card

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.packitupandroid.R


/**
 * Displays an icon with an optional badge to indicate a count or notification.
 *
 * The icon is displayed using the provided [image]. A badge is displayed on top
 * right corner of the icon if the [badgeCount] is greater than 0.
 *
 * @param image The content of the icon to display. This will typically be an
 *   [ImageContent] that is rendered by an `IconImage`.
 * @param badgeContentDescription The content description for the badge, for accessibility purposes.
 *   This is only used if [badgeCount] is greater than 0.
 * @param modifier Modifier to be applied to the root layout (the BadgedBox).
 * @param badgeCount The number to display in the badge. If 0 or less, the badge is not displayed.
 *
 * Example Usage:
 *
 * ```kotlin
 * IconBadge(
 *     image = ImageContent.DrawableResource(R.drawable.ic_notification),
 *     badgeContentDescription = "New Notifications",
 *     badgeCount = 3
 * )
 * ```
 */
@Composable
fun IconBadge(
    image: ImageContent,
    badgeContentDescription: String,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
) {
    val badgeOffsets = Modifier
        .offset(
            dimensionResource(R.dimen.badge_x_offset),
            dimensionResource(R.dimen.badge_y_offset)
        )
    val imageDimens: Modifier = Modifier
        .size(dimensionResource(R.dimen.image_size_medium))
        .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small)))

    return BadgedBox(
        modifier = modifier,
        badge = {
            if(badgeCount > 0) {
                Badge(
                    modifier = badgeOffsets,
                ) {
                    Text(
                        badgeCount.toString(),
                        modifier = modifier.semantics {
                            contentDescription = badgeContentDescription
                        }
                    )
                }
            }
        },
    ) {
        IconImage(
            image = image,
            modifier = imageDimens,
        )
    }
}

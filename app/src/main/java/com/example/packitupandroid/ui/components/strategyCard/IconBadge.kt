package com.example.packitupandroid.ui.components.strategyCard

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

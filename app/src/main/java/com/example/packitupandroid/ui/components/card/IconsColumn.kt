package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R

@Composable
fun IconsColumn(
    imageUri: Int? = null,
    imageVector1: ImageVector? = null,
    imageVector2: ImageVector? = null,
    firstBadgeCount: Int? = 0,
    secondBadgeCount: Int? = 0,
    isShowBadgeCount: Boolean = true,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when {
            imageUri != null -> {
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    painter = painterResource(imageUri ?: R.drawable.ic_broken_image),
                    contentDescription = null,
                )
            }

            imageVector1 != null -> {
                // Condition 'imageVector1 != null' is always 'true'
                // but can't just do imageVector1
                BadgedBox(
                    modifier = Modifier,
                    badge = {
                        if (isShowBadgeCount) Badge(
                            modifier = Modifier
                                .offset(
                                    dimensionResource(R.dimen.badge_x_offset),
                                    dimensionResource(R.dimen.badge_y_offset)
                                ),
                        ) {
                            Text(firstBadgeCount.toString())
                        }
                    },
                ) {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        imageVector = imageVector1,
                        contentDescription = null,
                    )
                }
                imageVector2?.let {
                    BadgedBox(
                        modifier = Modifier,
                        badge = {
                            if (isShowBadgeCount) Badge(
                                modifier = Modifier
                                    .offset(
                                        dimensionResource(R.dimen.badge_x_offset),
                                        dimensionResource(R.dimen.badge_y_offset)
                                    ),
                            ) {
                                if (isShowBadgeCount) Text(secondBadgeCount.toString())
                            }
                        },
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            imageVector = it,
                            contentDescription = null,
                        )
                    }
                }
            }
            else -> {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_label_24),
                    contentDescription = "",
                )
            }
        }
    }
}


@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemWithImageUriIconColumn() {
    IconsColumn(
        imageUri = R.drawable.pug,
    )
}

@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemWithOutImageUriIconColumn() {
    IconsColumn()
}

@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardBoxIconColumn() {
    IconsColumn(
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        firstBadgeCount = 5,
    )
}


@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardCollectionIconColumn() {
    IconsColumn(
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        firstBadgeCount = 5,
        secondBadgeCount = 2,
    )
}


@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemSummaryIconColumn() {
    IconsColumn(
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        firstBadgeCount = 5,
    )
}

@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardBoxSummaryIconColumn() {
    IconsColumn(
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        firstBadgeCount = 5,
        isShowBadgeCount = false,
    )
}

@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardCollectionSummaryIconColumn() {
    IconsColumn(
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        firstBadgeCount = 5,
        secondBadgeCount = 2,
        isShowBadgeCount = false,
    )
}


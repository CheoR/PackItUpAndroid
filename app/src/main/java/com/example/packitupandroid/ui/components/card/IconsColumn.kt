package com.example.packitupandroid.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Label
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
import com.example.packitupandroid.R
import com.example.packitupandroid.model.BaseCardData
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Summary

@Composable
fun IconsColumn(
    data: BaseCardData,
    cardType: CardType = CardType.Default,
) {
    when(data) {
        is Item -> {
            val image = data.imageUri
            ProjectIcons(
                icon1 = if (image != null) ColumnIcon.UriIcon(image) else ColumnIcon.VectorIcon(Icons.Default.Label),
                isShowBadgeCount = cardType is CardType.Summary
            )
        }
        is Box -> {
            ProjectIcons(
                icon1 = ColumnIcon.VectorIcon(Icons.Default.Label),
                badgeCount1 = data.items.size,
            )
        }
        is Collection -> {
            ProjectIcons(
                icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
                icon2 = ColumnIcon.VectorIcon(Icons.Default.Label),
                badgeCount1 = data.boxes.size,
                badgeCount2 = data.boxes.sumOf { it.items.size },
            )
        }
        is Summary -> {
            val (count, icon) = when(data.id) {
                "collections" -> Pair(data.collections.size, Icons.Default.Category)
                "boxes" -> Pair(data.boxes.size, ImageVector.vectorResource(R.drawable.ic_launcher_foreground))
                else -> Pair(data.items.size, Icons.Default.Label)
            }
            ProjectIcons(
                icon1 = ColumnIcon.VectorIcon(icon),
                badgeCount1 = count,
            )
        }
    }
}

@Composable
fun ProjectIcons(
    modifier: Modifier = Modifier,
    icon1: ColumnIcon = ColumnIcon.VectorIcon(Icons.Default.Label),
    icon2: ColumnIcon? = null,
    badgeCount1: Int? = 0,
    badgeCount2: Int? = 0,
    isShowBadgeCount: Boolean = true,
) {
    val imageDimens: Modifier = Modifier
        .size(dimensionResource(R.dimen.image_size_medium))
        .clip(RoundedCornerShape(dimensionResource(R.dimen.roundness_small)))
    val badgeOffsets = Modifier
        .offset(
            dimensionResource(R.dimen.badge_x_offset),
            dimensionResource(R.dimen.badge_y_offset)
        )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(dimensionResource(R.dimen.image_size_medium)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when(icon1) {
            is ColumnIcon.UriIcon -> {
                Image(
                    modifier = imageDimens,
                    painter = painterResource(icon1.uri as Int),
                    contentDescription = null,
                )
            }
            is ColumnIcon.VectorIcon -> {
                BadgedBox(
                    modifier = Modifier,
                    badge = {
                        if (isShowBadgeCount) Badge(
                            modifier = badgeOffsets,
                        ) {
                            Text(badgeCount1.toString())
                        }
                    },
                ) {
                    Icon(
                        modifier = imageDimens,
                        imageVector = icon1.imageVector,
                        contentDescription = null,
                    )
                }
            }
        }

        when(icon2) {
            is ColumnIcon.VectorIcon -> {
                BadgedBox(
                    modifier = Modifier,
                    badge = {
                        if (isShowBadgeCount) Badge(
                            modifier = badgeOffsets,
                        ) {
                            Text(badgeCount2.toString())
                        }
                    },
                ) {
                    Icon(
                        modifier = imageDimens,
                        imageVector = icon2.imageVector,
                        contentDescription = null,
                    )
                }
            }
            else -> {}
        }
    }
}


@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemWithImageUriIconColumn() {
    ProjectIcons(
        icon1 = ColumnIcon.UriIcon(R.drawable.pug)
    )
}

@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemWithOutImageUriIconColumn() {
    ProjectIcons()
}

@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardBoxIconColumn() {
    ProjectIcons(
        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
        badgeCount1 = 5,
    )
}


@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardCollectionIconColumn() {
    ProjectIcons(
        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
        icon2 = ColumnIcon.VectorIcon(Icons.Default.Label),
        badgeCount1 = 5,
        badgeCount2 = 2,
    )
}


@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemSummaryIconColumn() {
    ProjectIcons(
        icon1 = ColumnIcon.VectorIcon(Icons.Default.Label),
        badgeCount1 = 5,
    )
}

@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardBoxSummaryIconColumn() {
    ProjectIcons(
        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
        badgeCount1 = 5,
    )
}

@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardCollectionSummaryIconColumn() {
    ProjectIcons(
        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
        icon2 = ColumnIcon.VectorIcon(Icons.Default.Label),
        badgeCount1 = 5,
        badgeCount2 = 2,
    )
}

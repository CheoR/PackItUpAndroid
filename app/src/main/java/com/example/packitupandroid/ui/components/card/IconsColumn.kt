package com.example.packitupandroid.ui.components.card

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.packitupandroid.R

@Composable
fun IconsColumn(
    modifier: Modifier = Modifier,
    icon1: ColumnIcon = ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label),
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
//            .fillMaxHeight()
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
            is ColumnIcon.UriStringIcon -> {
                if (icon1.uri != null) {
                    val bitmap = base64ToBitmap(icon1.uri)
                    bitmap?.asImageBitmap()?.let { imageBitmap ->
                        Image(
                            modifier = imageDimens,
                            bitmap = imageBitmap,
                            contentDescription = null,
                        )
                    }
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

private fun base64ToBitmap(base64Image: String): Bitmap? {
    val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}


@Preview(
    group="Default",
    showBackground = true,
)
@Composable
fun PreviewBaseCardItemWithImageUriIconColumn() {
    IconsColumn(
        icon1 = ColumnIcon.UriIcon(R.drawable.pug)
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
    IconsColumn(
        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
        icon2 = ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label),
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
    IconsColumn(
        icon1 = ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label),
        badgeCount1 = 5,
    )
}

@Preview(
    group="Summary",
    showBackground = true,
)
@Composable
fun PreviewBaseCardBoxSummaryIconColumn() {
    IconsColumn(
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
    IconsColumn(
        icon1 = ColumnIcon.VectorIcon(ImageVector.vectorResource(R.drawable.ic_launcher_foreground)),
        icon2 = ColumnIcon.VectorIcon(Icons.AutoMirrored.Filled.Label),
        badgeCount1 = 5,
        badgeCount2 = 2,
    )
}

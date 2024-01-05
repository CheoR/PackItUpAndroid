import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.packitupandroid.R
import com.example.packitupandroid.data.local.LocalDataSource
import java.text.NumberFormat

fun Double.formatValue(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String = "",
    onCardClick: () -> Unit,
    imageId: Int? = null,
    imageVector1: ImageVector,
    imageVector2: ImageVector? = null,
    buttonIcon: ImageVector,
    onButtonIconClick: () -> Unit,
    dropdownOptions: List<String>? = null,
    value: Double = 0.00,
    isFragile: Boolean = false,
    onCheckedChange: () -> Unit,
    firstBadgeCount: Int? = 0,
    secondBadgeCount: Int? = 0,
) {
    Card(
        modifier = modifier
            .height(148.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
//            .padding(dimensionResource(R.dimen.padding_small))
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when {
                    imageId != null -> {
                        Image(
                            painter = painterResource(imageId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    // Condition 'imageVector1 != null' is always 'true'
                    // but can't just do imageVector1
                    imageVector1 != null -> {
//                        if(firstBadgeCount != 0) {
                            BadgedBox(
                                modifier = Modifier,
                                badge = {
                                    Badge(
                                        modifier = Modifier
                                            .offset(
                                                dimensionResource(R.dimen.badge_x_offset),
                                                dimensionResource(R.dimen.badge_y_offset)
                                            ),
                                    ) {
                                        Text(firstBadgeCount.toString())
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = imageVector1,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
//                        } else {
//                            Icon(
//                                imageVector = imageVector1,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(48.dp)
//                                    .clip(RoundedCornerShape(8.dp))
//                            )
//                        }
                    }
                }

                // Optional Image or icon2
                imageVector2?.let {
//                    if(secondBadgeCount != 0) {
                        BadgedBox(
                            modifier = Modifier,
                            badge = {
                                Badge(
                                    modifier = Modifier
                                    .offset(
                                        dimensionResource(R.dimen.badge_x_offset),
                                        dimensionResource(R.dimen.badge_y_offset)
                                    ),
                                ) {
                                    Text(secondBadgeCount.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                            )
                        }
//                    } else {
//                        Icon(
//                            imageVector = it,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(48.dp)
//                                .clip(RoundedCornerShape(8.dp)),
//                        )
//                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                dropdownOptions?.let {
                    BasicTextField(
                        value = dropdownOptions.first(),
                        onValueChange = { },
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Checkbox(
                        checked = isFragile,
                        onCheckedChange = { onCheckedChange() },
                        )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fragile")

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = value.formatValue(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = { onButtonIconClick() },
                    modifier = Modifier
                        .fillMaxHeight(),
                    content = {
                        Icon(
                            imageVector = buttonIcon,
                            contentDescription = "Icon Button",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSummaryCollectionCard() {
    BaseCard(
        title = "PreviewSummaryCard",
        description = "Summary for Collections",
        onCardClick = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_category_24),
        buttonIcon = Icons.Default.ArrowForward,
        onButtonIconClick = { },
        value = LocalDataSource().loadCollections().sumOf { it.totalValue },
        isFragile = true,
        onCheckedChange = {},
        firstBadgeCount = LocalDataSource().loadCollections().size,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCollectionCard() {
    BaseCard(
        title = "PreviewCollectionCard",
        description = "individusal descrition for this Collections",
        onCardClick = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        buttonIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        onButtonIconClick = { },
        value = LocalDataSource().loadCollections().first().totalValue,
        isFragile = true,
        onCheckedChange = {},
        firstBadgeCount = LocalDataSource().loadCollections().first().boxes.size,
        secondBadgeCount = LocalDataSource().loadCollections().first().boxes.sumOf { it.items.size }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxCard() {
    BaseCard(
        title = "PreviewBoxCard",
        dropdownOptions = LocalDataSource().loadCollections().map { it.name },
        description = "individusal descrition for this box",
        onCardClick = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        onButtonIconClick = { },
        value = LocalDataSource().loadBoxes().first().totalValue,
        onCheckedChange = {},
        firstBadgeCount = LocalDataSource().loadBoxes().first().items.size,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemCard() {
    BaseCard(
        title = "PreviewItemCard",
        dropdownOptions = LocalDataSource().loadBoxes().map { it.name },
        description = "descriptions optional",
        onCardClick = {},
        imageVector1 = Icons.Default.Home,
        imageId = R.drawable.pug , // R.drawable.ic_broken_image,
        buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        onButtonIconClick = { },
        value = LocalDataSource().loadItems().first().value,
        isFragile = true,
        onCheckedChange = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCollectionEditCard() {
    BaseCard(
        title = "PreviewCollectionEditCard",
        description = "individusal descrition for this Collections",
        onCardClick = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
        imageVector2 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        onButtonIconClick = { },
        value = LocalDataSource().loadCollections().first().totalValue,
        onCheckedChange = {},
        firstBadgeCount = LocalDataSource().loadCollections().first().boxes.size,
        secondBadgeCount = LocalDataSource().loadCollections().first().boxes.sumOf { it.items.size }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxEditCard() {
    BaseCard(
        title = "PreviewBoxEditCard",
        dropdownOptions = LocalDataSource().loadCollections().map { it.name },
        description = "individusal descrition for this box individusal descrition for this box individusal descrition for this box",
        onCardClick = {},
        imageVector1 = ImageVector.vectorResource(R.drawable.baseline_label_24),
        buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        onButtonIconClick = { },
        value = LocalDataSource().loadBoxes().first().totalValue,
        onCheckedChange = {},
        firstBadgeCount = LocalDataSource().loadBoxes().first().items.size,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemEditCard() {
    BaseCard(
        title = "PreviewItemEditCard",
        dropdownOptions = LocalDataSource().loadBoxes().map { it.name },
        description = "descriptions optional",
        onCardClick = {},
        imageVector1 = Icons.Default.Home,
        imageId = R.drawable.pug,
        buttonIcon =  ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
        onButtonIconClick = { },
        value = LocalDataSource().loadCollections().first().totalValue,
        isFragile = true,
        onCheckedChange = {},
    )
}
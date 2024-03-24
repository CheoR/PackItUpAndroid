package com.example.packitupandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

@Entity(
    tableName = "items",
//    ignoredColumns = ["value", "isFragile"],
)
data class Item(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String = "",
    val imageUri: String? = null, // ImageUri? = null,
    override val value: Double  = 0.0,
    override val isFragile: Boolean = false
) : BaseCardData  {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
            EditFields.IsFragile,
            EditFields.Value,
            EditFields.ImageUri,
        )
    }
    override val editFields get() = EDIT_FIELDS
}

//sealed class ImageUri {
//    data class StringUri(val uri: String) : ImageUri()
//    data class ResourceUri(val resourceId: Int) : ImageUri()
//}

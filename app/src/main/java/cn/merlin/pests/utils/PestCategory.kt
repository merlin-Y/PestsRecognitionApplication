package cn.merlin.pests.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pestcategory")
data class PestCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryID")
    var cid: Long? = null,
    @ColumnInfo(name = "categoryName")
    var categoryName: String,
    @ColumnInfo(name = "categoryDescription")
    var categoryDescription: String,
) {
}
package cn.merlin.pests.utils.model

import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import cn.merlin.pests.utils.PestCategory

class PestCategoryModel(pestCategory: PestCategory) {
    @ColumnInfo(name = "categoryID")
    var cid: Long? = pestCategory.cid
    @ColumnInfo(name = "categoryName")
    var categoryName = mutableStateOf(pestCategory.categoryName)
    @ColumnInfo(name = "categoryDescription")
    var categoryDescription = mutableStateOf(pestCategory.categoryDescription)
    @ColumnInfo(name = "deleted")
    var deleted = mutableStateOf(pestCategory.deleted)
}
package cn.merlin.pests.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pest")
data class Pest (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pestID")
    var pid: Long? = null,
    @ColumnInfo(name = "pestName")
    var pestName: String,
    @ColumnInfo(name = "pestImage")
    var pestImage: String,
    @ColumnInfo(name = "pestDescription")
    var pestDescription: String,
    @ColumnInfo(name = "pestSolution")
    var pestSolutio: String,
    @ColumnInfo(name = "categoryID")
    var categoryid: Int,
    @ColumnInfo(name = "deleted")
    var deleted: Int = 0
){
}
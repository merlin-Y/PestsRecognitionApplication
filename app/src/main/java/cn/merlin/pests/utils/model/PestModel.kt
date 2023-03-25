package cn.merlin.pests.utils.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import cn.merlin.pests.utils.Pest

class PestModel(pest: Pest){
    @ColumnInfo(name = "pestID")
    var pid: Long? = pest.pid
    @ColumnInfo(name = "pestName")
    var pestName = mutableStateOf(pest.pestName)
    @ColumnInfo(name = "pestImage")
    var pestImage = pest.pestImage
    @ColumnInfo(name = "pestDescription")
    var pestDescription = mutableStateOf(pest.pestDescription)
    @ColumnInfo(name = "pestSolution")
    var pestSolutio = mutableStateOf(pest.pestSolutio)
    @ColumnInfo(name = "categoryID")
    var categoryid = mutableStateOf(pest.categoryid)
    @ColumnInfo(name = "deleted")
    var deleted = mutableStateOf(pest.deleted)
}
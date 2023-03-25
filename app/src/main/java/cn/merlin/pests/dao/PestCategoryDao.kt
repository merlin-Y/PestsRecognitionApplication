package cn.merlin.pests.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.merlin.pests.utils.PestCategory

@Dao
interface PestCategoryDao {
    @Query("select * from pestcategory")
    fun queryAll(): MutableList<PestCategory>

    @Insert
    fun insert(pestCategory: PestCategory?)

    @Update
    fun update(pestCategory: PestCategory)

    @Delete
    fun delete(pestCategory: PestCategory)
    @Query("delete from pestcategory")
    fun deleteAll()
}
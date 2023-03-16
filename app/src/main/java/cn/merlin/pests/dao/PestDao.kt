package cn.merlin.pests.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.merlin.pests.utils.Pest

@Dao
interface PestDao {
    @Query("select * from pest")
    fun queryAll(): MutableList<Pest>

    @Insert
    fun insert(pest: Pest?)

    @Update
    fun update(pest: Pest)

    @Delete
    fun delete(pest: Pest)

    @Query("delete from pest")
    fun deleteAll()
}
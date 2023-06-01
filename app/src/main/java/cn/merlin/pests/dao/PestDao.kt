package cn.merlin.pests.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.merlin.pests.utils.Pest
import kotlinx.coroutines.selects.select

@Dao
interface PestDao {
    @Query("select * from pest")
    fun queryAll(): MutableList<Pest>

    @Query("select * from pest where deleted = 0")
    fun selectAll(): MutableList<Pest>

    @Insert
    fun insert(pest: Pest?)

    @Update
    fun update(pest: Pest)

    @Delete
    fun delete(pest: Pest)

    @Query("delete from pest")
    fun deleteAll()

    @Query("select * from pest where pestName like '%' || :Name || '%'")
    fun queryByName(Name: String): MutableList<Pest>

    @Query("select * from pest where pestDescription like '%' || :des || '%'")
    fun queryBuDes(des: String): MutableList<Pest>

    @Query("select * from pest where pestSolution like '%' || :sol || '%'")
    fun queryBySol(sol: String): MutableList<Pest>

    @Query("select * from pest where categoryid = :cId")
    fun groupBycId(cId: Long): MutableList<Pest>
}
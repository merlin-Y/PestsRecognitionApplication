package cn.merlin.pests.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.merlin.pests.dao.PestCategoryDao
import cn.merlin.pests.dao.PestDao
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory

@Database(entities = [Pest::class,PestCategory::class], version = 3, exportSchema = false)
abstract class PestDB : RoomDatabase(){
    abstract fun getPestDao(): PestDao
    abstract fun getPestCategoryDao(): PestCategoryDao
}
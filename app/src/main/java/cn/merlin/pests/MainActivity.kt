package cn.merlin.pests

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.room.Room
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.layout.ButtomBar
import cn.merlin.pests.ui.layout.PestAppMain
import cn.merlin.pests.ui.layout.SearchBar
import cn.merlin.pests.ui.theme.PestsTheme
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory

class MainActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pestDB = Room.databaseBuilder(applicationContext, PestDB::class.java,"pest.db").allowMainThreadQueries().build()
        if(pestDB.getPestCategoryDao().queryAll().size == 0)
            pestDB.getPestCategoryDao().insert(PestCategory(categoryName = "Default", categoryDescription = "Default Category"))
        setContent {
            PestsTheme {
                val pestList by remember { mutableStateOf(pestDB.getPestDao().queryAll()) }
                val categoryList by remember { mutableStateOf(pestDB.getPestCategoryDao().queryAll()) }
                HomePage(pestDB,categoryList,pestList)
            }
        }
    }
}

@Composable
fun HomePage(pestDB: PestDB, categoryList: MutableList<PestCategory>, pestList: MutableList<Pest>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        Column (modifier = Modifier){
            Column(modifier = Modifier.weight(1f)) {
                SearchBar()
                PestAppMain(pestDB, categoryList, pestList)
            }
            ButtomBar(pestDB,categoryList,pestList)
        }
    }
}
package cn.merlin.pests

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.room.Room
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.layout.AccountMain
import cn.merlin.pests.ui.layout.ButtomBar
import cn.merlin.pests.ui.layout.PestAppMain
import cn.merlin.pests.ui.layout.SearchBar
import cn.merlin.pests.ui.theme.PestsTheme
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pestDB = Room.databaseBuilder(applicationContext, PestDB::class.java, "pest.db")
            .allowMainThreadQueries().build()
        if (pestDB.getPestCategoryDao().queryAll().size == 0)
            pestDB.getPestCategoryDao().insert(
                PestCategory(
                    categoryName = "Default",
                    categoryDescription = "Default Category"
                )
            )
        requestPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
        ) {
            setContent {
                PestsTheme {
                    val pestList = remember { mutableStateListOf<PestModel>() }
                    val categoryList = remember { mutableStateListOf<PestCategoryModel>() }
                    val plist = pestDB.getPestDao().queryAll()
                    val CList = pestDB.getPestCategoryDao().queryAll()
                    val selectedPage = remember { mutableStateOf(0) }
                    pestList.clear()
                    categoryList.clear()
                    for (pest in plist) {
                        pestList.add(PestModel(pest))
                    }
                    for (category in CList) {
                        categoryList.add(PestCategoryModel(category))
                    }
                    Column() {
                        if(selectedPage.value == 0){
                            Column(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surface)) {
                                SearchBar(pestDB = pestDB, categoryList = categoryList, pestList = pestList)
                                PestAppMain(pestDB, categoryList, pestList)
                            }
                        }
                        else if (selectedPage.value == 1) {
                            Column(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surface)) {
                                AccountMain()
                            }
                        }
                        ButtomBar(pestDB, categoryList, pestList, selectedPage)
                    }

                }
            }
        }
    }

    private fun requestPermissions(vararg permissions: String, onResult: (List<String>) -> Unit) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val failed = result.filter { !it.value }.keys
            onResult(failed.toList())
        }.launch(arrayOf(*permissions))
    }
}
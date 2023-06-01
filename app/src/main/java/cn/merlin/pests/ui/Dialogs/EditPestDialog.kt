package cn.merlin.pests.ui.Dialogs

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel
import java.io.File
import java.io.FileInputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPestDialog(
    showDialog: MutableState<Boolean>,
    pestModel: PestModel,
    pestDB: PestDB,
    categoryList: SnapshotStateList<PestCategoryModel>
) {
    val imagefile = File(pestModel.pestImage)
    val input = FileInputStream(imagefile)
    val map = BitmapFactory.decodeStream(input)
    val selectedCategory = remember { mutableStateOf(categoryList[0]) }
    val expend = remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = { showDialog.value = false },
    ) {
        Surface(
            modifier = Modifier
                .width(400.dp)
                .height(340.dp)
                .background(MaterialTheme.colorScheme.surface),
            shadowElevation = 5.dp,
            tonalElevation = 5.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)

            ) {
                Row() {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "病虫识别结果：", fontSize = 16.sp)
                        Text(text = "病虫名：" + pestModel.pestName.value, fontSize = 14.sp)
                        LazyColumn(modifier = Modifier.height(68.dp)) {
                            item {
                                Text(
                                    text = "描述：" + pestModel.pestDescription.value,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Image(
                        bitmap = map.asImageBitmap(),
                        null,
                        modifier = Modifier
                            .height(125.dp)
                            .width(125.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    LazyColumn(modifier = Modifier.height(45.dp)) {
                        item {
                            Text(
                                text = "防治方案：" + pestModel.pestSolutio.value,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Column {

                        Row {
                            Surface(
                                shadowElevation = 1.dp,
                                tonalElevation = 1.dp,
                                shape = MaterialTheme.shapes.extraSmall
                            ) {
                                TextField(
                                    value =
                                    if (selectedCategory.value.categoryName.value.equals("Default"))
                                        "将其添加到类别（默认Default）"
                                    else
                                        selectedCategory.value.categoryName.value,
                                    onValueChange = {
                                        selectedCategory.value.categoryName.value = it
                                    },
                                    readOnly = true,
                                    modifier = Modifier
                                        .width(280.dp),
                                    textStyle = TextStyle(fontSize = 14.sp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = { expend.value = !expend.value }) {
                                            Icon(Icons.Filled.ArrowDropDown, null)
                                        }
                                    }
                                )
                            }

                        }
                        DropdownMenu(
                            expanded = expend.value,
                            onDismissRequest = { expend.value = false },
                            modifier = Modifier.width(120.dp)
                        ) {
                            categoryList.forEach {
                                if (it.deleted.value == 0)
                                    DropdownMenuItem(
                                        text = { Text(text = it.categoryName.value) },
                                        onClick = {
                                            selectedCategory.value = it
                                            expend.value = false
                                        })
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Button(
                        onClick = { showDialog.value = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.Cancel, null)
                            Text(text = "取消", fontSize = 18.sp)
                        }
                    }
                    Button(
                        onClick = {
                            showDialog.value = false
                            if(pestModel.categoryid.value != selectedCategory.value.cid){
                                pestDB.getPestCategoryDao().update(PestCategory(
                                    selectedCategory.value.cid,
                                    selectedCategory.value.categoryName.value,
                                    selectedCategory.value.categoryDescription.value,
                                    selectedCategory.value.deleted.value
                                ))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.Done, null)
                            Text(text = "确认", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
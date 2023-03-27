package cn.merlin.pests.ui.Dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.R
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPestDialog(
    showDialog: MutableState<Boolean>,
    categoryList: MutableState<MutableList<PestCategoryModel>>,
    pestList: MutableState<MutableList<PestModel>>,
    pestDB: PestDB
) {
    val expend = remember { mutableStateOf(false) }
    val pestName = remember { mutableStateOf("茶小绿叶蝉") }
    val pestDescription =
        remember { mutableStateOf("该虫主要以成虫、若虫刺吸茶树嫩梢汁液，雌成虫产卵于嫩梢茎内，致使茶树生长受阻，被害芽叶卷曲、硬化，叶尖、叶缘红褐焦枯。") }
    val pestSolutio =
        remember { mutableStateOf("经常检查，每百叶有虫夏茶达6头、秋茶达12头，开始用药；分期分批及时采茶，减少虫卵量；清除杂草，减低虫源。") }
    val pestImage = remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf(1) }
    val selectedCategory = remember { mutableStateOf(categoryList.value[0]) }
    val pestModel = remember {
        mutableStateOf<Pest>(
            Pest(
                pestName = "茶小绿叶蝉",
                pestDescription = "该虫主要以成虫、若虫刺吸茶树嫩梢汁液，雌成虫产卵于嫩梢茎内，致使茶树生长受阻，被害芽叶卷曲、硬化，叶尖、叶缘红褐焦枯。",
                pestSolutio = "经常检查，每百叶有虫夏茶达6头、秋茶达12头，开始用药；分期分批及时采茶，减少虫卵量；清除杂草，减低虫源。",
                pestImage = "",
                categoryid = 1
            )
        )
    }
    Dialog(
        onDismissRequest = { showDialog.value = false },
    ) {
        Surface(
            modifier = Modifier
                .width(400.dp)
                .height(305.dp)
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
                        Text(text = "病虫名：" + pestModel.value.pestName, fontSize = 14.sp)
                        LazyColumn(modifier = Modifier.height(68.dp)) {
                            item {
                                Text(
                                    text = "描述：" + pestModel.value.pestDescription,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Image(
                        painterResource(id = R.drawable.pest1),
                        null,
                        modifier = Modifier
                            .height(125.dp)
                            .width(125.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Column() {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(text = "查找解决方案", color = MaterialTheme.colorScheme.onSurface)
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
                            categoryList.value.forEach {
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
                        onClick = { /*TODO*/ },
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
                            val pest = Pest(
                                pestName = pestName.value,
                                pestDescription = pestDescription.value,
                                pestSolutio = pestSolutio.value,
                                pestImage = pestImage.value,
                                categoryid = categoryId.value
                            )
                            pestList.value.add(PestModel(pest))
                            showDialog.value = false
                            Thread {
                                pestDB.getPestDao().insert(pest)
                            }.start()
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

//@SuppressLint("UnrememberedMutableState")
//@Preview
//@Composable
//fun DialogPreview(){
//    AddPestDialog(mutableStateOf(true), mutableStateOf(mutableListOf<PestCategoryModel>(PestCategoryModel(pestCategory = PestCategory(1,"Default","   ",0)))))
//}
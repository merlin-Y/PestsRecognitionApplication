package cn.merlin.pests.ui.Dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPestDialog(
    showDialog: MutableState<Boolean>,
    categoryList: MutableState<MutableList<PestCategoryModel>>
) {
    var expend = remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = { showDialog.value = false },

        ) {
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 5.dp)
                .width(400.dp)
                .height(240.dp),
            shadowElevation = 5.dp,
            tonalElevation = 5.dp,
            shape = MaterialTheme.shapes.small
        ) {
            Column() {
                Row() {
                    Column {
                        Text(text = "病虫识别结果：", fontSize = 16.sp)
                        Text(text = "病虫名：", fontSize = 12.sp)
                    }
                    Icon(Icons.Filled.Image, null)
                }
                Text(text = "描述：")
                Column() {
                    Button(
                        onClick = { /*TODO*/ },
                    ) {
                        Text(text = "查找解决方案")
                    }
                    Column {
                        var selectedCategory by remember { mutableStateOf(categoryList.value[0]) }
                        Row {
                            TextField(
                                value =
                                if (selectedCategory.categoryName.value.equals("Default"))
                                    "将其添加到类别（默认Default）"
                                else
                                    selectedCategory.categoryName.value,
                                onValueChange = { selectedCategory.categoryName.value = it },
                                readOnly = true,
                                modifier = Modifier.width(280.dp),
                                textStyle = TextStyle(fontSize = 14.sp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                trailingIcon = {
                                    IconButton(onClick = { expend.value = !expend.value }) {
                                        Icon(Icons.Filled.ArrowDropDown, null)
                                }
                        }
                        )

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
                                    selectedCategory = it
                                    expend.value = false
                                })
                        }
                    }
                }
            }
        }
    }
}
}
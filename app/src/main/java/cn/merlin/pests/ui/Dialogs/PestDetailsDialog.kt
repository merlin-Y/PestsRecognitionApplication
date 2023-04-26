package cn.merlin.pests.ui.Dialogs

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.utils.model.PestModel
import java.io.File
import java.io.FileInputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestDetailsDialog(
    showDialog: MutableState<Boolean>,
    pestModel: PestModel,
    pestDB: PestDB
) {
    val imagefile = File(pestModel.pestImage)
    val input = FileInputStream(imagefile)
    val map = BitmapFactory.decodeStream(input)
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
                    Text(
                        text =
                        if (pestModel.categoryid.value == 1) "默认分组Default"
                        else "所在分组：" + pestDB.getPestCategoryDao()
                            .findById(pestModel.categoryid.value).categoryName,
                        fontSize = 12.sp
                    )
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
package cn.merlin.pests.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.More
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.R
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.Dialogs.PestDetailsDialog
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.model.PestModel
import java.io.File
import java.io.FileInputStream

@Composable
fun PestCard(pestDB: PestDB, pestModel: PestModel, modifier: Modifier = Modifier) {
    val moreButtonClicked = remember { mutableStateOf(false) }
    val imagefile = File(pestModel.pestImage)
    val input = FileInputStream(imagefile)
    val map = remember { mutableStateOf(BitmapFactory.decodeStream(input)) }
    input.close()
    Card(
        modifier
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 5.dp,
                bottom = 15.dp
            )
            .fillMaxWidth()
            .height(143.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        LazyRow() {
            item()
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.width(310.dp)
                ) {
                        Image(
                            bitmap = map.value!!.asImageBitmap(),
                            null,
                            modifier = Modifier
                                .height(160.dp)
                                .width(160.dp),
                            contentScale = ContentScale.Crop
                        )
                    Column(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(text = "▪" + pestModel.pestName.value, fontSize = 20.sp)
                        Text(
                            text = "    " + pestModel.pestDescription.value,
                            fontSize = 15.sp,
                            modifier = Modifier.height(95.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 4
                        )
                    }
                }
            }
            item {
                Button(
                    shape = RectangleShape,
                    onClick = {
                        moreButtonClicked.value = !moreButtonClicked.value
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff22c64d))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.More, null)
                        Text(text = "更多")
                    }
                }
            }
            item {
                Button(
                    shape = RectangleShape,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0166ff))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Edit, null)
                        Text(text = "编辑")
                    }
                }
            }
            item {
                Button(
                    shape = RectangleShape,
                    onClick = {
                        Thread {
                            pestModel.deleted.value = 1;
                            pestDB.getPestDao().update(
                                Pest(
                                    pid = pestModel.pid,
                                    pestName = pestModel.pestName.value,
                                    pestImage = pestModel.pestImage,
                                    pestDescription = pestModel.pestDescription.value,
                                    pestSolutio = pestModel.pestSolutio.value,
                                    categoryid = pestModel.categoryid.value,
                                    deleted = pestModel.deleted.value
                                )
                            )
                        }.start()
                    },
                    modifier = Modifier.fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xffff5d5b))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Delete, null)
                        Text(text = "删除")
                    }
                }
            }
        }
    }
    if (moreButtonClicked.value)
        PestDetailsDialog(moreButtonClicked, pestModel, pestDB)
}
package cn.merlin.pests.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.More
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.R
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel

@Composable
fun PestAppMain(
    pestDB: PestDB,
    categoryList: MutableState<MutableList<PestCategoryModel>>,
    pestList: MutableState<MutableList<PestModel>>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        var showDialog = remember { mutableStateOf(false) }
        Column() {
            val liststate = rememberLazyListState()
//            IconButton(
//                onClick = {
//                    Thread{
//                        pestDB.getPestDao().insert(Pest(
//                            pestName = "merlin", pestSolutio = "1", pestImage = "pestImage",pestDescription = "merlin", categoryid = 1
//                        ))
//                        pestList.value = pestDB.getPestDao().queryAll()
//                    }.start()
//                }) {
//                Icon(Icons.Filled.Add, null)
//            }
//            IconButton(
//                onClick = {
//                    Thread{
//                        pestList = pestDB.getPestDao().queryAll()
//                    }.start()
//                }) {
//                Icon(Icons.Filled.Add, null)
//            }
            Row {
                IconButton(
                    onClick = {
                        Thread {
                            pestDB.getPestCategoryDao().insert(
                                PestCategory(
                                    categoryName = "pest", categoryDescription = "1"
                                )
                            )
                            val CList = pestDB.getPestCategoryDao().queryAll()
                            categoryList.value.clear()
                            for (category in CList) {
                                categoryList.value.add(PestCategoryModel(category))
                            }
                        }.start()
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .padding(start = 20.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        null,
                        modifier = Modifier,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                if (categoryList.value.size > 1)
                    LazyRow(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = liststate
                    ) {
                        items(items = categoryList.value) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    text = it.categoryName.value,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(400),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 36.dp, end = 36.dp, top = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(items = pestList.value) {
                    if (it.deleted.value == 0)
                        PestCard(pestDB = pestDB, pestModel = it)
                }
            }
        }
    }
}

@Composable
fun PestCard(pestDB: PestDB, pestModel: PestModel) {
//    val painter = rememberCoilPainter(pest.pestImage)
    Card(
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 15.dp
            )
            .fillMaxWidth()
            .height(158.dp),
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
                        painterResource(id = R.drawable.pest1),
                        null,
                        modifier = Modifier
                            .height(160.dp)
                            .width(160.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(text = pestModel.pestName.value, fontSize = 20.sp)
                        Text(text = "    " + pestModel.pestDescription.value, fontSize = 15.sp)
                    }
                }
            }
            item {
                Button(
                    shape = RectangleShape,
                    onClick = { /*TODO*/ },
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
}
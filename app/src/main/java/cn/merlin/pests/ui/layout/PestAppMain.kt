package cn.merlin.pests.ui.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.components.PestCard
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PestAppMain(
    pestDB: PestDB,
    categoryList: SnapshotStateList<PestCategoryModel>,
    pestList: SnapshotStateList<PestModel>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(bottom = 4.dp)
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
                            categoryList.clear()
                            for (category in CList) {
                                categoryList.add(PestCategoryModel(category))
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
                if (categoryList.size > 1)
                    LazyRow(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = liststate
                    ) {
                        items(items = categoryList) {
                            Button(
                                onClick =
                                {
                                    pestList.clear()
                                    val pests = pestDB.getPestDao().groupBycId(it.cid!!)
                                    for (pest in pests) {
                                        pestList.add(PestModel(pest))
                                    }
                                },
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
                    .padding(start = 26.dp, end = 26.dp, top = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = pestList) {
                    val dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                        Thread {
                            it.deleted.value = 1;
                            pestDB.getPestDao().update(
                                Pest(
                                    pid = it.pid,
                                    pestName = it.pestName.value,
                                    pestImage = it.pestImage,
                                    pestDescription = it.pestDescription.value,
                                    pestSolutio = it.pestSolutio.value,
                                    categoryid = it.categoryid.value,
                                    deleted = it.deleted.value
                                )
                            )
                        }.start()
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        dismissThresholds = { direction ->
                            androidx.compose.material.FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                        },
                        directions = setOf(DismissDirection.StartToEnd),
                        background = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                            ) {

                            }
                        }
                    ) {
                        if (it.deleted.value == 0)
                            PestCard(pestDB = pestDB, pestModel = it)
                    }
                }
            }
        }
    }
}



//fun ImageList(images: List<String>) {
//    LazyColumn {
//        items(images) { imageUrl ->
//            var painter by remember { mutableStateOf<ImagePainter?>(null) }
//
//            Card(
//                modifier = Modifier.fillMaxWidth().padding(16.dp),
//                elevation = 4.dp,
//            ) {
//                if (painter != null) {
//                    Image(
//                        painter = painter!!,
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxWidth(),
//                        contentScale = ContentScale.Crop,
//                    )
//                } else {
//                    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
//                        CircularProgressIndicator(Modifier.align(Alignment.Center))
//                    }
//                }
//            }
//
//            LaunchedEffect(imageUrl) {
//                val newPainter = rememberImagePainter(
//                    data = imageUrl,
//                    builder = {
//                        // Configure Glide options here, such as cache strategy, transformations, and animations.
//                        diskCacheStrategy(DiskCacheStrategy.ALL)
//                        centerCrop()
//                    }
//                )
//                newPainter.load()
//                painter = newPainter
//            }
//        }
//    }
//}
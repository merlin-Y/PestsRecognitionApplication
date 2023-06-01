package cn.merlin.pests.ui.layout

import android.animation.ValueAnimator
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.components.PestCard
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun PestAppMain(
    pestDB: PestDB,
    categoryList: SnapshotStateList<PestCategoryModel>,
    pestList: SnapshotStateList<PestModel>,
    modifier: Modifier = Modifier
) {
    val isSelected = remember { mutableStateOf(false) }
    val isOpened = remember { mutableStateOf(false) }
    val categoryName = remember { mutableStateOf("") }
    val TextWidth = animateDpAsState(if(!isSelected.value) 0.dp else 200.dp)
    val useDarkTheme = isSystemInDarkTheme()
    val surfaceColor = if(!useDarkTheme) Color(0xFFFFFBFE) else Color(0xFF1C1B1F)
    val secondColor = if(!useDarkTheme) Color(0xFFE8DEF8) else Color(0xFF4A4458)
    val color = remember { mutableStateOf(surfaceColor) }
    val isLongClicked = remember { mutableStateOf(false) }
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
                Box(modifier = Modifier.padding(start = 20.dp)){
                    IconButton(
                        onClick = {
                            if(!isSelected.value){
                                isSelected.value = !isSelected.value
                                color.value = secondColor
                            }
                            else if(isSelected.value && !isOpened.value){
                                isSelected.value = !isSelected.value
                                color.value = surfaceColor
                            }
                            else if(isSelected.value && isOpened.value){
                                CoroutineScope(Dispatchers.IO).
                                launch{
                                    val category = PestCategory(
                                        categoryName = categoryName.value,
                                        categoryDescription = ""
                                    )
                                    pestDB.getPestCategoryDao().insert(category)
                                    val categorys = pestDB.getPestCategoryDao().queryAll()
                                    categoryList.clear()
                                    for(category in categorys){
                                        categoryList.add(PestCategoryModel(category))
                                    }
                                    categoryName.value = ""
                                }
                                isSelected.value = !isSelected.value
                                isOpened.value = false
                                color.value = surfaceColor
                            }

                        },
                        modifier = Modifier
                            .width(50.dp)
                            .background(color.value),
                        colors = IconButtonDefaults.filledIconButtonColors(Color.Transparent)
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                if (categoryList.size > 1)
                    LazyRow(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = liststate
                    ) {
                        item {
                            AnimatedVisibility(
                                visible = isSelected.value,
                                enter = slideInHorizontally(
                                    animationSpec = tween(150,150),
                                ),
                                exit = slideOutHorizontally(
                                    animationSpec = tween(150),
                                    targetOffsetX = {-5}
                                )
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .width(TextWidth.value)
                                        .height(48.dp),
                                    value = categoryName.value,
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 8, bottomStartPercent = 0, bottomEndPercent = 8),
                                    onValueChange = {
                                        categoryName.value = it
                                        isOpened.value = categoryList.size != 0
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                categoryName.value = ""
                                                isOpened.value = false
                                            }) {
                                            Icon(Icons.Filled.Clear, null)
                                        }
                                    }
                                )
                            }
                        }
                        for(category in categoryList){
                            if(category.deleted.value == 0){
                                item {
                                    Button(
                                        onClick =
                                        {
                                            Thread{
                                                pestList.clear()
                                                val pests = pestDB.getPestDao().groupBycId(category.cid!!)
                                                for (pest in pests) {
                                                    pestList.add(PestModel(pest))
                                                }
                                            }.start()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.onBackground
                                        ),
                                        modifier = if(category.cid == 1L)
                                            Modifier.padding(start =  12.dp).animateItemPlacement()
                                        else
                                            Modifier.animateItemPlacement()
                                    ) {
                                        Row() {
                                            Text(
                                                text = category.categoryName.value,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight(400),
                                                color = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.animateItemPlacement().pointerInput(Unit){
                                                    detectTapGestures(
                                                        onLongPress = {
                                                            isLongClicked.value = ! isLongClicked.value
                                                        },
                                                        onPress = {
                                                            Thread{
                                                                pestList.clear()
                                                                val pests = pestDB.getPestDao().groupBycId(category.cid!!)
                                                                for (pest in pests) {
                                                                    pestList.add(PestModel(pest))
                                                                }
                                                            }.start()
                                                        }
                                                    )
                                                }
                                            )
                                        }
                                        if(isLongClicked.value){
                                            IconButton(
                                                onClick = {
                                                    Thread{
                                                        category.deleted.value = 1
                                                        pestDB.getPestCategoryDao().update(
                                                            PestCategory(
                                                                cid = category.cid,
                                                                categoryName = category.categoryName.value,
                                                                categoryDescription = "",
                                                                deleted = category.deleted.value
                                                            )
                                                        )
                                                    }.start()
                                                },
                                                modifier = Modifier.size(20.dp).padding(top = 2.dp).animateItemPlacement()
                                            ) {
                                                Icon(Icons.Filled.Clear, contentDescription = "")
                                            }
                                        }
                                    }
                                }
                            }
                        }
//                        items(items = categoryList) {
//                            if(it.deleted.value == 0){
//                                Button(
//                                    onClick =
//                                    {
//                                        Thread{
//                                            pestList.clear()
//                                            val pests = pestDB.getPestDao().groupBycId(it.cid!!)
//                                            for (pest in pests) {
//                                                pestList.add(PestModel(pest))
//                                            }
//                                        }.start()
//                                    },
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = MaterialTheme.colorScheme.surface,
//                                        contentColor = MaterialTheme.colorScheme.onBackground
//                                    ),
//                                    modifier = if(it.cid == 1L)
//                                        Modifier.padding(start =  12.dp).animateItemPlacement()
//                                    else
//                                        Modifier.animateItemPlacement()
//                                ) {
//                                    Row() {
//                                        Text(
//                                            text = it.categoryName.value,
//                                            fontSize = 15.sp,
//                                            fontWeight = FontWeight(400),
//                                            color = MaterialTheme.colorScheme.secondary,
//                                            modifier = Modifier.animateItemPlacement().pointerInput(Unit){
//                                                detectTapGestures(
//                                                    onLongPress = {
//                                                        isLongClicked.value = true
//                                                    }
//                                                )
//                                            }
//                                        )
//                                    }
//                                   if(isLongClicked.value){
//                                       IconButton(
//                                           onClick = {
//                                               Thread{
//                                                   it.deleted.value = 1
//                                                   pestDB.getPestCategoryDao().update(
//                                                       PestCategory(
//                                                           cid = it.cid,
//                                                           categoryName = it.categoryName.value,
//                                                           categoryDescription = "",
//                                                           deleted = it.deleted.value
//                                                       )
//                                                   )
//                                               }.start()
//                                           },
//                                           modifier = Modifier.size(20.dp).padding(top = 2.dp).animateItemPlacement()
//                                       ) {
//                                           Icon(Icons.Filled.Clear, contentDescription = "")
//                                       }
//                                   }
//                                }
//                            }
//                        }
                        item {
                            if(isLongClicked.value){
                                IconButton(
                                    onClick = { isLongClicked.value = false },
                                    modifier = Modifier.animateItemPlacement()
                                ) {
                                    Icon(Icons.Filled.Clear, contentDescription = "", modifier = Modifier.animateItemPlacement())
                                }
                            }
                        }
                    }
                else
                    AnimatedVisibility(
                        visible = isSelected.value,
                        enter = slideInHorizontally(
                            animationSpec = tween(150,150),
                            ),
                        exit = slideOutHorizontally(
                            animationSpec = tween(150),
                            targetOffsetX = {-5}
                            )
                    ) {
                        TextField(
                            modifier = Modifier
                                .width(TextWidth.value)
                                .height(48.dp),
                            value = categoryName.value,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 8, bottomStartPercent = 0, bottomEndPercent = 8),
                            onValueChange = {
                                categoryName.value = it
                                isOpened.value = categoryList.size != 0
                                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        categoryName.value = ""
                                        isOpened.value = false
                                    }) {
                                    Icon(Icons.Filled.Clear, null)
                                }
                            }
                        )
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
                            PestCard(pestDB = pestDB, pestModel = it, categoryList = categoryList)
                    }
                }
            }
        }
    }

}
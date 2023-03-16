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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.R
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory

@Composable
fun PestAppMain(pestDB: PestDB,categoryList: MutableList<PestCategory>,pestList: MutableList<Pest>,modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column (){
            val liststate = rememberLazyListState()
            var pestList by remember { mutableStateOf(pestDB.getPestDao().queryAll()) }
            var categoryList by remember { mutableStateOf(pestDB.getPestCategoryDao().queryAll()) }
//            IconButton(
//                onClick = {
//                    Thread{
//                        pestDB.getPestDao().insert(Pest(
//                            pestName = "merlin", pestSolutio = "1", pestImage = "pestImage",pestDescription = "merlin", categoryid = 1
//                        ))
//                        pestList = pestDB.getPestDao().queryAll()
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
                        Thread{
                            pestDB.getPestCategoryDao().insert(PestCategory(
                                categoryName = "pest", categoryDescription = "1"
                            ))
                            categoryList = pestDB.getPestCategoryDao().queryAll()
                        }.start()
                    },
                    modifier = Modifier.width(50.dp)
                ) {
                    Icon(Icons.Filled.Add, null)
                }
                if(categoryList.size > 1)
                    LazyRow(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = liststate
                    ) {
                        items(items = categoryList) {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    text = it.categoryName,
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = pestList) {
                    PestCard(pest = it)
                }
            }
        }
    }
}

@Composable
fun PestCard(pest: Pest){
    Card(
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 5.dp
            )
            .fillMaxWidth()
            .height(158.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                    Text(text = pest.pestName, fontSize = 20.sp)
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(10.dp),
//                ){
                    Text(text = pest.pestDescription, fontSize = 15.sp)
//                }
            }
        }
    }
}

@Composable
fun AddCategoryDialog(pestDB: PestDB){
    Dialog(onDismissRequest = {  }) {

    }
}

@Preview
@Composable
fun MainPriview() {
    PestCard(Pest(pid = 1, pestName = "merlin", pestImage = "image", pestSolutio = "merlin", pestDescription = "merlin", categoryid = 1))
}
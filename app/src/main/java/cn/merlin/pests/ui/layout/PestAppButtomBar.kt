package cn.merlin.pests.ui.layout


import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.Dialogs.AddPestDialog
import cn.merlin.pests.ui.components.LabelButton
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel

@Composable
fun ButtomBar(
    pestDB: PestDB,
    categoryList: SnapshotStateList<PestCategoryModel>,
    pestList: SnapshotStateList<PestModel>,
    selectedPage : MutableState<Int>,
    modifier: Modifier = Modifier
) {
    val showDialog = remember { mutableStateOf(false) }
    val imageUri = remember{ mutableStateOf<Uri?>(null) }
    Surface(
        modifier = modifier
            .padding(top = 0.dp)
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LabelButton(icon = Icons.Filled.Home, label = "Home", modifier = Modifier.padding(start = 16.dp, top = 6.dp) ,onClick = { selectedPage.value = 0 })
//            Column(
//                modifier = modifier.padding(start = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(0.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                IconButton(onClick = { selectedPage.value = 0 }) {
//                    Icon(Icons.Filled.Home, null)
//                }
//                Text(text = "Home")
//            }
            Column() {
                Box(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                        .width(60.dp)
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        showDialog.value = !showDialog.value
                    }) {
                        Icon(Icons.Filled.Add, null)
                    }
                    if (showDialog.value) {
                        AddPestDialog(showDialog,categoryList,pestList,pestDB,imageUri)
//                        AlertDialog(
//                            onDismissRequest = {
//                                showDialog = false
//                            },
//                            title = {
//                                Text(text = "Title")
//                            },
//                            text = {
//                                Text(
//                                    "This area typically contains the supportive text " +
//                                            "which presents the details regarding the Dialog's purpose."
//                                )
//                            },
//                            confirmButton = {
//                                TextButton(
//                                    onClick = {
//                                        showDialog = false
//                                    }
//                                ) {
//                                    Text("Confirm")
//                                }
//                            },
//                            dismissButton = {
//                                TextButton(
//                                    onClick = {
//                                        showDialog = false
//                                    }
//                                ) {
//                                    Text("Dismiss")
//                                }
//                            }
//                        )
                    }
                }
            }
            LabelButton(icon = Icons.Filled.Settings, label = "Settings", modifier = Modifier.padding(end = 16.dp, top = 6.dp) ,onClick = {selectedPage.value = 1})
//            Column(
//                modifier = modifier.padding(end = 16.dp),
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                IconButton(onClick = { selectedPage.value = 1 }) {
//                    Icon(Icons.Filled.AccountCircle, null)
//                }
//                Text(text = "Account")
//            }
        }
    }
}


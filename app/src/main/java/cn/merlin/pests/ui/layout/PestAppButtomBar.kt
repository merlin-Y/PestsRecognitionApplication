package cn.merlin.pests.ui.layout

import android.widget.Toast
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

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.R
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.components.MultiFabItem
import cn.merlin.pests.ui.components.MultiFloatingActionButton
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.PestCategory

@Composable
fun ButtomBar(
    pestDB: PestDB,
    categoryList: MutableList<PestCategory>,
    pestList: MutableList<Pest>,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .padding(top = 6.dp)
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Home, null)
                }
                Text(text = "Home")
            }
            Column() {
                Box(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                        .width(60.dp)
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        showDialog = !showDialog
                    }) {
                        Icon(Icons.Filled.Add, null)
                    }
                    if(showDialog){
                        Dialog(onDismissRequest = { showDialog = !showDialog }) {

                        }
                    }
                }
            }
            Column(
                modifier = modifier.padding(end = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.AccountCircle, null)
                }
                Text(text = "Account")
            }
        }
    }
}
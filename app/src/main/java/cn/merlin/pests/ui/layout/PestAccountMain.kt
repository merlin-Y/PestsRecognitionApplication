package cn.merlin.pests.ui.layout

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.R
import cn.merlin.pests.network.RetrofitBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountMain() {
    val editor = LocalContext.current.getSharedPreferences("data",Context.MODE_PRIVATE).edit()
    val prefs = LocalContext.current.getSharedPreferences("data",Context.MODE_PRIVATE)
    editor.putString("BASE_URL","82.156.245.30:8088")
    editor.apply()
    val URL = remember {
        mutableStateOf(
            if(prefs.getString("URL","") != "") prefs.getString("URL","")
            else prefs.getString("BASE_URL",""))
    }
    Surface(modifier = Modifier
        .padding(start = 16.dp)
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = "Account",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Text(text = "Account", fontSize = 24.sp, modifier = Modifier.padding(start = 25.dp, end = 55.dp), color = MaterialTheme.colorScheme.onSurface)
                Button(
                    onClick = {
                        editor.putString("URL","")
                        URL.value = prefs.getString("BASE_URL", "")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Text(text = "恢复默认", color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "服务器地址:",
                    color = MaterialTheme.colorScheme.secondary,
                )
                TextField(
                    value = URL.value!!, onValueChange = { URL.value = it },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 5.dp)
                )
                IconButton(
                    onClick = {
                        editor.putString("URL",URL.value)
                        editor.apply()
                        RetrofitBuilder.setURL(URL.value!!)
                              },
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(Icons.Filled.Upload, contentDescription = "提交", tint = MaterialTheme.colorScheme.onSurface)
                }
            }

        }
    }
}

@Preview
@Composable
fun accountPreview() {
    AccountMain()
}
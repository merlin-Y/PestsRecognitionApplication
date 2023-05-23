package cn.merlin.pests.ui.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountMain(){
    val URL = remember {
        mutableStateOf("82.156.245.30:8088")
    }
    Surface(modifier = Modifier) {
        Row() {
            Text(text = "服务器地址:")
            TextField(value = URL.value, onValueChange = {URL.value = it})
            Button(onClick = { /*TODO*/ }) {
                Text(text = "提交")
            }
        }
    }
}

@Preview
@Composable
fun accountPreview(){
    AccountMain()
}
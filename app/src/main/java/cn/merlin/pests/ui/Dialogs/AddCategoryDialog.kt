package cn.merlin.pests.ui.Dialogs

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddCategoryDialog(showDialog: MutableState<Boolean>){
    Surface(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 5.dp)
            .width(33.dp)
            .height(24.dp),
        shape = MaterialTheme.shapes.small
    ){
        Dialog(onDismissRequest = { showDialog.value = !showDialog.value }) {

        }
    }
}
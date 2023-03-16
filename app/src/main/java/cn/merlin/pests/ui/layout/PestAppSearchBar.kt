package cn.merlin.pests.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.ui.theme.PestsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, start = 20.dp, end = 20.dp)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small),
        shadowElevation = 5.dp,
        tonalElevation = 5.dp,
        shape = MaterialTheme.shapes.small

    ) {
       TextField(
           modifier = Modifier.height(55.dp),
           value = "",
           label = { Text(text = "Input To Search",color = MaterialTheme.colorScheme.secondary, fontSize = 18.sp)},
           onValueChange = {},
           isError = false,
           shape = MaterialTheme.shapes.small,
           colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
           leadingIcon = {
               IconButton(onClick = { /*TODO*/ }) {
                   Icon(Icons.Filled.Search, contentDescription = "Search")
               }
           },
           trailingIcon = {
               IconButton(onClick = { /*TODO*/ }) {
                   Icon(Icons.Filled.Camera, contentDescription ="Take Photos" )
               }
           })
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    PestsTheme {
        SearchBar()
    }
}
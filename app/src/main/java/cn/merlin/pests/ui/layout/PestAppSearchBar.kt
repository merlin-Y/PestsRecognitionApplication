package cn.merlin.pests.ui.layout

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.ui.Dialogs.AddPestDialog
import cn.merlin.pests.ui.theme.PestsTheme
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    pestDB: PestDB,
    categoryList: SnapshotStateList<PestCategoryModel>,
    pestList: SnapshotStateList<PestModel>
) {
    val text = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap: Bitmap? ->
            if (bitmap != null) {
                val bytes = ByteArrayOutputStream().apply {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                }.toByteArray()
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                }
                imageUri.value = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )?.also { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(bytes)
                    }
                }
            }
            if (imageUri.value != null)
                showDialog.value = true
        })
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
            modifier = Modifier
                .height(55.dp),
            value = text.value,
            label = {
                Text(
                    text = "搜索",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 18.sp
                )
            },
            onValueChange = { text.value = it },
            isError = false,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                IconButton(
                    onClick = {
                        if (text.value != "") {
                            Thread {
                                var map = HashMap<Long, Pest>()
                                pestList.clear()
                                var pests = pestDB.getPestDao().queryByName(text.value)
                                for (pest in pests) {
                                    map.put(pest.pid!!, pest)
                                }
                                pests = pestDB.getPestDao().queryBuDes(text.value)
                                for (pest in pests) {
                                    map.put(pest.pid!!, pest)
                                }
                                pests = pestDB.getPestDao().queryBySol(text.value)
                                for (pest in pests) {
                                    map.put(pest.pid!!, pest)
                                }
                                for (pest in map) {
                                    pestList.add(PestModel(pest.value))
                                }
                            }.start()
                        } else {
                            pestList.clear()
                            val plist = pestDB.getPestDao().queryAll()
                            for (pest in plist) {
                                pestList.add(PestModel(pest))
                            }
                        }
                    }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            },
            trailingIcon = {
                if (text.value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            text.value = ""
                            Thread {
                                val pests = pestDB.getPestDao().queryAll()
                                pestList.clear()
                                for (pest in pests) {
                                    pestList.add(PestModel(pest))
                                }
                            }.start()
                        }) {
                        Icon(Icons.Filled.Clear, null)
                    }
                } else {
                    IconButton(
                        onClick = {
                            text.value = ""
                            launcher.launch()
                        }) {
                        Icon(Icons.Filled.Camera, null)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (text.value != "") {
                        Thread {
                            var map = HashMap<Long, Pest>()
                            pestList.clear()
                            var pests = pestDB.getPestDao().queryByName(text.value)
                            for (pest in pests) {
                                map.put(pest.pid!!, pest)
                            }
                            pests = pestDB.getPestDao().queryBuDes(text.value)
                            for (pest in pests) {
                                map.put(pest.pid!!, pest)
                            }
                            pests = pestDB.getPestDao().queryBySol(text.value)
                            for (pest in pests) {
                                map.put(pest.pid!!, pest)
                            }
                            for (pest in map) {
                                pestList.add(PestModel(pest.value))
                            }
                        }.start()
                    } else {
                        pestList.clear()
                        val plist = pestDB.getPestDao().queryAll()
                        for (pest in plist) {
                            pestList.add(PestModel(pest))
                        }
                    }
                }
            )
        )
    }
    if (showDialog.value) {
        AddPestDialog(
            showDialog = showDialog,
            categoryList = categoryList,
            pestList = pestList,
            pestDB = pestDB,
            imageUri = imageUri
        )
    }
}
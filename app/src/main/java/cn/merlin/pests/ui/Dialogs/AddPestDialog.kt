package cn.merlin.pests.ui.Dialogs

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.merlin.pests.R
import cn.merlin.pests.database.PestDB
import cn.merlin.pests.network.RetrofitBuilder
import cn.merlin.pests.network.model.PestNetworkModel
import cn.merlin.pests.utils.Pest
import cn.merlin.pests.utils.model.PestCategoryModel
import cn.merlin.pests.utils.model.PestModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.compose.material.icons.filled.Image
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPestDialog(
    showDialog: MutableState<Boolean>,
    categoryList: SnapshotStateList<PestCategoryModel>,
    pestList: SnapshotStateList<PestModel>,
    pestDB: PestDB,
    imageUri: MutableState<Uri?>
) {
    val expend = remember { mutableStateOf(false) }
    val pestName = remember { mutableStateOf("待检测") }
    val pestDescription =
        remember { mutableStateOf("待检测.") }
    val pestSolutio =
        remember { mutableStateOf("待检测。（点击查找解决方案）") }
    val pestImage = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf(categoryList[0]) }
    var filelabel = ""
    val searched = remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri.value = uri
        })
    Dialog(
        onDismissRequest = { showDialog.value = false },
    ) {
        for(c in categoryList){
            if(c.deleted.value == 0){
                selectedCategory.value = c
                break
            }
        }
        Surface(
            modifier = Modifier
                .width(400.dp)
                .height(305.dp)
                .background(MaterialTheme.colorScheme.surface),
            shadowElevation = 5.dp,
            tonalElevation = 5.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)

            ) {
                Row() {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "病虫识别结果：", fontSize = 16.sp)
                        Text(text = "病虫名：" + pestName.value, fontSize = 14.sp)
                        LazyColumn(modifier = Modifier.height(68.dp)) {
                            item {
                                Text(
                                    text = "描述：" + pestDescription.value,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        modifier = Modifier
                            .height(125.dp)
                            .width(125.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RectangleShape
                    ) {
                        if (imageUri.value == null) {
                            Icon(
                                Icons.Filled.Image,
                                null,
                                modifier = Modifier
                                    .height(125.dp)
                                    .width(125.dp),
                            )
                        } else {
                            bitmap =
                                context.contentResolver.openInputStream(imageUri.value!!)?.use {
                                    BitmapFactory.decodeStream(it)
                                }
                            Image(
                                bitmap = bitmap!!.asImageBitmap(),
                                null,
                                modifier = Modifier
                                    .height(125.dp)
                                    .width(125.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Column() {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
                            if (imageUri.value != null) {
                                Thread {
                                    val inputStream =
                                        context.contentResolver.openInputStream(imageUri.value!!)
                                    val requestBody =
                                        inputStream?.readBytes()
                                            ?.toRequestBody("image/*".toMediaTypeOrNull())
                                    val imagePart = requestBody?.let {
                                        MultipartBody.Part.createFormData(
                                            "image",
                                            "image.jpg",
                                            it
                                        )
                                    }
                                    RetrofitBuilder.service.imagePost(imagePart!!)
                                        .enqueue(object : Callback<ResponseBody> {
                                            override fun onResponse(
                                                call: Call<ResponseBody>,
                                                response: Response<ResponseBody>
                                            ) {
                                                if (response.isSuccessful) {
                                                    val responseBody = response.body()?.string()
                                                    val pests = (Gson().fromJson(
                                                        responseBody.toString(),
                                                        Array<PestNetworkModel>::class.java
                                                    ))
                                                    if (pests.isNotEmpty()) {
                                                        pestName.value = pests[0].pname
                                                        pestDescription.value =
                                                            pests[0].pdescription
                                                        pestSolutio.value = pests[0].psolution
                                                        filelabel = pests[0].plabel
                                                        searched.value = true
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "未检测到病虫，请重新发送。",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                } else {
                                                    val errorBody = response.errorBody()?.string()
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to upload image: $errorBody",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<ResponseBody>,
                                                t: Throwable
                                            ) {
                                                // 网络请求失败，显示错误信息。
                                                Toast.makeText(
                                                    context,
                                                    "Failed to upload image: ${t.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Log.i("message", t.message!!)
                                            }
                                        })
                                }.start()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Text(text = "查找解决方案", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Column {

                        Row {
                            Surface(
                                shadowElevation = 1.dp,
                                tonalElevation = 1.dp,
                                shape = MaterialTheme.shapes.extraSmall
                            ) {
                                TextField(
                                    value =
                                    if (selectedCategory.value.categoryName.value.equals("Default"))
                                        "将其添加到类别（默认Default）"
                                    else
                                        selectedCategory.value.categoryName.value,
                                    onValueChange = {
                                        selectedCategory.value.categoryName.value = it
                                    },
                                    readOnly = true,
                                    modifier = Modifier
                                        .width(280.dp),
                                    textStyle = TextStyle(fontSize = 14.sp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = { expend.value = !expend.value }) {
                                            Icon(Icons.Filled.ArrowDropDown, null)
                                        }
                                    }
                                )
                            }

                        }
                        DropdownMenu(
                            expanded = expend.value,
                            onDismissRequest = { expend.value = false },
                            modifier = Modifier.width(120.dp)
                        ) {
                            categoryList.forEach {
                                if (it.deleted.value == 0)
                                    DropdownMenuItem(
                                        text = { Text(text = it.categoryName.value) },
                                        onClick = {
                                            selectedCategory.value = it
                                            expend.value = false
                                        })
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Button(
                        onClick = {
                            showDialog.value = false
                            imageUri.value = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.Cancel, null)
                            Text(text = "取消", fontSize = 18.sp)
                        }
                    }
                    Button(
                        onClick = {
                            if (searched.value) {
                                val now = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                                val type = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                                    context.contentResolver.getType(imageUri.value!!)
                                )
                                val fileName = "$filelabel-$now.$type"
                                val file = File("/storage/emulated/0/Download/PestApplication")
                                if (!file.exists()) file.mkdirs()
                                val input =
                                    context.contentResolver.openInputStream(imageUri.value!!)
                                val buffer = ByteArray(input!!.available())
                                input.read(buffer)
                                val outputfile = File(file, fileName)
                                val output = FileOutputStream(outputfile)
                                output.write(buffer)
                                output.close()
                                input.close()
                                pestImage.value =
                                    "/storage/emulated/0/Download/PestApplication/$fileName"
                                val pest = Pest(
                                    pestName = pestName.value,
                                    pestDescription = pestDescription.value,
                                    pestSolutio = pestSolutio.value,
                                    pestImage = pestImage.value,
                                    categoryid = selectedCategory.value.cid!!
                                )
                                pestList.add(PestModel(pest))
                                imageUri.value = null
                                showDialog.value = false
                                Thread {
                                    pestDB.getPestDao().insert(pest)
                                }.start()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.Done, null)
                            Text(text = "确认", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}


//@SuppressLint("UnrememberedMutableState")
//@Preview
//@Composable
//fun DialogPreview(){
//    AddPestDialog(mutableStateOf(true), mutableStateOf(mutableListOf<PestCategoryModel>(PestCategoryModel(pestCategory = PestCategory(1,"Default","   ",0)))))
//}
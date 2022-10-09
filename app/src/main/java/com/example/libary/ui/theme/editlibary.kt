package com.example.libary.ui.theme

import android.annotation.SuppressLint
import android.app.Instrumentation
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.libary.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LibaryEditScreen(libaryId:Int,navController:NavController,viewModel: libaryViewModel) {
    val scope = rememberCoroutineScope()
    val libary = remember { mutableStateOf(Constant.libarydetailPlaceHolder) }

    val currentlibary_candidate = remember {
        mutableStateOf(libary.value.name_candi)
    }
    val currentlibary_namebook = remember {
        mutableStateOf(libary.value.name_book)
    }
    val currentlibary_bookno = remember {
        mutableStateOf(libary.value.book_no)
    }
    val currentlibar_imageuri = remember {
        mutableStateOf(libary.value.image_uri)
    }
    val saveButton = remember {
        mutableStateOf(false)
    }
    val getImagerequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument(),
            onResult = { uri ->
                if (uri != null) {
                    libaryapp.getUriPermission(uri = uri)
                }
                currentlibar_imageuri.value = uri.toString()
                if (currentlibar_imageuri.value != libary.value.image_uri) {
                    saveButton.value = true
                }
            })
    LaunchedEffect(true)
    {
        scope.launch(Dispatchers.IO) {
            libary.value = viewModel.getlibary(libaryId ?: 0) ?: Constant.libarydetailPlaceHolder
            currentlibary_candidate.value = libary.value.name_candi
            currentlibary_namebook.value = libary.value.name_book
            currentlibary_bookno.value = libary.value.book_no
            currentlibar_imageuri.value = libary.value.image_uri
        }
    }

    LibaryTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(topBar = {
                GenericAppBar(
                    title = "Edit Note",
                    onItemClick = {
                        viewModel.updateNode(
                            libaryentity(
                                id = libary.value.id,
                                name_candi = currentlibary_candidate.value,
                                name_book = currentlibary_namebook.value,
                                book_no = currentlibary_bookno.value,
                                image_uri = currentlibar_imageuri.value
                            )
                        )
                        navController.popBackStack()
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.save),
                            contentDescription = "Save Note", tint = Color.Black
                        )
                    },
                    ToolBar = saveButton
                )
            },
                floatingActionButton = {
                    NoteFab(
                        contentDescription = stringResource(id = R.string.add_photo),
                        action = {
                            getImagerequest.launch(arrayOf("image/*"))
                        },
                        icon = R.drawable.camera
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize()
                ) {
                    if (currentlibar_imageuri.value != null && currentlibar_imageuri.value!!.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = Uri.parse(currentlibar_imageuri.value)).build()
                            ),
                            contentDescription = null, modifier = Modifier
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth()
                                .padding(6.dp), contentScale = ContentScale.Crop
                        )
                    }
                    TextField(value = currentlibary_candidate.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black, focusedLabelColor = Color.Black
                        ),
                        onValueChange = { value ->
                            currentlibary_candidate.value = value
                            if (currentlibary_candidate.value != libary.value.name_candi  ) {
                                saveButton.value = true
                            } else if (currentlibary_candidate.value == libary.value.name_candi
                                && currentlibary_bookno.value == libary.value.book_no &&
                                currentlibary_namebook.value == libary.value.name_book
                            ) {
                                saveButton.value = false
                            }
                        }, label = { Text(text = "Candidate Name") }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    TextField(value = currentlibary_namebook.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black, focusedLabelColor = Color.Black
                        ),
                        onValueChange = { value ->
                            currentlibary_namebook.value = value
                            if (currentlibary_namebook.value != libary.value.name_book) {
                                saveButton.value = true
                            } else if (currentlibary_candidate.value == libary.value.name_candi
                                && currentlibary_bookno.value == libary.value.book_no &&
                                currentlibary_namebook.value == libary.value.name_book
                            ) {
                                saveButton.value = false
                            }
                        }, label = { Text(text = "Name Of THe Book") }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    TextField(value = currentlibary_bookno.value,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black, focusedLabelColor = Color.Black
                        ),
                        onValueChange = { value ->
                            currentlibary_bookno.value = value
                            if (currentlibary_candidate.value != libary.value.book_no) {
                                saveButton.value = true
                            } else if (currentlibary_bookno.value == libary.value.book_no
                                && currentlibary_bookno.value == libary.value.book_no &&
                                currentlibary_namebook.value == libary.value.name_book
                            ) {
                                saveButton.value=false
                            }
                        }
                        , label = { Text(text = "Book Number")}
                    )
                }
            }
        }
    }
}


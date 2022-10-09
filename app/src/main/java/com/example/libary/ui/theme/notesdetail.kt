package com.example.libary.ui.theme

import android.annotation.SuppressLint
import android.net.Uri
import android.view.Surface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.libary.ui.theme.Constant.libarydetailPlaceHolder
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier
import java.nio.file.WatchEvent


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun notesdetail(libaryId:Int,navComposable:NavController,viewModel: libaryViewModel) {
    val scope = rememberCoroutineScope()
    val libary = remember {
        mutableStateOf(libarydetailPlaceHolder)
    }
    LaunchedEffect(true)
    {
        scope.launch {
            libary.value = viewModel.getlibary(libaryId) ?: libarydetailPlaceHolder
        }
    }

    LibaryTheme {
        Surface(
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(topBar = { GenericAppBar(
                title = libary.value.name_candi,
                onItemClick = {navComposable.navigate(Constant.libaryDetailNavigation(libary.value.id?:0))},
                icon = { Icon(imageVector = ImageVector.vectorResource(id = com.example.libary.R.drawable.edit_notes),
                    contentDescription ="Edit Libary" , tint = Color.Black)},
                ToolBar = remember {
                    mutableStateOf(true)
                }
            )}) {
                Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                    if(libary.value.image_uri!=null )
                    {
                        Image(painter = rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(data = Uri.parse(libary.value.image_uri)).build()),
                            contentDescription =null , modifier = androidx.compose.ui.Modifier
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth(), contentScale = ContentScale.Crop)
                    }
                    Text(text = libary.value.name_candi,
                        androidx.compose.ui.Modifier.padding(top=24.dp,start=24.dp,end=24.dp),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                        )
                    Text(text = libary.value.name_book,
                        androidx.compose.ui.Modifier.padding(12.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = libary.value.book_no,
                        androidx.compose.ui.Modifier.padding(12.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = libary.value.dateUpdated,
                        androidx.compose.ui.Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
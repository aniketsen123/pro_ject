package com.example.libary.ui.theme

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.libary.R
import okhttp3.internal.filterList
import okhttp3.internal.indexOfControlOrNonAscii
import java.util.Collections.emptyList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LibaryListScreen( navController: NavController, viewModel: libaryViewModel)
{


    val delete_libary = remember {
        mutableStateOf("")
    }
    val libaryQuery = remember {
        mutableStateOf("")
    }
    val libarytodelete = remember {
        mutableStateOf(listOf<libaryentity>())
    }
    val openDialog = remember {
        mutableStateOf(false)
    }

val libary=viewModel.libary.observeAsState()
    val context= LocalContext.current
    LibaryTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary
        ) {
            Scaffold(topBar = {
                GenericAppBar(
                    title = stringResource(id = R.string.app_name),
                    onItemClick = {
                        if(libary.value?.isEmpty()==false)
                        {
                            openDialog.value=true
                            delete_libary.value="Delete All !! Are You Sure About It?"
                            libarytodelete.value= (libary.value?: emptyList()) as SnapshotStateList<libaryentity>
                        }
                        else
                        {
                            Toast.makeText(context,"Nothing To Delete",Toast.LENGTH_SHORT).show()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.note_delete),
                            contentDescription = "Delete Note", tint = Color.Black
                        )
                    },
                    ToolBar = remember {
                        mutableStateOf(true)
                    }
                )
            },
                floatingActionButton = {
                    NoteFab(
                        contentDescription = stringResource(id = R.string.create_note),
                        action = {
                            navController.navigate(Constant.NAVIGATION_LIBARY_CREATE)
                        },
                        icon = R.drawable.note_add_icon
                    )
                }
            ) {
                    Column() {
                        Search(query = libaryQuery)
                        LibaryList(
                            libary = libary.value.orPlaceHolderList(),
                            opendialog = openDialog,
                            query = libaryQuery,
                            deleteLibary = delete_libary,
                            navController = navController,
                            libaryToBeDelete =libarytodelete
                        )
                    }
                DeleteDialog(opendialog = openDialog, text =delete_libary ,
                    action = {
                             libarytodelete.value.forEach{
                                 viewModel.deleteNode(it)
                             }
                    },
                    libaryTobedeleted =libarytodelete )
            }
        }
    }
}

























@Composable
fun Search(query:MutableState<String>)
{
    Column(modifier = Modifier.padding(top=12.dp,start=12.dp,end=12.dp, bottom = 0.dp)) {
        TextField(value =query.value ,
            placeholder = { Text(text = "Search....")},
            maxLines = 1,
            onValueChange ={query.value=it} ,
        modifier = Modifier
            .background(Color.White)
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(textColor = Color.Black),
        trailingIcon = {AnimatedVisibility(visible = query.value != null, enter = fadeIn(), exit = fadeOut()) {
           IconButton(onClick = { query.value="" }) {
     Icon(imageVector = ImageVector.vectorResource(id =R.drawable.icon_cross), contentDescription = stringResource(
         id = R.string.clear_search))
                  }
        }})
    }
}
@Composable
fun LibaryList(
    libary:List<libaryentity>,
    opendialog:MutableState<Boolean>,
    query:MutableState<String>,
    deleteLibary:MutableState<String>,
    navController:NavController,
    libaryToBeDelete:MutableState<List<libaryentity>>
)
{
var previousHeader=""
    
    LazyColumn(contentPadding = PaddingValues(12.dp),modifier=Modifier.background(MaterialTheme.colors.primary))
    {
        val querylibary= if (query.value.isEmpty()) {
            libary
        } else {
            libary.filter {
                it.name_candi.contains(query.value) || it.name_book.contains(query.value) || it.book_no.contains(
                    query.value
                )
            }
        }
            itemsIndexed(querylibary){index,lib->
                      if(lib.getDay()!=previousHeader)
                      {
                          Column(modifier = Modifier
                              .padding(6.dp)
                              .fillMaxWidth()) {
                              Text(text = lib.getDay(),color= Color.Black)
                          }
                          Spacer(modifier = Modifier
                              .fillMaxWidth()
                              .height(6.dp))
                          previousHeader=lib.getDay()
                      }
                LibaryListItem(
                    lib,opendialog,deleteLibary,navController,
                    if(index%2==0)
                    noteBGBlue
                    else
                        noteBGYellow
                ,
                    libaryToBeDelete
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp))
            }
        }
    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibaryListItem(libaryentity: libaryentity, opendialog: MutableState<Boolean>,
                   deleteLibary: MutableState<String>, navController: NavController, libayBackground:Color, libaryToBeDelete: MutableState<List<libaryentity>>)
{
    Box(modifier = Modifier
        .height(120.dp)
        .clip(RoundedCornerShape(12.dp))){
        Column(modifier = Modifier
            .background(libayBackground)
            .height(120.dp)
            .fillMaxWidth()
            .combinedClickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = rememberRipple(bounded = false),
                onClick = {
                    if (libaryentity.id != 0) {
                        navController.navigate(
                            Constant.libaryDetailNavigation(
                                libaryId = libaryentity.id ?: 0
                            )
                        )
                    }
                },
                onLongClick = {
                    if (libaryentity.id != 0) {
                        opendialog.value = true
                        deleteLibary.value = "Are You Sure You Want To This Information ?"
                        libaryToBeDelete.value = mutableListOf(libaryentity)
                    }
                }
            ) ) {
            Row {
                   if(libaryentity.image_uri!=null && libaryentity.image_uri.isNotEmpty())
                   {
                       Image(painter = rememberAsyncImagePainter(model = ImageRequest.Builder(
                           LocalContext.current).data(Uri.parse(libaryentity.image_uri)).build()), contentDescription = null,
                       modifier = Modifier.fillMaxWidth(0.25f), contentScale = ContentScale.Crop)
                   }
                Column {
                    Text(text = libaryentity.name_candi, color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
                    Text(text = libaryentity.name_book, color = Color.Black, fontWeight = FontWeight.Bold,maxLines = 2, modifier = Modifier.padding(horizontal = 12.dp))
                    Text(text = libaryentity.book_no, color = Color.Black, fontWeight = FontWeight.Bold, maxLines = 2, modifier = Modifier.padding(horizontal = 12.dp))
                    Text(text = libaryentity.dateUpdated, color = Color.Black, modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        }
    }
}
@Composable
fun NoteFab(
    contentDescription:String,icon:Int,action:()->Unit
)
{
    return FloatingActionButton(onClick = {action.invoke()}, backgroundColor = MaterialTheme.colors.primary) {
             Icon(imageVector = ImageVector.vectorResource(id = icon), contentDescription =contentDescription, tint = Color.Black )
    }
}
@Composable
fun DeleteDialog(
    opendialog: MutableState<Boolean>, text:MutableState<String>, action:()->Unit, libaryTobedeleted:MutableState<List<libaryentity>>
)
{
if(opendialog.value)
{
    AlertDialog(onDismissRequest ={ opendialog.value=false}, title = { Text(text = "Delete Information")},
    text={
        Column {
            Text(text = text.value)
        }
    },
    buttons = {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center) {
            Column() {
                Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                    onClick = {
                        action.invoke()
                        opendialog.value = false
                        libaryTobedeleted.value = mutableListOf()
                    }
                ) {
                    Text(text = "YES")
                    Spacer(modifier = Modifier.padding(12.dp))
                    Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    ),
                        onClick = {
                            opendialog.value = false
                            libaryTobedeleted.value = mutableListOf()
                        }
                    ) {
                        Text(text = "NO")
                    }

                }
            }
        }
    }) 
}
}
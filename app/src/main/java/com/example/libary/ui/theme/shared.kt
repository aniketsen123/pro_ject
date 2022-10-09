package com.example.libary.ui.theme

import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.textInputServiceFactory

@Composable
fun GenericAppBar(title:String,
onItemClick:(()->Unit)?,
icon:@Composable()(()->Unit)?,ToolBar:MutableState<Boolean>)
{
TopAppBar(title={Text(text = title)}, backgroundColor = MaterialTheme.colors.primary, actions = { IconButton(
    onClick = { onItemClick?.invoke() },
    content = {if(ToolBar.value)
    {
        icon?.invoke()
    }}
    )
    
})
}
package com.example.libary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.libary.ui.theme.*

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: libaryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          viewModel=ViewModelFactory(libaryapp.getDao()).create(libaryViewModel::class.java)
        setContent {
            val navController= rememberNavController()
            NavHost(navController = navController, startDestination =Constant.NAVIGATION_Libary_LIST )
            {
                    composable(Constant.NAVIGATION_Libary_LIST){
                        LibaryListScreen(navController = navController, viewModel =viewModel )
                    }
                composable(Constant.NAVIGATION_Libary_Detail,
                arguments = listOf(navArgument(Constant.NAVIGATION_Libary_id){
                    type= NavType.IntType
                }))
                {
                    navBackStackEntry->
                    navBackStackEntry.arguments?.getInt(Constant.NAVIGATION_Libary_id)?.let {
                        notesdetail(libaryId = it, navComposable = navController, viewModel =viewModel )
                    }
                }
                composable(Constant.NAVIGATION_Libary_edit,
                    arguments = listOf(navArgument(Constant.NAVIGATION_Libary_id){
                        type= NavType.IntType
                    }))
                {
                        navBackStackEntry->
                    navBackStackEntry.arguments?.getInt(Constant.NAVIGATION_Libary_id)?.let {
                        LibaryEditScreen(libaryId = it, navController = navController, viewModel =viewModel)
                    }
                }
                composable(Constant.NAVIGATION_LIBARY_CREATE){
                    LibaryCreateScreen( navController =navController , viewModel = viewModel)
                }
            }
        }
    }
}


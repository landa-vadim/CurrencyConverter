package com.landa.currencyconverter.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(this, mainViewModel)
        }
    }
}

@Composable
fun MyApp(context: Context, mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") { MainPage(context, mainViewModel, navController) }
        composable("convert_result_screen") { ConvertResult(mainViewModel) }
    }
}


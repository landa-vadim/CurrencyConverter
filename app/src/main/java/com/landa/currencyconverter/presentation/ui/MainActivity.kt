package com.landa.currencyconverter.presentation.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.landa.currencyconverter.ConverterApplication
import com.landa.currencyconverter.di.DaggerViewModelFactory
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    lateinit var mainViewModel: MainViewModel
    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ConverterApplication).component.inject(this)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class]
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


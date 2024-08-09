@file:OptIn(ExperimentalMaterial3Api::class)

package com.landa.currencyconverter.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel
import java.util.Calendar
import java.util.Date

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

@Composable
fun MainPage(context: Context, mainViewModel: MainViewModel, navController: NavController) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val date = mainViewModel.exchangeDate.collectAsState()

    val currenciesList = mainViewModel.allCurrencies.collectAsState()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val actualMonth = if (month.toString().count() < 2) "0${month + 1}" else "${month + 1}"
            val actualDay = if (day.toString().count() < 2) "0$day" else "$day"
            mainViewModel.userPickDate("$actualDay.$actualMonth.$year")
        }, year, month, day
    )


    var isExpandedFromCurrency by rememberSaveable {
        mutableStateOf(false)
    }

    var fromCurrency by rememberSaveable {
        mutableStateOf("EUR")
    }

    var isExpandedToCurrency by rememberSaveable {
        mutableStateOf(false)
    }

    val toCurrencyEmpty by rememberSaveable {
        mutableStateOf(listOf("All"))
    }

    val selectedToCurrencies = remember { mutableStateListOf<String>() }

    var amount by rememberSaveable {
        mutableStateOf("")
    }

// Общая колонка
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
// Название экрана
        Text(text = "Currency Exchange")
// Название выпадающего списка FROM CURRENCY
        Text(text = "From Currency")
// Бокс выпадающего списка FROM CURRENCY
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { isExpandedFromCurrency = it }) {
// Текст внутри бокса выпадающего списка FROM CURRENCY
            TextField(
                value = fromCurrency,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedFromCurrency)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
// Выпадающий список FROM CURRENCY
            ExposedDropdownMenu(
                expanded = isExpandedFromCurrency,
                onDismissRequest = { isExpandedFromCurrency = false },
                modifier = Modifier.fillMaxHeight(0.4F)
            ) {
                currenciesList.value
                    .filter {
                        !toCurrencyEmpty.contains(it)
                    }
                    .forEach {
// Элемент выпадающего списка FROM CURRENCY
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            onClick = {
                                fromCurrency = it.removeRange(3, it.count())
                                isExpandedFromCurrency = false
                            },
                        )
                    }
            }
        }
// Название выпадающего списка TO CURRENCY
        Text(text = "To Currency")
// Бокс выпадающего списка TO CURRENCY
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { isExpandedToCurrency = it }) {
// Текст внутри бокса выпадающего списка TO CURRENCY
            TextField(
                value = if (selectedToCurrencies.isEmpty()) {
                    Log.i("INSIDE BOX DROPDOWNMENU TOCURRENCY", "IF")
                    toCurrencyEmpty.joinToString("")
                } else {
                    Log.i("INSIDE BOX DROPDOWNMENU TOCURRENCY", "ELSE")
                    selectedToCurrencies.joinToString(", ")
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedFromCurrency)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
// Выпадающий список TO CURRENCY
            ExposedDropdownMenu(
                expanded = isExpandedToCurrency,
                onDismissRequest = { isExpandedToCurrency = false },
                modifier = Modifier.fillMaxHeight(0.4F)
            ) {
                currenciesList.value
                    .filter { it != fromCurrency }
                    .forEach {
                        DropdownMenuItem(
// Элемент выпадающего списка TO CURRENCY
                            text = { Text(text = it) },
                            onClick = {
                                val shortCut = it.removeRange(3, it.count())
                                if (shortCut in selectedToCurrencies) {
                                    Log.i("ITEM DROPDOWNMENU TOCURRENCY", "IF")
                                    selectedToCurrencies.remove(shortCut)
                                } else {
                                    Log.i("ITEM DROPDOWNMENU TOCURRENCY", "ELSE")
                                    selectedToCurrencies.add(shortCut)
                                }
                                isExpandedToCurrency = true
                            }
                        )
                    }
            }
        }
// Тайтл поля AMOUNT
        Text(text = "Amount")
// Текст поля AMOUNT
        TextField(
            value = amount,
            onValueChange = { amount = it },
        )
// Тайтл поля DATE
        Text(text = "Date")
// Бокс открытия календаря DATE
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier
                .clickable { datePickerDialog.show() }
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Gray)
        ) {
// Текст поля DATE
            Text(
                text = date.value,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
// Кнопка CONVERT
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("convert_result_screen") },
            content = { Text(text = "Convert") }
        )
    }
}

@Composable
fun ConvertResult(mainViewModel: MainViewModel) {

    val fromCurrency = mainViewModel.fromCurrency.collectAsState()
    val toCurrencies = mainViewModel.toCurrencies.collectAsState()
    val date = mainViewModel.exchangeDate.collectAsState()
    val resultList = mainViewModel.resultsList.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .background(Color.Red),
                text = "CurrencyExchange"
            )
        }
        Row {
            Column(
                modifier = Modifier
                    .background(Color.Red)
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = "From Currency"
                )
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = "To Currency"
                )
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = "Date"
                )
            }
            Column(
                modifier = Modifier
                    .background(Color.Green)
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = fromCurrency.value
                )
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = toCurrencies.value.joinToString { "" }
                )
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = date.value
                )
            }
        }
        resultList.value.forEach {
            Text(
                modifier = Modifier.padding(24.dp),
                text = it
            )
        }
    }
}


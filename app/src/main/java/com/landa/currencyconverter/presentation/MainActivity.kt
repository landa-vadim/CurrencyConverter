@file:OptIn(ExperimentalMaterial3Api::class)

package com.landa.currencyconverter.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.landa.currencyconverter.R
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

    val date = mainViewModel.exchangeDateFlow.collectAsState()

    val currenciesList = mainViewModel.allCurrencies.collectAsState()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            val actualMonth =
                if ((month + 1).toString().count() < 2) "0${month + 1}" else "${month + 1}"
            val actualDay = if (day.toString().count() < 2) "0$day" else "$day"
            mainViewModel.userPickDate("$actualDay.$actualMonth.$year")
        }, mainViewModel.year, mainViewModel.month, mainViewModel.day
    )

    var isExpandedFromCurrency by rememberSaveable {
        mutableStateOf(false)
    }
    val fromCurrency = mainViewModel.fromCurrencyStringFlow.collectAsState()

    var isExpandedToCurrency by rememberSaveable {
        mutableStateOf(false)
    }
    val toCurrencies = mainViewModel.toCurrenciesListFlow.collectAsState()
    val toCurrencyEmpty = mainViewModel.toCurrenciesEmptyListFlow.collectAsState()

    val amount = mainViewModel.amountStringFlow.collectAsState()

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
                value = fromCurrency.value,
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
                        !toCurrencies.value.contains(it)
                    }
                    .forEach {
// Элемент выпадающего списка FROM CURRENCY
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            onClick = {
                                val shortCut = it.removeRange(3, it.count())
                                mainViewModel.userPickFromCurrency(shortCut)
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
                value = if (toCurrencies.value.isEmpty()) {
                    toCurrencyEmpty.value[0]
                } else {
                    toCurrencies.value.joinToString(", ")
                },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor()
            )
// Выпадающий список TO CURRENCY
            ExposedDropdownMenu(
                expanded = isExpandedToCurrency,
                onDismissRequest = { isExpandedToCurrency = false },
                modifier = Modifier.fillMaxHeight(0.4F)
            ) {
                currenciesList.value
                    .filter { it != fromCurrency.value }
                    .forEach {
                        DropdownMenuItem(
// Элемент выпадающего списка TO CURRENCY
                            text = { Text(text = it) },
                            onClick = {
                                val shortCut = it.removeRange(3, it.count())
                                mainViewModel.userPickToCurrencies(shortCut)
                                isExpandedToCurrency = true
                            },
                        )
                    }
            }
        }
// Тайтл поля AMOUNT
        Text(text = "Amount")
// Текст поля AMOUNT
        TextField(
            value = amount.value,
            onValueChange = { mainViewModel.userPickAmount(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
// Тайтл поля DATE
        Text(text = "Date")
// Бокс открытия календаря DATE
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier
                .clickable {
                    datePickerDialog.datePicker.maxDate = mainViewModel.calendar.getTimeInMillis()
                    datePickerDialog.show()
                }
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
            onClick = {
                navController.navigate("convert_result_screen")
                mainViewModel.userClickedConvert()
            },
            content = { Text(text = "Convert") }
        )
    }
}

@Composable
fun ConvertResult(mainViewModel: MainViewModel) {

    val allCurrencies = mainViewModel.allCurrenciesShortCut.collectAsState()
    val fromCurrency = mainViewModel.fromCurrencyStringFlow.collectAsState()
    val toCurrencies = mainViewModel.toCurrenciesListFlow.collectAsState()
    val date = mainViewModel.exchangeDateFlow.collectAsState()
    val resultList = mainViewModel.resultsListFlow.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .padding(24.dp),
            text = "CurrencyExchange",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        RowForResults("From Currency", fromCurrency.value)
        RowForResults(
            "To Currency", if (toCurrencies.value.isEmpty()) {
                allCurrencies.value.toString().removeSurrounding("[", "]")
            } else {
                toCurrencies.value.toString().removeSurrounding("[", "]")
            }
        )
        RowForResults("Date", date.value)
        resultList.value.forEach {
            Text(
                modifier = Modifier.padding(24.dp),
                text = it
            )
        }
    }
}

@Composable
fun RowForResults(title: String, result: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(24.dp)
                .width(100.dp),

            text = title
        )
        Text(
            modifier = Modifier.padding(24.dp),
            text = result
        )
    }
}


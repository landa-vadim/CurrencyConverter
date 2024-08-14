package com.landa.currencyconverter.presentation.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Currency Exchange",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(text = "From Currency")
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { isExpandedFromCurrency = it }) {
            TextField(
                value = fromCurrency.value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor()
            )
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
        Text(text = "To Currency")
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { isExpandedToCurrency = it }) {
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
            ExposedDropdownMenu(
                expanded = isExpandedToCurrency,
                onDismissRequest = { isExpandedToCurrency = false },
                modifier = Modifier.fillMaxHeight(0.4F)
            ) {
                currenciesList.value
                    .filter { it != fromCurrency.value }
                    .forEach {
                        DropdownMenuItem(
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
        Text(text = "Amount")
        TextField(
            value = amount.value,
            onValueChange = { mainViewModel.userPickAmount(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Text(text = "Date")
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
            Text(
                text = date.value,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.landa.currencyconverter.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage(this, mainViewModel)
        }
    }
}

@Composable
fun MainPage(context: Context, mainViewModel: MainViewModel) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val date = mainViewModel.todayDate.collectAsState()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            mainViewModel.userPickDate("$dayOfMonth/$month/$year")
        }, year, month, day
    )


    var isExpandedFromCurrency by rememberSaveable {
        mutableStateOf(false)
    }

    var fromCurrency by rememberSaveable {
        mutableStateOf(mainViewModel.currencies[0])
    }

    var isExpandedToCurrency by rememberSaveable {
        mutableStateOf(false)
    }

    var toCurrency by rememberSaveable {
        mutableStateOf(mainViewModel.currencies.joinToString(", "))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Currency Exchange")
        Text(text = "From Currency")
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { isExpandedFromCurrency = it }) {
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
            ExposedDropdownMenu(
                expanded = isExpandedFromCurrency,
                onDismissRequest = { isExpandedFromCurrency = false }
            ) {
                mainViewModel.currencies.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = {
                            fromCurrency = it
                            isExpandedFromCurrency = false
                        }
                    )
                }
            }
        }
        Text(text = "To Currency")
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { isExpandedToCurrency = it }) {
            TextField(
                value = toCurrency,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedFromCurrency)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpandedToCurrency,
                onDismissRequest = { isExpandedToCurrency = false }
            ) {
                mainViewModel.currencies
                    .filter { it != fromCurrency }
                    .forEach {
                        DropdownMenuItem(
                            text = { Text(text = it) },
                            onClick = {
                                toCurrency = it
                                isExpandedToCurrency = false
                            }
                        )
                    }
            }
        }
        Text(text = "Amount")
        TextField(value = "1", onValueChange = {}, readOnly = false)
        Text(text = "Date")
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { },
            modifier = Modifier
                .clickable { datePickerDialog.show() }
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
            onClick = { }
        ) { }
    }
}
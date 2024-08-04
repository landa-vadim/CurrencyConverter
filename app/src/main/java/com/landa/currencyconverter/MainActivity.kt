@file:OptIn(ExperimentalMaterial3Api::class)

package com.landa.currencyconverter

import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPage() {

    val currentDate = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val changeDate = currentDate.format(formatter).toString()

    val currencies = listOf("USD", "EUR", "RUB")

    var isExpandedFromCurrency by remember {
        mutableStateOf(false)
    }

    var fromCurrency by remember {
        mutableStateOf(currencies[0])
    }

    var isExpandedToCurrency by remember {
        mutableStateOf(false)
    }

    var toCurrency by remember {
        mutableStateOf("")
    }

    var isExpandedDate by remember {
        mutableStateOf(false)
    }

    val calendarDialog = CalendarDialog()



    Column(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = "Currency Exchange")
        Text(text = "From Currency")
        ExposedDropdownMenuBox(expanded = false, onExpandedChange = { isExpandedFromCurrency = it }) {
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
            ExposedDropdownMenu(expanded = isExpandedFromCurrency, onDismissRequest = { isExpandedFromCurrency = false }
            ) {
                DropdownMenuItem(text = { Text(text = "Название валюты") },
                    onClick = {
                        fromCurrency = "Название валюты"
                        isExpandedFromCurrency = false
                    }
                )
            }
        }
        Text(text = "To Currency")
        ExposedDropdownMenuBox(expanded = false, onExpandedChange = { isExpandedToCurrency = it }) {
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
            ExposedDropdownMenu(expanded = isExpandedToCurrency, onDismissRequest = { isExpandedToCurrency = false }
            ) {
                DropdownMenuItem(text = { Text(text = "Название валюты") },
                    onClick = {
                        toCurrency = "Название валюты"
                        isExpandedToCurrency = false
                    }
                )
            }
        }
        Text(text = "Amount")
        TextField(value = "1", onValueChange = {})
        Text(text = "Date")
        ExposedDropdownMenuBox(expanded = false, onExpandedChange = { isExpandedDate = it }) {
            TextField(
                value = changeDate,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedDate)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = isExpandedDate, onDismissRequest = { isExpandedDate = false }
            ) {
                DropdownMenuItem(text = { Text(text = "") },
                    onClick = {

                        isExpandedToCurrency = false
                    }
                )
            }
        }
        Button(onClick = { }) { }

    }
}
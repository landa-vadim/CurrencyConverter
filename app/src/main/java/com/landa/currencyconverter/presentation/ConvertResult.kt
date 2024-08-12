package com.landa.currencyconverter.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel

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
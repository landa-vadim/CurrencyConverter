package com.landa.currencyconverter.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.landa.currencyconverter.data.ApiCurrencyRepository
import com.landa.currencyconverter.domain.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val apiCurrencyRepository = ApiCurrencyRepository()
    private val _allCurrencies = MutableStateFlow(listOf(""))
    val allCurrencies = _allCurrencies.asStateFlow()

    init {
        viewModelScope.launch {
            _allCurrencies.value = apiCurrencyRepository.getCurrenciesList()
        }
    }

    private val currentDate = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val _exchangeDate = MutableStateFlow(currentDate.format(formatter).toString())
    val exchangeDate = _exchangeDate.asStateFlow()


    fun userPickDate(date: String) {
        _exchangeDate.value = date
    }

    private val _fromCurrency = MutableStateFlow("EUR")
    val fromCurrency = _fromCurrency.asStateFlow()
    private val _toCurrencies = MutableStateFlow(listOf("All"))
    val toCurrencies = _toCurrencies.asStateFlow()
    private val _amount = MutableStateFlow(1)
    val amount = _amount.asStateFlow()

    private val exchangeCurrency = mutableStateOf(Currency(1, "", "", mapOf()))
    private val _resultsList = MutableStateFlow(listOf<String>())
    val resultsList = _resultsList.asStateFlow()

    fun userClickedConvert(fromCurrency: String, amount: Int) {
        viewModelScope.launch {
            _amount.value = amount
            val date = _exchangeDate.value.replace('.', '-')
            val dateForApi =
                "${date[7]}${date[8]}${date[9]}${date[10]}${date[3]}${date[4]}${date[5]}${date[6]}${date[1]}${date[2]}"
            viewModelScope.launch {
                exchangeCurrency.value =
                    apiCurrencyRepository.getCurrencyForExchange(dateForApi, fromCurrency)
            }.join()
            _resultsList.value = exchangeCurrency.value.rates.map {
                "${_amount.value} ${it.key} = ${it.value*_amount.value}"
            }
        }
    }

}
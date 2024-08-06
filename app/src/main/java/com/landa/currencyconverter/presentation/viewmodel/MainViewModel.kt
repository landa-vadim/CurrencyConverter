package com.landa.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.landa.currencyconverter.data.ApiCurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val apiCurrencyRepository = ApiCurrencyRepository()

    private val _allCurrencies = MutableStateFlow(listOf(""))
    val allCurrencies = _allCurrencies.asStateFlow()


    private val currentDate = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    private val _todayDate = MutableStateFlow(currentDate.format(formatter).toString())
    val todayDate = _todayDate.asStateFlow()

    val currencies = listOf("USD", "EUR", "RUB")

    init {
        viewModelScope.launch {
            _allCurrencies.value = apiCurrencyRepository.getCurrenciesList()
        }
    }

    fun userPickDate(date: String) {
        _todayDate.value = date
    }



}
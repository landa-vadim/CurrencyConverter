package com.landa.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val currentDate = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    private val _todayDate = MutableStateFlow(currentDate.format(formatter).toString())
    val todayDate = _todayDate.asStateFlow()

    val currencies = listOf("USD", "EUR", "RUB")

    fun userPickDate(date: String) {
        _todayDate.value = date
    }

}